package org.seckill.exception;

/**
 * �ظ���ɱ�쳣���������쳣��
 * 
 * @author wyc
 *
 */
public class RepeatKillException extends SeckillException {

	private static final long serialVersionUID = 1L;

	public RepeatKillException(String message, Throwable cause) {
		super(message, cause);
	}

	public RepeatKillException(String message) {
		super(message);
	}

}
