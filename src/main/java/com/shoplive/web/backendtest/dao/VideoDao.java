package com.shoplive.web.backendtest.dao;

import org.apache.ibatis.annotations.Mapper;

import com.shoplive.web.backendtest.entity.Video;

@Mapper
public interface VideoDao {
    
    int insert(Video video);
    int update(Video video);
    Video getById(Long id);
}
