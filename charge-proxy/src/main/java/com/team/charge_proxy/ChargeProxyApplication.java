package com.team.charge_proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ChargeProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChargeProxyApplication.class, args);
	}

}
