package org.seckill.dao;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
/**
 * 配置spring和junit整合，junit启动时加载spring ioc容器
 * @author wyc
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
	//注入dao实现类依赖
	@Autowired
	private SeckillDao seckillDao;
	
	@Test
	public void testQueryById() {
		long id = 1000;
		Seckill seckill = seckillDao.queryById(id);
		System.out.println(seckill.getName());
		System.out.println(seckill);
	}

	@Test
	public void testQueryAll() {
		List<Seckill>list = seckillDao.queryAll(0, 10);
		System.out.println(list);
	}
	
	@Test
	public void testReduceNumber() {
		long id = 1000;
		Date killTime = new Date();
		System.out.println(seckillDao.reduceNumber(id, killTime));
	}

}
