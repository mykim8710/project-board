package io.mykim.projectboard.global.result.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.FieldError;

@Getter
public class ValidationError {
    private String fieldName;
    private String errorMessage;

    @Builder
    private ValidationError(FieldError fieldError) {
        this.fieldName = fieldError.getField();
        this.errorMessage = fieldError.getDefaultMessage();
    }

    public static ValidationError of(FieldError fieldError) {
        return new ValidationError(fieldError);
    }
}
