package com.scorpios.tokenauthentication.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.scorpios.tokenauthentication.annotation.AuthToken;
import com.scorpios.tokenauthentication.utils.ConstantKit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * @author Think
 * @Title: AuthorizationInterceptor
 * @ProjectName token-authentication
 * @Description: TODO
 * @date 2019/1/1815:50
 */
@Slf4j
public class AuthorizationInterceptor implements HandlerInterceptor {

    //存放鉴权信息的Header名称，默认是Authorization
    private String httpHeaderName = "Authorization";

    //鉴权失败后返回的错误信息，默认为401 unauthorized
    private String unauthorizedErrorMessage = "401 unauthorized";

    //鉴权失败后返回的HTTP错误码，默认为401
    private int unauthorizedErrorCode = HttpServletResponse.SC_UNAUTHORIZED;

    /**
     * 存放登录用户模型Key的Request Key
     */
    public static final String REQUEST_CURRENT_KEY = "REQUEST_CURRENT_KEY";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        // 如果打上了AuthToken注解则需要验证token
        if (method.getAnnotation(AuthToken.class) != null || handlerMethod.getBeanType().getAnnotation(AuthToken.class) != null) {
            String token = request.getHeader(httpHeaderName);
            // String token = request.getParameter(httpHeaderName);
            log.info("Get token from request is {} ", token);
            String username = "";
            Jedis jedis = new Jedis("127.0.0.1", 6379);
            if (token != null && token.length() != 0) {
                username = jedis.get(token);
                log.info("Get username from Redis is {}", username);
            }
            if (username != null && !username.trim().equals("")) {
                //log.info("token birth time is: {}",jedis.get(username+token));
                Long tokeBirthTime = Long.valueOf(jedis.get(token + username));
                log.info("token Birth time is: {}", tokeBirthTime);
                Long diff = System.currentTimeMillis() - tokeBirthTime;
                log.info("token is exist : {} ms", diff);
                //重新设置Redis中的token过期时间
                if (diff > ConstantKit.TOKEN_RESET_TIME) {
                    jedis.expire(username, ConstantKit.TOKEN_EXPIRE_TIME);
                    jedis.expire(token, ConstantKit.TOKEN_EXPIRE_TIME);
                    log.info("Reset expire time success!");
                    Long newBirthTime = System.currentTimeMillis();
                    jedis.set(token + username, newBirthTime.toString());
                }

                //用完关闭
                jedis.close();
                request.setAttribute(REQUEST_CURRENT_KEY, username);
                return true;
            } else {
                JSONObject jsonObject = new JSONObject();
                PrintWriter out = null;
                try {
                    log.info("Token是正确的，但是根据Token在Redis找不到username");
                    //response.setStatus(unauthorizedErrorCode);
                    response.setStatus(200);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                    // jsonObject.put("code", ((HttpServletResponse) response).getStatus());
                    jsonObject.put("code", 401);
                    jsonObject.put("message", HttpStatus.UNAUTHORIZED + "  不合法用户");

                    response.setCharacterEncoding("utf-8");
                    out = response.getWriter();
                    out.println(jsonObject);


                    // 上面本来是写：
                    // response.setStatus(unauthorizedErrorCode);
                    // jsonObject.put("code", ((HttpServletResponse) response).getStatus());
                    // 客户端拿不到response，只拿到Http code = 401，而一般客户端的http success回调一般都固定http code=200。
                    // 这个时候客户端只能从最原始的result判断http code是401就约定显示什么，这样不太统一。
                    // 如果是Tomcat的问题导致http code不为200这么没办法，但是如果Tomcat服务器配置正常运行正常，就代表已经进入业务系统了，理论上http code=200了。
                    // 所以要写成：
                    // response.setStatus(200);
                    // jsonObject.put("code", 401);
                    // 表示业务系统已经正常返回，但是因为登录用户不满足某些条件，所以返回401以及message，请根据message信息作出修改。

                    return false;
                } catch (Exception e) {
                    log.info("异常：", e);
                    e.printStackTrace();
                    return false;
                } finally {
                    if (null != out) {
                        out.flush();
                        out.close();
                    }
                }
            }
        }
        request.setAttribute(REQUEST_CURRENT_KEY, null);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
