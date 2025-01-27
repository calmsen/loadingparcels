package ru.calmsen.loadingparcels.advice;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.ErrorResponse;
import ru.calmsen.loadingparcels.exception.BusinessException;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler({
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class,
            MissingPathVariableException.class,
            MissingServletRequestParameterException.class,
            MissingServletRequestPartException.class,
            ServletRequestBindingException.class,
            MethodArgumentNotValidException.class,
            HandlerMethodValidationException.class,
            NoHandlerFoundException.class,
            NoResourceFoundException.class,
            ErrorResponseException.class,
            MaxUploadSizeExceededException.class})
    public ResponseEntity<Object> handleStatusException(Exception ex) {
        var statusCode = ex instanceof ErrorResponse subEx ? subEx.getStatusCode() : HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(statusCode).body(
                toError(ex, statusCode)
        );
    }

    @ExceptionHandler({TypeMismatchException.class, HttpMessageNotReadableException.class, BindException.class})
    public ResponseEntity<Object> handleBadRequestException(Exception ex) {
        return ResponseEntity.badRequest().body(
                toError(ex, HttpStatus.BAD_REQUEST)
        );
    }

    @ExceptionHandler(value = {BusinessException.class})
    public ResponseEntity<Object> handleBusinessException(BusinessException ex) {
        return ResponseEntity.unprocessableEntity().body(
                toError(ex, HttpStatus.UNPROCESSABLE_ENTITY)
        );
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleRuntimeException(Exception ex) {
        return ResponseEntity.internalServerError().body(
                toError(ex, HttpStatus.INTERNAL_SERVER_ERROR)
        );
    }

    private ProblemDetail toError(Exception ex, HttpStatusCode statusCode) {
        return ErrorResponse.builder(ex, statusCode, ex.getMessage()).build().getBody();
    }
}