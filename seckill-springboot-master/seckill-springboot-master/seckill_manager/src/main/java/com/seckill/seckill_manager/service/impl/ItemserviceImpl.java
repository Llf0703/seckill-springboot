package com.seckill.seckill_manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seckill.seckill_manager.common.Response;
import com.seckill.seckill_manager.controller.vo.ItemVO;
import com.seckill.seckill_manager.entity.ManagerUsers;
import com.seckill.seckill_manager.entity.SeckillItems;
import com.seckill.seckill_manager.mapper.ManagerUsersMapper;
import com.seckill.seckill_manager.mapper.SeckillItemsMapper;
import com.seckill.seckill_manager.service.ItemService;
import com.seckill.seckill_manager.utils.JWTAuth;
import com.seckill.seckill_manager.utils.RedisUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.TimeZone;

@Service
public class ItemserviceImpl extends ServiceImpl<SeckillItemsMapper, SeckillItems> implements ItemService {

    @Resource
    private SeckillItemsMapper seckillItemsMapper;

    @Resource
    private ManagerUsersMapper managerUsersMapper;

    @Override
    public Response add_item_service(ItemVO item_VO, String token, String ip) {
        HashMap<String, Object> result = JWTAuth.parseToken(token);
        if (!result.get("status").equals(true)) return Response.authErr("登录失效");
        if (ip == null) return Response.authErr("登录失效");
        String account = result.get("account").toString();
        String userInfoStr = RedisUtils.get(account + "," + ip);
        String MD5Password = RedisUtils.get(account);
        String loginUser = RedisUtils.get(ip + "_user");
        //检查ip对应的登录账号及token账号是否一致
        if (loginUser == null || !loginUser.equals(account)) return Response.authErr("登录失效");
        if (userInfoStr == null) return Response.authErr("登录失效");
        String[] userInfo = userInfoStr.split(",");
        if (MD5Password == null) {
            QueryWrapper<ManagerUsers> queryWrapper = new QueryWrapper<>();
            queryWrapper.isNull("deleted_at").eq("account", account);
            ManagerUsers managerUser = managerUsersMapper.selectOne(queryWrapper);
            if (managerUser == null) return Response.authErr("登录失效");
            if (managerUser.getSeckillItemsPermissions() != 2) return Response.authErr("无权限");
            //检查密码是否更改及ip所登录的token与传回的token是否一致
            if (!Objects.equals(managerUser.getPassword(), userInfo[1]) || !Objects.equals(token, userInfo[0]))
                return Response.authErr("登录失效");
        }
        else if (!Objects.equals(userInfo[0], token) || !Objects.equals(userInfo[1], MD5Password))
            return Response.authErr("登录失效");

        Long amount = item_VO.getAmount(), stock = item_VO.getStock();
        if (amount<=0 || stock<=0)  return Response.systemErr("数值无效");
        Date date = new Date();
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fmt.setLenient(false); 
        fmt.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        SeckillItems item = SeckillItems.builder()
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
            return Response.systemErr("error");
        }
        seckillItemsMapper.insert(item);
        return Response.success("success");
    }
}
