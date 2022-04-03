package com.seckill.user_new.mapper;

import com.seckill.user_new.entity.Item;
import com.seckill.user_new.entity.Items;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ItemMapper {
    Item find_by_id(int id);
    List<Items> find_all();
}
