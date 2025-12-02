package com.team.charge_proxy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class HelloController {

    private RestTemplate restTemplate;

    @Value("${manager.url}")
    private String managerUrl;

    public void HelloProxyController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public HelloController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/hello")
    public String hello() {
        String url = managerUrl + "/internal/hello";
        return restTemplate.getForObject(url, String.class);
    }
}
