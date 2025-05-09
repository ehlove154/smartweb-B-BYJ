package com.yjb.test250502.mappers;


import com.yjb.test250502.entities.TickerEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TickerMapper {
    int deleteById(@Param(value = "id") String id);

    int insert(@Param(value = "ticker") TickerEntity ticker);


    TickerEntity[] selectAll();

    TickerEntity selectById(@Param(value = "id") String id);
}
