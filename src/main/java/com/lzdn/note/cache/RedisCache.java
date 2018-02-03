package com.lzdn.note.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

@Component
public class RedisCache {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private JedisPool jedisPool;

}
