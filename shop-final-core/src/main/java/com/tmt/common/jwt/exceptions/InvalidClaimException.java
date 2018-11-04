package com.tmt.common.jwt.exceptions;


public class InvalidClaimException extends JWTVerificationException {
	private static final long serialVersionUID = 1L;

	public InvalidClaimException(String message) {
        super(message);
    }
}
