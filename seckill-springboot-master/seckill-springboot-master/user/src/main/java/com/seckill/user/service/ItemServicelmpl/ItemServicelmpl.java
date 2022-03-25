package com.seckill.user.service.ItemServicelmpl;

import com.seckill.user.entity.Item;
import com.seckill.user.entity.Items;
import com.seckill.user.mapper.ItemMapper;
import com.seckill.user.service.ItemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ItemServicelmpl implements ItemService {
    @Resource
    private ItemMapper itemMapper;

    @Override
    public List<Items> findAll(){
        return itemMapper.find_all();
    }
    @Override
    public Item findById(int id){
        return itemMapper.find_by_id(id);
    }
}