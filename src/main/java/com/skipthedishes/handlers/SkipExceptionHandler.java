package com.skipthedishes.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.skipthedishes.dto.ErrorDto;
import com.skipthedishes.exceptions.DuplicateResourceException;
import com.skipthedishes.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class SkipExceptionHandler extends ResponseEntityExceptionHandler {

	@InitBinder
	public void initBinder(final WebDataBinder binder) {
		StringTrimmerEditor stringtrimmer = new StringTrimmerEditor(true);
		binder.registerCustomEditor(String.class, stringtrimmer);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundException(final ResourceNotFoundException resourceNotFoundException, WebRequest request) {
		String userMsg = resourceNotFoundException.getMessage();
		String developerMsg = resourceNotFoundException.getCause() != null ? resourceNotFoundException.getCause().toString() : resourceNotFoundException.toString();
		List<ErrorDto> erros = Arrays.asList(new ErrorDto(userMsg, developerMsg));
		return handleExceptionInternal(resourceNotFoundException, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	} 

	@ExceptionHandler(DuplicateResourceException.class)
	public ResponseEntity<?> handleDuplicateResourceException(final DuplicateResourceException duplicateRecordException, WebRequest request) {
		String userMsg = duplicateRecordException.getMessage();
		String developerMsg = duplicateRecordException.getCause() != null ? duplicateRecordException.getCause().toString() : duplicateRecordException.toString();
		List<ErrorDto> erros = Arrays.asList(new ErrorDto(userMsg, developerMsg));
		return handleExceptionInternal(duplicateRecordException, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request); 
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		String userMsg = ex.getMessage(); 
		String developerMsg = ex.getCause() != null ? ex.getCause().toString() : ex.toString();
		List<ErrorDto> erros = Arrays.asList(new ErrorDto(userMsg, developerMsg));
		return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<ErrorDto> erros = createListOfError(ex.getBindingResult());
		return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler({ EmptyResultDataAccessException.class })
	public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex,WebRequest request) {
		String userMsg = ex.getMessage(); 
		String developerMsg = ex.getCause() != null ? ex.getCause().toString() : ex.toString();
		List<ErrorDto> erros = Arrays.asList(new ErrorDto(userMsg, developerMsg));
		return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	private List<ErrorDto> createListOfError(BindingResult bindingResult) {
		List<ErrorDto> erros = new ArrayList<ErrorDto>();
		
		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			String userMsg = fieldError.getDefaultMessage();
			String developerMsg = fieldError.toString();
			erros.add(new ErrorDto(userMsg, developerMsg));
		}
			
		return erros;
	}
}
