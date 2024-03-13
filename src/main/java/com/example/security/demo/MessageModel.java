package com.example.security.demo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
@AllArgsConstructor
public class MessageModel {
    @JsonProperty("name")
    private String name ;
}
