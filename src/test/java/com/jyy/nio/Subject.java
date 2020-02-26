package com.jyy.nio;

import java.util.ArrayList;
import java.util.List;

public class Subject {

    List<Observer> observers;
    int state;
    private Subject(){
        observers = new ArrayList<>();
    }
    public static Subject getInstance(){
        return new Subject();
    }

    public void add(Observer observer){
        observers.add(observer);
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        notifyAllObserver();
    }

    private void notifyAllObserver(){
        for(Observer observer : observers){
            observer.update();
        }
    }
}
