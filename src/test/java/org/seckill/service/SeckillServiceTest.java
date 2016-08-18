package org.seckill.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.SeckillException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
// 告诉junit spring的配置文件
@ContextConfiguration({ "classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml" })
public class SeckillServiceTest {
	@Autowired
	private SeckillService seckillService;
	private Logger logger = Logger.getLogger(SeckillServiceTest.class);

	@Test
	public void testGetSeckillList() {
		List<Seckill> list = seckillService.getSeckillList();
		logger.info(list);
	}

	@Test
	public void testGetById() {
		long seckillId = 1000l;
		Seckill seckill = seckillService.getById(seckillId);
		logger.info(seckill);
	}

	@Test
	public void testExportSeckillUrl() {
		long seckillId = 1000l;
		Exposer exposer = seckillService.exportSeckillUrl(seckillId);
		logger.info(exposer);
	}

	@Test
	public void testExecuteSeckill() {
		long seckillId = 1000l;
		long phone = 13800000001l;
		String md5="0a423bf10082f23b359abb96eff4be0e";
		try{
		SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId, phone, md5);
			logger.info(seckillExecution);
		}catch(SeckillException e){
			e.printStackTrace();
		}
	}

}
