package com.example.seckill_manager.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Arrays;
/**
 * @ClassName RedisUtils
 * @description:  redis工具类
 * @author Wky1742095859
 * @date 2022/3/20 2:30
 * @version 1.0
 */
@Component
public class RedisUtils {
    private static JedisPool jedisPool;

    @Autowired
    public void init(JedisPool jedisPool) {
        RedisUtils.jedisPool = jedisPool;
    }

    public static String set(String key, String val) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.set(key, val);
        } catch (Exception e) {
            return "0";
        }
    }

    public static String set(String key, String val, long ex) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.setex(key, ex, val);
        } catch (Exception e) {
            return "0";
        }
    }

    public static String mset(String... kv) {
        try (Jedis jedis = jedisPool.getResource()) {
            System.out.println(Arrays.toString(kv));
            return jedis.mset(kv);
        } catch (Exception e) {
            return "0";
        }
    }

    public static String get(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        } catch (Exception e) {
            return null;
        }
    }

    public static Boolean exist(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.exists(key);
        } catch (Exception e) {
            return false;
        }
    }

    public static long ttl(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.ttl(key);
        } catch (Exception e) {
            return -1;
        }
    }
    public static long del(String key){
        try (Jedis jedis= jedisPool.getResource()){
            return jedis.del(key);
        }catch (Exception e){
            return 0;
        }
    }
    public static long del(String... key){
        try (Jedis jedis= jedisPool.getResource()){
            return jedis.del(key);
        }catch (Exception e){
            return 0;
        }
    }
}
