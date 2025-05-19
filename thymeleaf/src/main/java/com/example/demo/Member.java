package com.example.demo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Member {
    private Long id;
    private String name;
    private String email;
    private Integer age;
}
