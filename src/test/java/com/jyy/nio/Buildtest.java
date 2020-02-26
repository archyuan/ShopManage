package com.jyy.nio;

public class Buildtest {
    public static void main(String[] args) {
        User user = User.builder()
                .name("sd")
                .age(20)
                .build();
        System.out.println(user);
    }
}
