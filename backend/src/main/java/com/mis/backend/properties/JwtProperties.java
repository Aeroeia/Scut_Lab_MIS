package com.mis.backend.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sky.jwt")
@Data
public class JwtProperties {

    /**
     * 生成jwt令牌相关配置
     */
    private String SecretKey;
    private long Ttl;
    private String TokenName;



}
