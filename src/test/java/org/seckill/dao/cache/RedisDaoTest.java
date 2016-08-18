package org.seckill.dao.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SeckillDao;
import org.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
// ����junit spring�������ļ�
@ContextConfiguration({ "classpath:spring/spring-dao.xml" })
public class RedisDaoTest {
	@Autowired
	private RedisDao redisDao;
	private long id = 1002;
	@Autowired
	private SeckillDao seckillDao;

	@Test
	public void testGetSeckill() {
		Seckill seckill = redisDao.getSeckill(id);
		System.out.println("1 = "+seckill);
		if (seckill == null) {
			seckill = this.seckillDao.queryById(id);
			System.out.println("2 = "+seckill);
			if (seckill != null) {
				String result = this.redisDao.pubSeckill(seckill);
				System.out.println("3 = "+result);
				seckill = redisDao.getSeckill(id);
				System.out.println("4 = "+seckill);
			}
		}
	}

	@Test
	public void testPubSeckill() {

	}

}
