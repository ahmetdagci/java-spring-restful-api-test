package uk.co.huntersix.spring.rest.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class DuplicateResourceException extends Exception {
	private static final long serialVersionUID = 1L;

	public DuplicateResourceException(String message) {
		super(message);
	}
}