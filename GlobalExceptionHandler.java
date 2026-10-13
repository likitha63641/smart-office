package com.smartoffice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.access.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler {
   
	// Handles 404 - Issue not found
    @ExceptionHandler({IssueNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleIssueNotFound(IssueNotFoundException ex) {
        return new ErrorResponse(404, ex.getMessage());
    }
    
    // Handles 400 - Validation error
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException ex) {
    	
    	 String message = ex.getBindingResult()
    	   .getFieldErrors() 
    	    .get(0)
    	    .getDefaultMessage();
    	 
    	 return new ErrorResponse(400, message);
    }
    
    @ExceptionHandler(EmployeeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEmployeeNotFound(EmployeeNotFoundException ex) {
        return new ErrorResponse(404, ex.getMessage());
    }
    
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalStateException(IllegalStateException ex) {
        return new ErrorResponse(400, ex.getMessage());
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDenied(AccessDeniedException ex) {
        return new ErrorResponse(403, ex.getMessage() == null ? "Access denied" : ex.getMessage());
    }

    @ExceptionHandler(TechnicianNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleTechnicianNotFound(
            TechnicianNotFoundException ex) {

        return new ErrorResponse(404, ex.getMessage());
    }
    
}