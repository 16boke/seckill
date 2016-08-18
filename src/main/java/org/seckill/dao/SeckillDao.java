package org.seckill.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

public interface SeckillDao {
	// 减库存
	int reduceNumber(@Param("id") long id, @Param("killTime") Date killTime);

	Seckill queryById(long id);

	List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);

	/**
	 * 使用存储过程执行秒杀
	 *
	 * @param paramMap
	 */
	void killByProcedure(Map<String, Object> paramMap);
}
