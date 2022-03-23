package com.seckill.manager.service.ItemServiceImpl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seckill.manager.controller.vo.ItemVO;
import com.seckill.manager.entity.Item;
import com.seckill.manager.entity.Manager;
import com.seckill.manager.mapper.ItemMapper;
import com.seckill.manager.mapper.ManagerMapper;
import com.seckill.manager.service.ItemService;
import com.seckill.manager.utils.JWTUtil;
import com.seckill.manager.utils.MessageUitl;

import org.springframework.stereotype.Service;

@Service
public class ItemserviceImpl implements ItemService {

    @Resource
    private ItemMapper item_mapper;

    @Resource
    private ManagerMapper manager_mapper;

    public HashMap<String, Object> add_item_service(ItemVO item_VO, String token) {
        MessageUitl result = new MessageUitl();
        String account = JWTUtil.verifyToken(token);
        if (account == null) {
            result.auth_error("未登录");
            return result.getMap();
        }
        QueryWrapper<Manager> manager_wrapper = new QueryWrapper<>();
        manager_wrapper.eq("account", account);
        Manager manager = manager_mapper.selectOne(manager_wrapper);
        if (manager == null) {
            result.auth_error("未登录");
            return result.getMap();
        }
        if (manager.getProduct_permissions() != 2) {
            result.auth_error("无增加权限");
            return result.getMap();
        }

        Long amount = item_VO.getAmount(), stock = item_VO.getStock();
        if (amount<=0 || stock<=0) {
            result.auth_error("数字无效");
            return result.getMap();
        }
        Date date = new Date();
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fmt.setLenient(false); 
        fmt.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        Item item = Item.builder()
            .created_at(date)
            .updated_at(date)
            .title(item_VO.getTitle())
            .description(item_VO.getDescription())
            .amount(item_VO.getAmount())
            .stock(item_VO.getStock())
            .remaining_stock(item_VO.getStock())
            .build();
        try {
            Date start_time = fmt.parse(item_VO.getStart_time());
            Date end_time = fmt.parse(item_VO.getEnd_time());
            item = item.toBuilder()
            .start_time(start_time)
            .end_time(end_time)
            .build();
        } catch (ParseException e) {
            result.system_error("error");
            return result.getMap();
        }
        item_mapper.insert(item);
        result.success("成功");
        return result.getMap();
    }
}
