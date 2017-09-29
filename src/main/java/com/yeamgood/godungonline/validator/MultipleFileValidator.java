package com.yeamgood.godungonline.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.yeamgood.godungonline.form.DocumentForm;

@Component
public class MultipleFileValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return DocumentForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
    	DocumentForm bucket = (DocumentForm) target;

        if (bucket.getFile() != null && bucket.getFile().isEmpty()){
            errors.rejectValue("file", "file.empty");
        }
    }
}