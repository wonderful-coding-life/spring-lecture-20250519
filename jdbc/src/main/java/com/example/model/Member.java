package com.example.model;

import lombok.Data;

@Data
public class Member {
    private Long id;
    private String name;
    private String email;
    private Integer age;

    public Member(Long id, String name, String email, Integer age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public String toString() {
        return "Member(id=" + id + ", name=" + name + ", email=" + email + ", age=" + age + ")";
    }
}
