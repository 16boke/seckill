package org.seckill.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dao.cache.RedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

@Service
public class SeckillServiceImpl implements SeckillService {
	private Logger logger = Logger.getLogger(SeckillServiceImpl.class);
	@Autowired
	private SeckillDao seckillDao;
	@Autowired
	private SuccessKilledDao successKillDao;
	@Autowired
	private RedisDao redisDao;

	// 加盐,用于混淆md5
	private final String slat = "sfwerwqr234#$%$&@2141ewr324WERSFD~!!#@$%^&*(";

	public List<Seckill> getSeckillList() {
		return seckillDao.queryAll(0, 10);
	}

	public Seckill getById(long seckillId) {
		return seckillDao.queryById(seckillId);
	}

	public Exposer exportSeckillUrl(long seckillId) {
		// 缓存优化
		Seckill seckill = redisDao.getSeckill(seckillId);
		if (seckill == null) {
			seckill = this.seckillDao.queryById(seckillId);
			if (seckill == null) {
				return new Exposer(false, seckillId);
			}
			// 如果存在就放入到redis中
			redisDao.pubSeckill(seckill);
		}

		Date startTime = seckill.getStartTime();
		Date endTime = seckill.getEndTime();
		// 系统当前时间
		Date nowTime = new Date();
		if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime())
			return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());

		// 转代特定字符串，不可逆
		String md5 = getMd5(seckillId);
		return new Exposer(true, md5, seckillId);
	}

	private String getMd5(long seckillId) {
		String base = seckillId + "/" + slat;
		String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
		return md5;
	}

	@Transactional(rollbackFor = Exception.class)
	/**
	 * 使用注解控制事务方法的优点： 1、开发团队达成一致约定，明确标注事务方法的编程风格 2、保证事务方法执行的时间尽可能短，不要有其他的网络操作
	 */
	public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
			throws SeckillException, SeckillCloseException, RepeatKillException {
		if (md5 == null || !md5.equals(getMd5(seckillId))) {
			throw new SeckillException("seckill data rewrite");
		}
		// 执行秒杀逻辑：减库存+记录购买行为
		Date nowTime = new Date();
		try {
			//先插入够买行为，只有成功才进行减库存
			int insertCount = successKillDao.insertSuccessKilled(seckillId, userPhone);
			if (insertCount <= 0) {
				throw new RepeatKillException("seckill repeated");
			} else {
				// 减库存
				int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
				// 没有更新到记录操作,秒杀结束
				if (updateCount <= 0) {
					throw new SeckillCloseException("seckill is closed");
				} else {
					// 记录购买行为
					SuccessKilled successKilled = successKillDao.queryByIdWithSeckill(seckillId, userPhone);
					return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
				}
			}
		} catch (SeckillException e) {
			throw e;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new SeckillException("seckill inner error:" + ex.getMessage());
		}
	}

}
