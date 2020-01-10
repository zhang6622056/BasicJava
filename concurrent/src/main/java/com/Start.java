package com;

import java.util.concurrent.locks.ReentrantLock;

public class Start {

    private ReentrantLock reentrantLock = new ReentrantLock();
    int value = 1;


    public void addOne(){
        reentrantLock.lock();
        try{
            value += 1;
        }finally{
            reentrantLock.unlock();
        }
    }







}
