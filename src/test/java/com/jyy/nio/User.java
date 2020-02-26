package com.jyy.nio;

import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

public class User {

    private String name;
    private String password;
    private String nickName;
    private int age;

    private User(String name,String password,String nickName,
                 int age){
        this.name = name;
        this.password = password;
        this.nickName = nickName;
        this.age = age;
    }

    public static UserBuilder builder(){
        return new UserBuilder();
    }

    public static class UserBuilder{
        private String name;
        private String password;
        private String nickName;
        private int age;

        private UserBuilder(){

        }

        public UserBuilder name(String name){
            this.name = name;
            return this;
        }

        public UserBuilder password(String password){
            this.password = password;
            return this;
        }

        public UserBuilder nickName(String nickName){
            this.nickName = nickName;
            return this;
        }

        public UserBuilder age(int age){
            this.age = age;
            return this;
        }

        public User build(){
            return new User(name,password,nickName,age);
        }
    }
}
