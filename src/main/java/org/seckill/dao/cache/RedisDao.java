package org.seckill.dao.cache;

import org.seckill.entity.Seckill;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisDao {
	private final JedisPool jedisPool;
	private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

	public RedisDao(String ip, int port) {
		jedisPool = new JedisPool(ip, port);
	}

	public Seckill getSeckill(long seckillId) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String key = "seckill:" + seckillId;
			// �����Զ������л���ʽ protostuff��ʽ��ʵ��
			byte[] bytes = jedis.get(key.getBytes());
			// �����п���ȡ��
			if (bytes != null) {
				Seckill seckill = schema.newMessage();
				ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
				// seckill�������л�
				return seckill;
			}
		} finally {
			jedis.close();
		}
		return null;
	}

	public String pubSeckill(Seckill seckill) {
		// ��һ���������л�Ϊbyte[]
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String key = "seckill:" + seckill.getId();
			byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
			// ��ʱ����
			int timeout = 60 * 60;
			String result = jedis.setex(key.getBytes(), timeout, bytes);
			return result;
		} finally {
			jedis.close();
		}
	}
}
