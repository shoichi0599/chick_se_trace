package com.chick_se_trace.base.validation

interface Validatable {

    fun validateAndGetViolations(): List<String> {
        return AnnotationValidator().validateAndGetViolations(this)
    }
}