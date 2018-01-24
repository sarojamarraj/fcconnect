package com.freightcom.api;

import java.lang.reflect.UndeclaredThrowableException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.NestedServletException;

import com.bugsnag.Bugsnag;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.freightcom.api.services.ValidationException;
import com.freightcom.api.util.MapBuilder;

@ControllerAdvice
@RestController
public class GlobalExceptionHandlers extends ResponseEntityExceptionHandler
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("mappingJackson2HttpMessageConverter")
    private MappingJackson2HttpMessageConverter messageConverter;

    @Autowired
    private Bugsnag bugsnag;

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
            HttpStatus status, WebRequest request)
    {
        logError(ex);

        if (body == null) {
            body = "{ \"message\": \"Internal Error\" }";
        }

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
            String body = "{ \"message\": \"" + ex.getMessage() + "\" }";

            return handleExceptionInternal(ex, body, headers, status, request);
	}

    protected ResponseEntity<Object> handleReportable(Exception ex, Object body, HttpHeaders headers, HttpStatus status,
            WebRequest request)
    {
        logException(ex);

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    @ExceptionHandler(value = { NestedServletException.class })
    protected ResponseEntity<Object> handleReportable(NestedServletException exception, WebRequest request)
    {
        Throwable cause = exception.getRootCause();

        if (cause instanceof ReportableError) {
            return handleReportable((ReportableError) cause, request);

        } else if (cause instanceof ConstraintViolationException) {
            return handleConstraint((ConstraintViolationException) cause, request);

        } else if (cause instanceof SessionExpiredException) {
            return handleExpired((SessionExpiredException) cause, request);

        } else if (cause instanceof ValidationException) {
            return handleValidation((ValidationException) cause, request);

        } else if (cause instanceof AccessDeniedException) {
            return handleAccessDenied((AccessDeniedException) cause, request);

        } else if (cause instanceof ResourceNotFoundException) {
            return handleResourceNotFound((ResourceNotFoundException) cause, request);

        } else {
            String bodyOfResponse = "{ \"message\": \"Internal Error\" }";
            HttpHeaders headers = new HttpHeaders();

            logError(exception);

            headers.setContentType(MediaType.APPLICATION_JSON);

            return handleExceptionInternal(exception, bodyOfResponse, headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
        }
    }

    @ExceptionHandler(value = { ReportableError.class })
    protected ResponseEntity<Object> handleReportable(ReportableError exception, WebRequest request)
    {
        String bodyOfResponse = "";

        try {
            bodyOfResponse = messageConverter.getObjectMapper()
                    .writeValueAsString(MapBuilder.getNew("message", exception.getMessage()).toMap());
        } catch (JsonProcessingException e) {
            bodyOfResponse = "{ \"validation\": \"problem\" }";
            logError(e);
        }

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        // Turn into an exception since handleExceptionInternal doesn't handle
        // throwable.

        return handleReportable(new Exception(exception.getMessage()), bodyOfResponse, headers, HttpStatus.BAD_REQUEST,
                request);
    }

    @ExceptionHandler(value = { ConstraintViolationException.class })
    protected ResponseEntity<Object> handleConstraint(ConstraintViolationException exception, WebRequest request)
    {
        String bodyOfResponse = "";

        try {
            List<String> errors = new ArrayList<String>();

            for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
                errors.add(violation.getPropertyPath() + ": " + violation.getMessage());
            }

            log.debug("ERRORS " + errors.size());
            log.debug(errors.toString());

            bodyOfResponse = messageConverter.getObjectMapper()
                    .writeValueAsString(errors);
        } catch (JsonProcessingException e) {
            bodyOfResponse = "{ \"validation\": \"problem\" }";
            logError(e);
        }

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        // Turn into an exception since handleExceptionInternal doesn't handle
        // throwable.

        return handleReportable(new Exception(exception.getMessage()), bodyOfResponse, headers, HttpStatus.BAD_REQUEST,
                request);
    }

    protected Map<String, String> getMap(String key, String value)
    {
        Map<String, String> map = new HashMap<String, String>(1);

        map.put(key, value);

        return map;
    }

    protected String jsonResponse(String key, String value)
    {
        String response = null;

        try {
            response = messageConverter.getObjectMapper()
                    .writeValueAsString(getMap(key, value));
        } catch (JsonProcessingException e) {
            response = "{ \"jsonproblem\": \"problem\" }";
            logError(e);
        }

        return response;
    }

    @ExceptionHandler(value = { SessionExpiredException.class })
    protected ResponseEntity<Object> handleExpired(SessionExpiredException exception, WebRequest request)
    {
        String bodyOfResponse = "{ \"message\": \"Session expired\", \"event\": \"session-expired\" }";
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleReportable(exception, bodyOfResponse, headers, HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(value = { AccessDeniedException.class })
    protected ResponseEntity<Object> handleAccessDenied(AccessDeniedException exception, WebRequest request)
    {
        String bodyOfResponse = "{ \"message\": \"Access is denied\" }";
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleReportable(exception, bodyOfResponse, headers, HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(value = { ValidationException.class })
    protected ResponseEntity<Object> handleValidation(ValidationException exception, WebRequest request)
    {
        String bodyOfResponse;

        try {
            bodyOfResponse = messageConverter.getObjectMapper()
                    .writeValueAsString(exception.getErrors());
        } catch (JsonProcessingException e) {
            bodyOfResponse = "{ \"validation\": \"problem\" }";
            logError(e);
        }

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleReportable(exception, bodyOfResponse, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { InvalidFormatException.class })
    protected ResponseEntity<Object> handleONe(InvalidFormatException exception, WebRequest request)
    {
        logException(exception);

        String bodyOfResponse = "{ \"message\": \"" + exception.getMessage() + "\" }";
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleReportable(exception, bodyOfResponse, headers, HttpStatus.BAD_REQUEST, request);
    }

    // HttpMessageNotReadableException

    @ExceptionHandler(value = { NoRoleSelectedException.class })
    protected ResponseEntity<Object> handleNoRole(NoRoleSelectedException exception, WebRequest request)
    {
        logException(exception);

        String bodyOfResponse = "{ \"message\": \"No role selected\" }";
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleReportable(exception, bodyOfResponse, headers, HttpStatus.UNAUTHORIZED, request);
    }

    /**
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(value = { ResourceNotFoundException.class })
    protected ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException exception, WebRequest request)
    {

        String bodyOfResponse = "{ \"message\": \"" + exception.getMessage() + "\" }";
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleReportable(exception, bodyOfResponse, headers, HttpStatus.UNAUTHORIZED, request);
    }

    /**
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(value = { ClassCastException.class })
    protected ResponseEntity<Object> handleClassCastException(ClassCastException exception, WebRequest request)
    {

        logError(exception);

        String bodyOfResponse = "{ \"message\": \"" + exception.getMessage() + "\" }";
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(exception, bodyOfResponse, headers, HttpStatus.UNAUTHORIZED, request);
    }

    /**
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(value = { UndeclaredThrowableException.class })
    protected ResponseEntity<Object> undeclared(UndeclaredThrowableException exception, WebRequest request)
    {
        log.debug("UNDECLARED THROWABLE " + exception.getCause());
        String bodyOfResponse = "{ \"message\": \"Internal Error\" }";
        HttpHeaders headers = new HttpHeaders();

        logError(exception);

        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(exception, bodyOfResponse, headers, HttpStatus.UNAUTHORIZED, request);
    }

    /**
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(value = { NullPointerException.class })
    protected ResponseEntity<Object> handleConflict(NullPointerException exception, WebRequest request)
    {
        String bodyOfResponse = "{ \"message\": \"Internal Error\" }";
        HttpHeaders headers = new HttpHeaders();

        logError(exception);

        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(exception, bodyOfResponse, headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception,
            HttpHeaders existingHeaders,
            HttpStatus status,
            WebRequest request)
    {
        String message = exception.getMessage();
        int n = message.indexOf("\n");

        if (n > 0) {
            message = message.substring(0, n);
        }

        String bodyOfResponse = "{ \"message\": \"" + message.replace("\"", "'") + "\" }";
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleReportable(exception, bodyOfResponse, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    /**
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(value = { JsonMappingException.class })
    protected ResponseEntity<Object> handleJsonMapping(JsonMappingException exception, WebRequest request)
    {
        String bodyOfResponse = "{ \"message\": \"Internal Error\" }";
        HttpHeaders headers = new HttpHeaders();

        logError(exception);

        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(exception, bodyOfResponse, headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(value = { MultipartException.class })
    protected ResponseEntity<Object> handleIllegalState(MultipartException exception, WebRequest request)
    {
        String bodyOfResponse = "{ \"message\": \"Internal Error\" }";
        HttpHeaders headers = new HttpHeaders();
        Exception realException = exception;

        if (exception.getCause() instanceof Exception) {
            realException = (Exception) exception.getCause();
        }

        log.debug("MULTIPART STATE EXCEPTION");

        logError(realException);

        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleReportable(realException, bodyOfResponse, headers, HttpStatus.UNAUTHORIZED, request);
    }

    /**
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<Object> handleConflict(Exception exception, WebRequest request)
    {
        Map<String,String> bodyOfResponse = new HashMap<String,String>();

        HttpHeaders headers = new HttpHeaders();
        Exception realException = exception;

        if (exception.getCause() instanceof Exception) {
            realException = (Exception) exception.getCause();
        }

        bodyOfResponse.put("message", realException.getMessage());

        logError(realException);

        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleReportable(realException, bodyOfResponse, headers, HttpStatus.BAD_REQUEST, request);
    }

    /**
     * @param exception
     */
    private void logException(Throwable exception)
    {
        log.info(Instant.now().toString() + "\nFRAMEWORK EXCEPTION " + exception.getMessage());
        log.debug(buildStackTrace(exception, null));
    }

    /**
     * @param exception
     */
    private void logError(Throwable exception)
    {
        if (bugsnag != null) {
            bugsnag.notify(exception);
        }

        log.info(Instant.now().toString() + "\nFRAMEWORK ERROR " + exception.getMessage());
        log.info(buildStackTrace(exception, null));
    }

    private String buildStackTrace(Throwable exception, StringBuilder buffer)
    {
        if (buffer == null) {
            buffer = new StringBuilder();
            buffer.append("\n\n");
        }

        buffer.append("FRAMEWORK EXCEPTION " + exception.getMessage());
        buffer.append("\n");
        buffer.append(exception.toString());
        buffer.append("\n");
        buffer.append("Stack Trace\n");

        for (StackTraceElement element : exception.getStackTrace()) {
            buffer.append("-- ")
                    .append(element.toString())
                    .append("\n");
        }

        if (exception.getCause() != null) {
            buffer.append("Caused By\n");
            buildStackTrace(exception.getCause(), buffer);
        }

        return buffer.toString();
    }
}
