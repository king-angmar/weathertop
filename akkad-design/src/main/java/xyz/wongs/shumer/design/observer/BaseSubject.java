package xyz.wongs.shumer.design.observer;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseSubject {

    private List<Observer> list = new ArrayList<Observer>();

    public void addObserver(Observer observer){
        list.add(observer);
    }

    public void removeObserver(Observer observer){
        list.remove(observer);
    }

    public void notifyObserver(){
        for(Observer observer:list){
            observer.see(this);
        }
    }
}
