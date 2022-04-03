package com.seckill.user_new.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

@Data
@Builder(toBuilder = true)
public class Items {

    private String title;

    private BigInteger amount;

    private int id;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date start_time, end_time;
}