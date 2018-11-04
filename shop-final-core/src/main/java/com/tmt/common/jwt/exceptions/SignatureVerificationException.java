package com.tmt.common.jwt.exceptions;

import com.tmt.common.jwt.algorithms.Algorithm;

public class SignatureVerificationException extends JWTVerificationException {
	private static final long serialVersionUID = 1L;

	public SignatureVerificationException(Algorithm algorithm) {
        this(algorithm, null);
    }

    public SignatureVerificationException(Algorithm algorithm, Throwable cause) {
        super("The Token's Signature resulted invalid when verified using the Algorithm: " + algorithm, cause);
    }
}
