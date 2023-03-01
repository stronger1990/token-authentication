package com.scorpios.tokenauthentication.mapper;

import com.scorpios.tokenauthentication.entity.User;
import org.apache.ibatis.annotations.*;

/**
 * @author Think
 * @Title: UserMapper
 * @ProjectName token-authentication
 * @Description: TODO
 * @date 2019/1/1823:04
 */

@Mapper
public interface UserMapper {
    @Select("select * from user where username=#{username} and password=#{password}")
    User getUser(@Param("username") String username, @Param("password") String password);
}
