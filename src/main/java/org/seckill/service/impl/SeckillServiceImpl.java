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

	// ����,���ڻ���md5
	private final String slat = "sfwerwqr234#$%$&@2141ewr324WERSFD~!!#@$%^&*(";

	public List<Seckill> getSeckillList() {
		return seckillDao.queryAll(0, 10);
	}

	public Seckill getById(long seckillId) {
		return seckillDao.queryById(seckillId);
	}

	public Exposer exportSeckillUrl(long seckillId) {
		// �����Ż�
		Seckill seckill = redisDao.getSeckill(seckillId);
		if (seckill == null) {
			seckill = this.seckillDao.queryById(seckillId);
			if (seckill == null) {
				return new Exposer(false, seckillId);
			}
			// ������ھͷ��뵽redis��
			redisDao.pubSeckill(seckill);
		}

		Date startTime = seckill.getStartTime();
		Date endTime = seckill.getEndTime();
		// ϵͳ��ǰʱ��
		Date nowTime = new Date();
		if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime())
			return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());

		// ת���ض��ַ�����������
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
	 * ʹ��ע��������񷽷����ŵ㣺 1�������ŶӴ��һ��Լ������ȷ��ע���񷽷��ı�̷�� 2����֤���񷽷�ִ�е�ʱ�価���̣ܶ���Ҫ���������������
	 */
	public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
			throws SeckillException, SeckillCloseException, RepeatKillException {
		if (md5 == null || !md5.equals(getMd5(seckillId))) {
			throw new SeckillException("seckill data rewrite");
		}
		// ִ����ɱ�߼��������+��¼������Ϊ
		Date nowTime = new Date();
		try {
			//�Ȳ��빻����Ϊ��ֻ�гɹ��Ž��м����
			int insertCount = successKillDao.insertSuccessKilled(seckillId, userPhone);
			if (insertCount <= 0) {
				throw new RepeatKillException("seckill repeated");
			} else {
				// �����
				int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
				// û�и��µ���¼����,��ɱ����
				if (updateCount <= 0) {
					throw new SeckillCloseException("seckill is closed");
				} else {
					// ��¼������Ϊ
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
