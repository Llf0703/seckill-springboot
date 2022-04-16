package com.seckill.user_new.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.StreamEntry;
import redis.clients.jedis.StreamEntryID;

import java.util.List;
import java.util.Map;

/**
 * @author Wky1742095859
 * @version 1.0
 * @ClassName RedisUtils
 * @description: redis工具类
 * @date 2022/3/20 2:30
 */
@Component
public class RedisUtils {
    public static final String doRechargeLua =
            "local doRecharge=redis.call('get',KEYS[1])" +
                    " if doRecharge==false then" +//查询为空
                    " return false" +//直接返回false(java:null)
                    " end" +
                    " redis.call('del',KEYS[1])" +//作废链接
                    " return doRecharge";
    public static String doRechargeLuaSHA;
    private static JedisPool jedisPool;

    @Autowired
    public void init(JedisPool jedisPool) {
        RedisUtils.jedisPool = jedisPool;
        loadScript();
    }

    private static void loadScript() {
        try (Jedis jedis = jedisPool.getResource()) {
            doRechargeLuaSHA = jedis.scriptLoad(doRechargeLua);
        } catch (Exception ignored) {
        }
    }
    public static Long zadd(String key,Double score,String f){
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zadd(key,score,f);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Long zadd(String key,Map<String,Double> fv){
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zadd(key,fv);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Object eval(String script, List<String> KEYS, List<String> ARGV) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.eval(script, KEYS, ARGV);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object evalSHA(String scriptSHA, List<String> KEYS, List<String> ARGV) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.evalsha(scriptSHA, KEYS, ARGV);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Double hincrbyfloat(String key, String f, float v) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hincrByFloat(key, f, v);
        } catch (Exception e) {
            return null;
        }
    }

    public static Boolean exists(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.exists(key);
        } catch (Exception e) {
            return null;
        }
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


    public static Long ttl(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.ttl(key);
        } catch (Exception e) {
            return null;
        }
    }

    public static Long del(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.del(key);
        } catch (Exception e) {
            return null;
        }
    }

    public static Long del(String... key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.del(key);
        } catch (Exception e) {
            return null;
        }
    }

    public static Long hset(String key, Map<String, String> fv) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hset(key, fv);
        } catch (Exception e) {
            return null;
        }
    }

    public static String hget(String key, String f) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hget(key, f);
        } catch (Exception e) {
            return null;
        }
    }

    public static List<String> hmget(String key, String... fs) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hmget(key, fs);
        } catch (Exception e) {
            return null;
        }
    }

    public static Map<String, String> hgetall(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hgetAll(key);
        } catch (Exception e) {
            return null;
        }
    }

    public static Long hset(String key, String f, String v) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hset(key, f, v);
        } catch (Exception e) {
            return null;
        }
    }

    public static String xadd(String streamName, Map<String, String> msg) {
        try (Jedis jd = jedisPool.getResource()) {
            //key, id, hash
            StreamEntryID id = jd.xadd(streamName, StreamEntryID.NEW_ENTRY, msg);
//			StreamEntryID id = jd.xadd("k", new StreamEntryID("1-1"), hash);
            return id.toString();
            //Map<String, String> hash2 = new HashMap<>();
            //hash2.put("name", "Jerry");
            //hash2.put("age", "12");
            //key, id, hash, len, ~(false)
            //StreamEntryID id2 = jd.xadd("k", StreamEntryID.NEW_ENTRY, hash2, 5, false);
            //System.out.println("xadd2 id:" + id2.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String xadd(String streamName, Map<String, String> msg, long maxLen, boolean approximateLength) {
        try (Jedis jd = jedisPool.getResource()) {
            StreamEntryID id = jd.xadd(streamName, StreamEntryID.NEW_ENTRY, msg, maxLen, approximateLength);
//			StreamEntryID id = jd.xadd("k", new StreamEntryID("1-1"), hash);
            return id.toString();
            //StreamEntryID id2 = jd.xadd("k", StreamEntryID.NEW_ENTRY, hash2, 5, approximateLength);
            //System.out.println("xadd2 id:" + id2.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static long xlen(String streamName) {
        try (Jedis jd = jedisPool.getResource()) {
            return jd.xlen(streamName);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static List<Map.Entry<String, List<StreamEntry>>> xreadall(String streamName) {
        try (Jedis jd = jedisPool.getResource()) {
            //count, block, key-id...
            return jd.xread(0, 0, new MyJedisEntry(streamName, "0"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Map.Entry<String, List<StreamEntry>>> xread(Integer count, String streamName) {
        try (Jedis jd = jedisPool.getResource()) {
            //count, block, key-id...
            return jd.xread(count, 0, new MyJedisEntry(streamName, "0"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Map.Entry<String, List<StreamEntry>>> xread(Integer count, String streamName, String start) {
        try (Jedis jd = jedisPool.getResource()) {
            //count, block, key-id...
            return jd.xread(count, 0, new MyJedisEntry(streamName, start));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<StreamEntry> xrange(String key, String start, String end, Integer count) {
        try (Jedis jd = jedisPool.getResource()) {
            //key, start, end, count
            //null表示无穷小或者无穷大
            StreamEntryID s = null;
            StreamEntryID e = null;
            if (start != null) s = new StreamEntryID(start);
            if (end != null) e = new StreamEntryID(end);
            return jd.xrange(key, s, e, count);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void xrevrange() {
        try (Jedis jd = jedisPool.getResource()) {
            //key, end, start, count
            List<StreamEntry> list = jd.xrevrange("k", null, null, 100);
            System.out.println("xrevrange:" + list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long xdel(String key, String id) {
        try (Jedis jd = jedisPool.getResource()) {
            //key, id...
            return jd.xdel(key, new StreamEntryID(id));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void xtrim() {
        try (Jedis jd = jedisPool.getResource()) {
            //key, len, ~(false)
            long result = jd.xtrim("k", 2, false);
            System.out.println("xtrim 2 return=" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class MyJedisEntry implements Map.Entry<String, StreamEntryID> {
    private String k;
    private StreamEntryID id;

    public MyJedisEntry(String key, String id) {
        this.k = key;
        if ("0".equals(id)) {
            this.id = new StreamEntryID();
        } else {
            this.id = new StreamEntryID(id);
        }
    }

    public MyJedisEntry(String key, StreamEntryID ID) {
        this.k = key;
        this.id = ID;
    }

    @Override
    public String getKey() {
        return k;
    }

    @Override
    public StreamEntryID getValue() {
        return id;
    }

    @Override
    public StreamEntryID setValue(StreamEntryID value) {
        this.id = value;
        return id;
    }
}