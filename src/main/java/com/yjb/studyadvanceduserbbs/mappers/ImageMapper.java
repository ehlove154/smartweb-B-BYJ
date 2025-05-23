package com.yjb.studyadvanceduserbbs.mappers;

import com.yjb.studyadvanceduserbbs.entities.ImageEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.awt.*;

@Mapper
public interface ImageMapper {
    int insert(@Param("image") ImageEntity image);

    ImageEntity selectById(@Param("id") int id);
}
