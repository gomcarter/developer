package com.yiayoframework.redis.tool;

import com.yiayoframework.base.mapper.JsonMapper;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RedisProxy {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private JedisCluster jedisCluster;

    private JedisPool jedisPool;

    private boolean needCache;

    private static final String OK = "OK";

    public enum EXPX {
        //秒
        EX,
        //毫秒
        PX;
    }

    public enum NXXX {
        //不覆盖设置
        NX,
        //覆盖设置
        XX;
    }

    /**
     * @param key
     * @return 返回剩余的毫秒数
     */
    public Long pttl(final String key) {
        if (jedisCluster != null) {
            return jedisCluster.pttl(key);
        } else {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.pttl(key);
            }
        }
    }

    /**
     * @param key
     * @return 返回剩余的秒数
     */
    public Long ttl(final String key) {
        if (jedisCluster != null) {
            return jedisCluster.ttl(key);
        } else {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.ttl(key);
            }
        }
    }

    public boolean set(final String key, final Object value, final NXXX nxxx, final EXPX expx, final long time) {
        return this.set(key, JsonMapper.buildNonNullMapper().toJson(value), nxxx, expx, time);
    }

    public boolean sadd(final String key, final Object value, final NXXX nxxx, final EXPX expx, final long time) {
        return this.sadd(key, value, nxxx, expx, time);

    }

    public boolean set(final String key, final Object value) {
        return this.set(key, value, NXXX.XX, EXPX.EX, Long.MAX_VALUE);
    }

    public boolean set(final String key, final Object value, Long seconds) {
        return this.set(key, value, NXXX.XX, EXPX.EX, seconds);
    }

    public long decr(final String key) {
        if (jedisCluster != null) {
            return jedisCluster.decr(key);
        } else {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.decr(key);
            }
        }
    }

    public long decrBy(final String key, final long increment) {
        if (jedisCluster != null) {
            return jedisCluster.decrBy(key, increment);
        } else {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.decrBy(key, increment);
            }
        }
    }

    public long incr(final String key) {
        if (jedisCluster != null) {
            return jedisCluster.incr(key);
        } else {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.incr(key);
            }
        }
    }

    public long incrBy(final String key, final long increment) {
        if (jedisCluster != null) {
            return jedisCluster.incrBy(key, increment);
        } else {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.incrBy(key, increment);
            }
        }
    }

    private int getSeconds(final EXPX expx, final long time) {
        if (time <= 0) {
            return 0;
        }

        int seconds = (int) Math.min(time, Integer.MAX_VALUE);
        if (expx == EXPX.PX) {
            seconds = seconds / 1000;
        }
        return seconds;
    }

    public boolean expire(final String key, final long time) {
        //redis client有bug，nx可以，xx不行，所以有以下不可思议的代码
        if (jedisCluster != null) {
            Long a = jedisCluster.expire(key, (int) time);
            return a > 0L;
        } else if (jedisPool != null) {
            try (Jedis jedis = jedisPool.getResource()) {
                Long a = jedis.expire(key, (int) time);
                return a > 0L;
            } catch (Exception e) {
                logger.error("访问redis失败, set：", e);
            }
        }
        return false;
    }

    public boolean set(final String key, final String value, final NXXX nxxx, final EXPX expx, final long time) {
        //redis client有bug，nx可以，xx不行，所以有以下不可思议的代码
        if (jedisCluster != null) {
            if (nxxx == NXXX.NX) {
                Long a = jedisCluster.setnx(key, value);
                boolean r;
                if (r = (a > 0L)) {
                    //FIXME: 非原子操作
                    jedisCluster.expire(key, (int) time);
                }
                return r;
            } else {
                return OK.equalsIgnoreCase(jedisCluster.setex(key, getSeconds(expx, time), value));
            }
        } else if (jedisPool != null) {
            try (Jedis jedis = jedisPool.getResource()) {
                if (nxxx == NXXX.NX) {
                    Long a = jedis.setnx(key, value);
                    boolean r;
                    if (r = (a > 0L)) {
                        //FIXME: 非原子操作
                        jedis.expire(key, (int) time);
                    }
                    return r;
                } else {
                    return OK.equalsIgnoreCase(jedis.setex(key, getSeconds(expx, time), value));
                }
            } catch (Exception e) {
                logger.error("访问redis失败, set：", e);
            }
        }
        return false;
    }

    public boolean set(final String key, final String value) {
        return this.set(key, value, NXXX.XX, EXPX.EX, Long.MAX_VALUE);
    }

    public boolean set(final String key, final String value, Long seconds) {
        return this.set(key, value, NXXX.XX, EXPX.EX, seconds);
    }

    public boolean lock(final String key, final String value, Long seconds) {
        return this.set(key, value, NXXX.NX, EXPX.EX, seconds);
    }

    public boolean lock(final String key, Long seconds) {
        return this.set(key, "true", NXXX.NX, EXPX.EX, seconds);
    }

    public boolean lock(final String key) {
        return this.set(key, "true", NXXX.NX, EXPX.EX, Long.MAX_VALUE);
    }

    public String get(final String key) {
        if (jedisCluster != null) {
            return jedisCluster.get(key);
        } else if (jedisPool != null) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.get(key);
            } catch (Exception e) {
                logger.error("访问redis失败, get：", e);
            }
        }
        return null;
    }

    public Set<String> smembers(final String key) {
        if (jedisCluster != null) {
            return jedisCluster.smembers(key);
        } else if (jedisPool != null) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.smembers(key);
            } catch (Exception e) {
                logger.error("访问redis失败, smembers：", e);
            }
        }
        return null;
    }


    public Long del(final String... key) {
        if (jedisCluster != null) {
            return jedisCluster.del(key);
        } else if (jedisPool != null) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.del(key);
            } catch (Exception e) {
                logger.error("访问redis失败, del：", e);
            }
        }
        return 0L;
    }

    public Long del(final String key) {
        return this.del(new String[]{key});
    }


    //TODO:更多方法待拓展
    //哈希类型基本操作
    public Map<String, String> hgetAll(String key) {
        if (null != jedisCluster) {
            return jedisCluster.hgetAll(key);
        } else if (null != jedisPool) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.hgetAll(key);
            } catch (Exception e) {
                logger.error("访问redis失败, hgetAll：", e);
            }
        }
        return null;
    }

    //todo 后期看能不能写Lua脚本保证设置键和设置键过期时间事务
    public boolean hmsetAndExpire(String key, Map<String, String> map, Integer seconds) {
        if (null != jedisCluster) {
            //因为设置键和设置键的过期时间两步不是原子性的，此处上锁保证两个命令同时执行，保证原子性;
            if (OK.equalsIgnoreCase(jedisCluster.hmset(key, map))) {
                jedisCluster.expire(key, seconds);
            }
            return true;
        } else if (null != jedisPool) {
            //此处锁住的原因同上，保证原子性
            try (Jedis jedis = jedisPool.getResource()) {
                if (OK.equalsIgnoreCase(jedis.hmset(key, map))) {
                    jedis.expire(key, seconds);
                }
                return true;
            } catch (Exception e) {
                logger.error("访问redis失败, hmsetAndExpire：", e);
            }
        }
        return false;
    }

    /**
     * 移除某个用户
     *
     * @param key
     * @param member
     * @return
     */
    public Long zrem(final String key, String member) {
        if (jedisCluster != null) {
            return jedisCluster.zrem(key, member);
        } else if (jedisPool != null) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.zrem(key, member);
            } catch (Exception e) {
                logger.error("访问redis失败, zrem：", e);
            }
        }
        return null;
    }

    /**
     * 获取某个用户的排名情况，降序
     *
     * @param key
     * @param member
     * @return
     */
    public Long zrevrank(final String key, String member) {
        if (jedisCluster != null) {
            return jedisCluster.zrevrank(key, member);
        } else if (jedisPool != null) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.zrevrank(key, member);
            } catch (Exception e) {
                logger.error("访问redis失败, zrevrank：", e);
            }
        }
        return null;
    }

    /**
     * 获取某个用户的分数
     *
     * @param key
     * @param member
     * @return
     */
    public Double zscore(final String key, String member) {
        if (jedisCluster != null) {
            return jedisCluster.zscore(key, member);
        } else if (jedisPool != null) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.zscore(key, member);
            } catch (Exception e) {
                logger.error("访问redis失败, zscore：", e);
            }
        }
        return null;
    }

    /**
     * 获取某个用户的排名情况，升序
     *
     * @param key
     * @param member
     * @return
     */
    public Long zrank(final String key, String member) {
        if (jedisCluster != null) {
            return jedisCluster.zrank(key, member);
        } else if (jedisPool != null) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.zrank(key, member);
            } catch (Exception e) {
                logger.error("访问redis失败, zrank：", e);
            }
        }
        return null;
    }

    /**
     * 获取多少排名为start到end之间的member，带分数
     * 递增排列，分数小的排前面，分数大的排后面
     *
     * @param key   ： 在key下
     * @param start ： 排名起始，0作为起始位
     * @param stop  ： 排名截止
     * @return member set
     */
    public Set<Tuple> zrangeWithScores(final String key, long start, long stop) {
        if (jedisCluster != null) {
            return jedisCluster.zrangeWithScores(key, start, stop);
        } else if (jedisPool != null) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.zrangeWithScores(key, start, stop);
            } catch (Exception e) {
                logger.error("访问redis失败, zrangeWithScores：", e);
            }
        }
        return null;
    }

    /**
     * 获取多少排名为start到end之间的member，带分数
     * 递减排列，分数小的排后面，分数大的排前面
     *
     * @param key   ： 在key下
     * @param start ： 排名起始，0作为起始位
     * @param stop  ： 排名截止
     * @return member set
     */
    public Set<Tuple> zrevrangeWithScores(final String key, long start, long stop) {
        if (jedisCluster != null) {
            return jedisCluster.zrevrangeWithScores(key, start, stop);
        } else if (jedisPool != null) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.zrevrangeWithScores(key, start, stop);
            } catch (Exception e) {
                logger.error("访问redis失败, zrevrangeWithScores：", e);
            }
        }
        return null;
    }

    /**
     * 获取多少排名为start到end之间的member
     * 递增排列，分数小的排前面，分数大的排后面
     *
     * @param key   ： 在key下
     * @param start ： 排名起始，0作为起始位
     * @param stop  ： 排名截止
     * @return member set
     */
    public Set<String> zrange(final String key, long start, long stop) {
        if (jedisCluster != null) {
            return jedisCluster.zrange(key, start, stop);
        } else if (jedisPool != null) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.zrange(key, start, stop);
            } catch (Exception e) {
                logger.error("访问redis失败, zrange：", e);
            }
        }
        return null;
    }

    /**
     * 获取多少排名为start到end之间的member
     * 递减排列，分数小的排后面，分数大的排前面
     *
     * @param key   ： 在key下
     * @param start ： 排名起始，0作为起始位
     * @param stop  ： 排名截止
     * @return member set
     */
    public Set<String> zrevrange(final String key, long start, long stop) {
        if (jedisCluster != null) {
            return jedisCluster.zrevrange(key, start, stop);
        } else if (jedisPool != null) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.zrevrange(key, start, stop);
            } catch (Exception e) {
                logger.error("访问redis失败, zrevrange：", e);
            }
        }
        return null;
    }

    /**
     * 有多少member
     *
     * @param key ： 在key下
     * @return 这个人目前的分数
     */
    public Long zcard(final String key) {
        if (jedisCluster != null) {
            return jedisCluster.zcard(key);
        } else if (jedisPool != null) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.zcard(key);
            } catch (Exception e) {
                logger.error("访问redis失败, zcard：", e);
            }
        }
        return null;
    }

    /**
     * 增加分数
     *
     * @param key    ： 在key下
     * @param member ： 这个人
     * @param score  ： 增加多少多分
     * @return 成功数量
     */
    public Double zincrby(final String key, final String member, final Double score) {
        if (jedisCluster != null) {
            return jedisCluster.zincrby(key, score, member);
        } else if (jedisPool != null) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.zincrby(key, score, member);
            } catch (Exception e) {
                logger.error("访问redis失败, zincrby：", e);
            }
        }
        return null;
    }


    /**
     * 设置某人的分数
     *
     * @param key    ： 在key下
     * @param member ： 这个人
     * @param score  ： 这个人设置为多少分
     * @return 成功数量
     */
    public Long zadd(final String key, final String member, final Double score) {
        return zadd(key, new HashMap<String, Double>() {{
            put(member, score);
        }});
    }

    /**
     * 设置某批人的分数
     *
     * @param key          ： 在key下
     * @param scoreMembers ： 这批人的分数map
     * @return 成功数量
     */
    public Long zadd(final String key, final Map<String, Double> scoreMembers) {
        if (jedisCluster != null) {
            return jedisCluster.zadd(key, scoreMembers);
        } else if (jedisPool != null) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.zadd(key, scoreMembers);
            } catch (Exception e) {
                logger.error("访问redis失败, zadd：", e);
            }
        }
        return 0L;
    }


    public RedisProxy setJedisCluster(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
        return this;
    }

    public RedisProxy setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
        return this;
    }

    public boolean needCache() {
        return needCache;
    }

    public RedisProxy setNeedCache(boolean needCache) {
        this.needCache = needCache;
        return this;
    }

    public static void main(String[] args) {
        Set<HostAndPort> haps = new HashSet<>();
        haps.add(new HostAndPort("server2", 7000));
        haps.add(new HostAndPort("server2", 7001));
        haps.add(new HostAndPort("server3", 7000));
        haps.add(new HostAndPort("server3", 7001));
        haps.add(new HostAndPort("server4", 7000));
        haps.add(new HostAndPort("server5", 7001));
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();

        //最大空闲数
        poolConfig.setMaxIdle(200);
        poolConfig.setMinIdle(10);
        poolConfig.setMaxTotal(1000);
        //最大建立连接等待时间
        poolConfig.setMaxWaitMillis(1000);
        //是否在从池中取出连接前进行检验,如果检验失败,则从池中去除连接并尝试取出另一个
        poolConfig.setTestOnBorrow(true);


//        RedisProxy proxy = new RedisProxy()
//                .setJedisCluster(new JedisCluster(haps, 300000, 300000,
//                        5, null, poolConfig));

        RedisProxy proxy = new RedisProxy()
                .setJedisPool(new JedisPool(poolConfig, "192.168.1.61", 7379, 300000, null));

        System.out.println(proxy.set("aa", 11111));
        System.out.println(proxy.get("aa"));

        System.out.println(proxy.lock("aaa"));
        System.out.println(proxy.lock("aaa"));
        System.out.println(proxy.lock("aaa"));
        System.out.println(proxy.get("aaa"));

        System.out.println(proxy.del("aaa"));

    }
}
