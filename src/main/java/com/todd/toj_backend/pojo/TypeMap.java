package com.todd.toj_backend.pojo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Data
@ConfigurationProperties(prefix = "type-maps")
public class TypeMap {
    private Map<String, String> cppTypeMap;
    private Map<String, String> javaTypeMap;
    private Map<String, String> argumentFunctionMap;
    private Map<String, String> returnFunctionMap;
}
