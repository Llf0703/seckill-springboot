package com.seckill.user_new.controller.dto;

import com.seckill.user_new.entity.SeckillItems;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
class SeckillItemOverview {
    private Integer id;

    private String title;

    private BigDecimal amount;

    private String description;

    private String tip;
}


public class SeckillItemsDTO {
    private static String handleTip(LocalDateTime startTime, LocalDateTime endTime) {
        LocalDateTime nowTime = LocalDateTime.now();
        if (startTime.isAfter(nowTime.plusHours(2))) return "未开始";
        if (startTime.isAfter(nowTime)) return "即将开始";
        if (startTime.isBefore(nowTime) && endTime.isAfter(nowTime.plusHours(2))) return "进行中";
        if (endTime.isAfter(nowTime) && endTime.isBefore(nowTime.plusHours(2))) return "即将结束";
        if (endTime.isBefore(nowTime)) return "已结束";
        else return "状态异常";
    }

    public static SeckillItemOverview toSeckillItemsOverview(SeckillItems seckillItems) {
        SeckillItemOverview seckillItemOverview = new SeckillItemOverview();
        seckillItemOverview.setId(seckillItems.getId());
        seckillItemOverview.setTitle(seckillItems.getTitle());
        seckillItemOverview.setAmount(seckillItems.getAmount());
        seckillItemOverview.setDescription(seckillItems.getDescription());
        seckillItemOverview.setTip(handleTip(seckillItems.getStartTime(), seckillItems.getEndTime()));
        return seckillItemOverview;
    }

    public static List<SeckillItemOverview> toSeckillItemsOverview(List<SeckillItems> seckillItemsList) {
        List<SeckillItemOverview> seckillItemOverviewList = new LinkedList<>();
        for (SeckillItems item : seckillItemsList) {
            seckillItemOverviewList.add(toSeckillItemsOverview(item));
        }
        return seckillItemOverviewList;
    }
}
