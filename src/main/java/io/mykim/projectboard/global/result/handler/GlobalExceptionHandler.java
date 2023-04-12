package io.mykim.projectboard.global.result.handler;

import io.mykim.projectboard.global.result.model.CommonResponse;
import io.mykim.projectboard.global.result.model.ValidationError;
import io.mykim.projectboard.global.result.enums.CustomErrorCode;
import io.mykim.projectboard.global.result.exception.BusinessRollbackException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * {
     *     status : 400,
     *     code : "V001",
     *     message : "validation error",
     *     data : [
     *          {
     *              fieldName : "",
     *              errorMessage : "",
     *          }
     *     ]
     * }
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse> validationExceptionHandler(MethodArgumentNotValidException e) {
        log.error("exception = {}", e);
        List<ValidationError> validationErrors = new ArrayList<>();

        if(e.hasErrors()) {
            List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
            validationErrors.addAll(fieldErrors
                                        .stream()
                                        .map(fieldError -> ValidationError.of(fieldError))
                                        .collect(Collectors.toList()));
        }

        return ResponseEntity
                .badRequest()
                .body(new CommonResponse(CustomErrorCode.VALIDATION_ERROR, validationErrors));
    }

    /**
     * @Service Exception : BusinessRollbackException
     * {
     *     status : 400,404,....
     *     code : "",.....
     *     message : "",.....
     *     data : null
     * }
     */
    @ExceptionHandler(BusinessRollbackException.class)
    public ResponseEntity<CommonResponse> businessRollbackExceptionHandler(BusinessRollbackException e) {
        log.error("exception = {}", e);
        CommonResponse commonResponse = new CommonResponse(e.getErrorCode());
        return ResponseEntity
                .status(commonResponse.getStatus())
                .body(commonResponse);
    }

}
