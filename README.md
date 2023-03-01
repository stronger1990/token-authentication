# token-authentication

可以参考下面文章

https://blog.csdn.net/zxd1435513775/article/details/86555130


1、
# 修改后运行，浏览器访问http://localhost:8123/login3出现错误：
# 2023-03-01 08:41:58.014  INFO 12767 --- [nio-8123-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
# 2023-03-01 08:41:58.014  INFO 12767 --- [nio-8123-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
# 2023-03-01 08:41:58.019  INFO 12767 --- [nio-8123-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 5 ms
在application.properties增加这个即可：
spring.mvc.servlet.load-on-startup=1

2、
console输出最早的log：
OpenJDK 64-Bit Server VM warning: Options -Xverify:none and -noverify were deprecated in JDK 13 and will likely be removed in a future release.
点击锤子🔨右边的TokenAuthenticationApplication的下拉箭头，选择Edit configuration，将Java17改成Java11运行即可。

3、
浏览器显示：
Whitelabel Error Page

This application has no explicit mapping for /error, so you are seeing this as a fallback.

Wed Mar 01 10:54:24 CST 2023
There was an unexpected error (type=Not Found, status=404).
No message available
我以为是mapper的问题，或者是thymeleaf的问题，但是找不到错误的存在，后来试过clean项目，把浏览器重启，也无法解决问题，
直至我关闭IDEA，把项目的.idea删了重新导入项目就可以了。
我认为应该是IDEA没有把新增的controller一起编译的原因。

