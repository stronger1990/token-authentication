package com.scorpios.tokenauthentication;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.scorpios.tokenauthentication.mapper")
public class TokenAuthenticationApplication {

	public static void main(String[] args) {
		SpringApplication.run(TokenAuthenticationApplication.class, args);
	}

}

