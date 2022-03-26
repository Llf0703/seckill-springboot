package com.seckill.user.entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

@Data
@Builder(toBuilder = true)
public class Item {

    private String title;

    private BigInteger amount;

    private int id;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date start_time, end_time;

    private String description;

    private BigInteger remaining_stock;

//    private BigInteger stock;
//    private Date created_at;
//    private Date updated_at;
//    private Date deleted_at;

}