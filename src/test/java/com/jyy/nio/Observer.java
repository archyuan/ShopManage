package com.jyy.nio;

public class Observer {

    Subject subject;
    public Observer(Subject s){
        subject = s;
    }
    public void update(){
        System.out.println(this+" 更新："+subject.getState());
    }
}
