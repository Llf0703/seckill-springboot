package com.seckill.user_new.service;

import com.seckill.user_new.common.Response;
import com.seckill.user_new.controller.vo.PageVO;
import com.seckill.user_new.controller.vo.QueryVO;
import com.seckill.user_new.entity.SeckillItems;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wky1742095859
 * @since 2022-04-07
 */
public interface ISeckillItemsService extends IService<SeckillItems> {
    Response getOverview(PageVO pageVO);
    Response getDetail(QueryVO queryVO);
    Response getSeckillLink(HttpServletRequest request, String seckillID);
    Response doSeckill(String seckillID);
    Response loadTest();
    Response getSeckillLinkTest(HttpServletRequest request, String seckillID);
    Response doSeckillTest(String seckillID);
}
