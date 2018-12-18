package com.tmt.common.exception;

/**
 * 端口不可用异常
 * 
 * @author lifeng
 */
public class PortUnUseableException extends RuntimeException{

	private static final long serialVersionUID = 1L;

    public PortUnUseableException() {
        super("端口占用！");
    }
}
