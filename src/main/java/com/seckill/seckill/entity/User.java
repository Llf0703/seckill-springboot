package com.seckill.seckill.entity;

import lombok.*;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Pattern;

import com.baomidou.mybatisplus.annotation.*;
import org.springframework.util.DigestUtils;

import com.seckill.seckill.utils.IdCardUtil;
import com.seckill.seckill.utils.MessageUitl;

@Data
@Builder(toBuilder = true)
@TableName("user")
public class User {

    @TableId(type = IdType.AUTO)
    private int id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date created_at, updated_at, deleted_at, age;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private String user_name, name, password, phone, id_card;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private int employment_status, credit_status;

    private String email;

    private BigDecimal balance;

    private boolean is_valid_phone() {
        if (this.phone == null) return false;
        return Pattern.matches("^(13[0-9]|14[01456879]|15[0-3,5-9]|16[2567]|17[0-8]|18[0-9]|19[0-3,5-9])\\d{8}$", this.phone);
    }

    private boolean is_valid_id_card() {
        if (this.phone == null) return false;
        return IdCardUtil.idCardValidate(this.id_card);
    }

    private boolean is_valid_password() {
        if (this.password == null) return false;
        return Pattern.matches("^(?:(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])).{6,20}$", this.password);
    }

    private boolean is_valid_name() {
        if (this.name == null) return false;
        return Pattern.matches("^[\u9FA6-\u9FCB\u3400-\u4DB5\u4E00-\u9FA5]{2,5}([\u25CF\u00B7][\u9FA6-\u9FCB\u3400-\u4DB5\u4E00-\u9FA5]{2,5})*$", this.name);
    }

    public MessageUitl register_check() {
        MessageUitl result = new MessageUitl();
        if (!is_valid_phone()) result.init(300, "invalid phone", false);
        else if (!is_valid_password()) result.init(300, "invalid password", false);
        else if (!is_valid_id_card()) result.init(300, "invalid id card", false);
        else if (!is_valid_name()) result.init(300, "invalid name", false);
        else result.init(200, "ok", true);
        return result;

    }

    public MessageUitl login_check() {
        MessageUitl result = new MessageUitl();
        if (!is_valid_phone()) result.init(300, "invalid phone", false);
        else if (!is_valid_password()) result.init(300, "invalid password", false);
        else result.init(200, "ok", true);
        return result;
    }

    public void password_to_md5() {
        String salt = "23fdt34w=-*.de";
        String md5 = DigestUtils.md5DigestAsHex(this.phone.concat(this.password).concat(salt).getBytes());
        this.password = md5;
    }

    public void get_age() throws ParseException {
        String date_string = this.id_card.substring(6, 14);
        DateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        fmt.setLenient(false); 
        fmt.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        this.age = fmt.parse(date_string);
    }

    public void generate_user_name() {
        String S = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer tmp = new StringBuffer();
        for (int i = 0; i < 8; i++) {
            int number = random.nextInt(62);
            tmp.append(S.charAt(number));
        }
        this.user_name = tmp.toString();
    }
}