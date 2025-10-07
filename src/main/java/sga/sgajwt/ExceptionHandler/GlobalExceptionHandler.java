package sga.sgajwt.ExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> handlerArgumentException(IllegalArgumentException iException){
        
        return new ResponseEntity<String>(iException.getMessage(),HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler
    public ResponseEntity<String> handlerArgumentException(RuntimeException rException){
        
        return new ResponseEntity<String>(rException.getMessage(),HttpStatus.BAD_GATEWAY);

    }
}
