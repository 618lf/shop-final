package com.tmt.core.jwt.exceptions;

import com.tmt.core.jwt.algorithms.Algorithm;

public class SignatureGenerationException extends JWTCreationException {
	private static final long serialVersionUID = 1L;

	public SignatureGenerationException(Algorithm algorithm, Throwable cause) {
        super("The Token's Signature couldn't be generated when signing using the Algorithm: " + algorithm, cause);
    }
}