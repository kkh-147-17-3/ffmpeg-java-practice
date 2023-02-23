package com.shoplive.web.backendtest.interfaces;

public interface Progressible {
    Integer getProgress(String id);
    void deleteProgress(String id);
    void updateProgress(String id, Integer percentage);
}
