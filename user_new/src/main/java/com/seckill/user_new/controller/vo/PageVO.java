package com.seckill.user_new.controller.vo;

import lombok.Data;

@Data
public class PageVO {
    Integer current;//选择页
    Integer size;//页大小
    Integer order;//排序方式
    String keyWord;//关键词
    String keyWord2;//关键词2
}
