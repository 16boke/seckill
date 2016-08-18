package org.seckill.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

public interface SeckillDao {
	// �����
	int reduceNumber(@Param("id") long id, @Param("killTime") Date killTime);

	Seckill queryById(long id);

	List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);

	/**
	 * ʹ�ô洢����ִ����ɱ
	 *
	 * @param paramMap
	 */
	void killByProcedure(Map<String, Object> paramMap);
}
