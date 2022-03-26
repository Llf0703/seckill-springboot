package com.seckill.user.service;

import com.seckill.user.entity.Item;
import com.seckill.user.entity.Items;

import java.util.List;

public interface ItemService {
      List<Items> findAll();
      Item findById(int id);

}