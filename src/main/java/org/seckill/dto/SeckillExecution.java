package org.seckill.dto;
/**
 * ��װ��ɱִ�к���
 * @author wyc
 *
 */

import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;

public class SeckillExecution {
	private long seckillId;
	// ��ɱִ�н��״̬
	private int state;
	// ״̬����
	private String statInfo;
	private SuccessKilled successKilled;

	public SeckillExecution() {
		super();
	}

	public SeckillExecution(long seckillId, SeckillStatEnum seckillStatEnum, SuccessKilled successKilled) {
		super();
		this.seckillId = seckillId;
		this.state = seckillStatEnum.getState();
		this.statInfo = seckillStatEnum.getStateInfo();
		this.successKilled = successKilled;
	}

	public SeckillExecution(long seckillId, SeckillStatEnum seckillStatEnum) {
		super();
		this.seckillId = seckillId;
		this.state = seckillStatEnum.getState();
		this.statInfo = seckillStatEnum.getStateInfo();
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStatInfo() {
		return statInfo;
	}

	public void setStatInfo(String statInfo) {
		this.statInfo = statInfo;
	}

	public SuccessKilled getSuccessKilled() {
		return successKilled;
	}

	public void setSuccessKilled(SuccessKilled successKilled) {
		this.successKilled = successKilled;
	}

	public long getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(long seckillId) {
		this.seckillId = seckillId;
	}

	@Override
	public String toString() {
		return "SeckillExecution [seckillId=" + seckillId + ", state=" + state + ", statInfo=" + statInfo + ", successKilled="
				+ successKilled + "]";
	}

}
