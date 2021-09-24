package com.tai.chef.salefood.common;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class CommonResource {

    @Value("${application.token.length}")
    private int tokenLength;

    @Value("${spring.redis.timeout}")
    private int redisTimeout;
}
