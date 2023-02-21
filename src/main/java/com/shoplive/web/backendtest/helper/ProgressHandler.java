package com.shoplive.web.backendtest.helper;

import java.util.HashMap;
import java.util.Map;

public class ProgressHandler {

    public final Map<String, Integer> status;

    public ProgressHandler(){
        this.status = new HashMap<>();
    }

    public void updateProgress(String name, Integer value){
        if (value > 100){
            throw new RuntimeException("진행 상태는 100보다 클 수 없습니다.");
        }
        if (value < 0){
            throw new RuntimeException("진행 상태는 0보다 작을 수 없습니다.");
        }

        status.put(name, value);        
    }

    public void deleteProgress(String name){
        status.remove(name);
    }

    public Integer getProgress(String name){
        return status.get(name);
    }
}