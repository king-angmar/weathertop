<!-- TOC -->

- [1. 概念](#1-概念)
- [2. 简介](#2-简介)
    - [2.1. 优缺点](#21-优缺点)
        - [2.1.1. 优点](#211-优点)
        - [2.1.2. 缺点](#212-缺点)
- [3. 使用场景：](#3-使用场景)
- [4. 实现](#4-实现)
    - [4.1. 观察者](#41-观察者)
        - [4.1.1. 接口](#411-接口)
        - [4.1.2. 观察者动作](#412-观察者动作)
    - [4.2. 主题](#42-主题)
        - [4.2.1. 抽象类](#421-抽象类)
        - [4.2.2. 主题实现](#422-主题实现)
    - [4.3. 测试类](#43-测试类)

<!-- /TOC -->

# 1. 概念
> 当对象间存在一对多关系时，则使用观察者模式（Observer Pattern）。比如，当一个对象被修改时，则会自动通知它的依赖对象。观察者模式属于行为型模式。

# 2. 简介
- 意图：定义对象间的一种一对多的依赖关系，当一个对象的状态发生改变时，所有依赖于它的对象都得到通知并被自动更新。

- 主要解决问题：一个对象状态改变给其他对象通知的问题，而且要考虑到易用和低耦合，保证高度的协作。

- 使用场景：一个对象（目标对象）的状态发生改变，所有的依赖对象（观察者对象）都将得到通知，进行广播通知。

- 解决方式：使用面向对象技术，可以将这种依赖关系弱化。

- 代码点：在抽象类里有一个 ArrayList 存放观察者们。

- 应用实例： 
> 拍卖的时候，拍卖师观察最高标价，然后通知给其他竞价者竞价。
 
> 西游记里面悟空请求菩萨降服红孩儿，菩萨洒了一地水招来一个老乌龟，这个乌龟就是观察者，他观察菩萨洒水这个动作。

## 2.1. 优缺点

### 2.1.1. 优点

- 观察者和被观察者是抽象耦合的。 
- 建立一套触发机制。

### 2.1.2. 缺点

- 如果一个被观察者对象有很多的直接和间接的观察者的话，将所有的观察者都通知到会花费很多时间
- 如果在观察者和观察目标之间有循环依赖的话，观察目标会触发它们之间进行循环调用，可能导致系统崩溃
- 观察者模式没有相应的机制让观察者知道所观察的目标对象是怎么发生变化的，而仅仅只是知道观察目标发生了变化。

# 3. 使用场景：

一个抽象模型有两个方面，其中一个方面依赖于另一个方面。将这些方面封装在独立的对象中使它们可以各自独立地改变和复用。一个对象的改变将导致其他一个或多个对象也发生改变，而不知道具体有多少对象将发生改变，可以降低对象之间的耦合度。一个对象必须通知其他对象，而并不知道这些对象是谁。需要在系统中创建一个触发链，A对象的行为将影响B对象，B对象的行为将影响C对象……，可以使用观察者模式创建一种链式触发机制。

注意事项
-  JAVA 中已经有了对观察者模式的支持类

- 避免循环引用。 3、如果顺序执行，某一观察者错误会导致系统卡壳，一般采用异步方式。

# 4. 实现

观察者模式使用三个类 Subject、Observer 和 Client。
Subject 对象带有绑定观察者到 Client 对象和从 Client 对象解绑观察者的方法。我们创建 Subject 类、Observer 抽象类和扩展了抽象类 Observer 的实体类。

## 4.1. 观察者

### 4.1.1. 接口

~~~

package xyz.wongs.weather.design.observer;

public interface Observer {

    void see(BaseSubject abstractSubject);
}
~~~

### 4.1.2. 观察者动作

~~~

package xyz.wongs.weather.design.observer;

public class ObserverImpl implements Observer {

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public void see(BaseSubject baseSubject) {
        msg = ((Subject)baseSubject).getMsg();

    }
}

~~~


## 4.2. 主题

### 4.2.1. 抽象类

~~~

package xyz.wongs.weather.design.observer;

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

~~~


### 4.2.2. 主题实现

~~~

package xyz.wongs.weather.design.observer;

public class Subject extends BaseSubject {

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg){
        this.msg=msg;
        this.notifyObserver();
    }
}

~~~


## 4.3. 测试类

~~~

package xyz.wongs.weather;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import xyz.wongs.weather.design.observer.ObserverImpl;
import xyz.wongs.weather.design.observer.Subject;


public class AppTest {

    @Test
    public void testObserver() {

        Subject targetSubject = new Subject();

        ObserverImpl observerDesign1 = new ObserverImpl();

        ObserverImpl observerDesign2 = new ObserverImpl();

        ObserverImpl observerDesign3 = new ObserverImpl();
        ObserverImpl observerDesign4 = new ObserverImpl();

        targetSubject.addObserver(observerDesign1);
        targetSubject.addObserver(observerDesign2);
        targetSubject.addObserver(observerDesign3);
        targetSubject.addObserver(observerDesign4);

        targetSubject.setMsg(Thread.currentThread().getName());

        System.out.println(observerDesign1.getMsg());
        System.out.println(observerDesign2.getMsg());
        System.out.println(observerDesign3.getMsg());
        System.out.println(observerDesign4.getMsg());
    }
}


~~~