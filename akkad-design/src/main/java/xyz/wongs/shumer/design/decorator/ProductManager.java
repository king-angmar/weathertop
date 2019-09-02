package xyz.wongs.shumer.design.decorator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductManager implements Person {

    @Override
    public void run() {
        log.error("乘坐专车");
    }

    @Override
    public void sleep() {
        log.error("为了产品特性，很少睡觉");
    }

    @Override
    public void eat() {
        log.error("为了产品特性，也很少吃饭");
    }

    @Override
    public void drink() {
        log.error("为了产品特性，也很少喝水");
    }
}
