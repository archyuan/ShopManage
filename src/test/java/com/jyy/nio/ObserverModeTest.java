package com.jyy.nio;

public class ObserverModeTest {

    public static void main(String[] args) {
        Subject subject = Subject.getInstance();
        Observer observer = new Observer(subject);
        Observer observer1 = new Observer(subject);
        subject.add(observer);
        subject.add(observer1);
        subject.setState(1);
    }
}
