package org.seckill.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

public interface SeckillDao {
	// ¼õ¿â´æ
	int reduceNumber(@Param("id") long id, @Param("killTime") Date killTime);

	Seckill queryById(long id);

	List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);
}
