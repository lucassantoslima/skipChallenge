package com.skipthedishes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( HttpStatus.BAD_REQUEST)
public class DuplicateResourceException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public DuplicateResourceException() {
	}

	public DuplicateResourceException(final String message) {
		super(message);
	}
}
