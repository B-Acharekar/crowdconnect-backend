package com.crowdconnect.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN) // Returns 403 Forbidden status
public class UnauthorizedException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnauthorizedException(String message) {
        super(message);
    }
}
