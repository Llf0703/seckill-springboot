package com.seckill.user.controller;

import com.seckill.user.entity.Items;
import com.seckill.user.service.ItemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class  DTO {
    @Resource
    private ItemService itemService;
    public List<Items> TimeComparator_1(){
        List<Items> AllItems = itemService.findAll();

        Date date = new Date();
        List<Items> items;
        items = AllItems.stream().filter(s -> s.getStart_time().after(date)).collect(Collectors.toList());
        return items;
    }
    public List<Items> TimeComparator_2() {
        List<Items> AllItems = itemService.findAll();

        Date date = new Date();
        List<Items> items;
        items = AllItems.stream().filter(s -> s.getEnd_time().after(date)).collect(Collectors.toList());
        return items;
    }
    public List<Items> TimeComparator_3() {
        List<Items> AllItems = itemService.findAll();

        Date date = new Date();
        List<Items> items;
        items = AllItems.stream().filter(s -> s.getEnd_time().before(date)).collect(Collectors.toList());
        return items;
    }
}
