package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
// 告诉junit spring的配置文件
@ContextConfiguration({ "classpath:spring/spring-dao.xml" })
public class SuccessKilledDaoTest {
	@Autowired
	private SuccessKilledDao successKilledDao;

	@Test
	public void testInsertSuccessKilled() {
		try {
			System.out.println(successKilledDao.insertSuccessKilled(1000L, 13500000000L));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public void testQueryByIdWithSeckill() {
		SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(1000L, 13500000000L);
		System.out.println(successKilled);
	}

}
