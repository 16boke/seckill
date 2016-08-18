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
 * ����spring��junit���ϣ�junit����ʱ����spring ioc����
 * @author wyc
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
//����junit spring�������ļ�
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
	//ע��daoʵ��������
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
