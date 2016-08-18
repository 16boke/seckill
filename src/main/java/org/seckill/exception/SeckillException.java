package org.seckill.exception;

/**
 * 秒杀相关的业务异常
 * 
 * @author wyc
 *
 */
public class SeckillException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SeckillException(String message, Throwable cause) {
		super(message, cause);
	}

	public SeckillException(String message) {
		super(message);
	}

}
