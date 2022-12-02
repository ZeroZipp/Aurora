package com.zerozipp.client.utils;

import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Type;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Timer {
    private long lastMS;

    public Timer() {
        resetTime();
    }

    public void resetTime() {
        lastMS = System.currentTimeMillis();
    }

    public boolean hasTime(float time) {
        if(System.currentTimeMillis()-lastMS > time) {
            resetTime();
            return true;
        }else{
            return false;
        }
    }
}