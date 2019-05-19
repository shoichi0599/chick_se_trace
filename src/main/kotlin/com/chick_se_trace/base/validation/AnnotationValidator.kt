package com.chick_se_trace.base.validation

import java.util.*
import javax.validation.ConstraintViolation
import javax.validation.Validation
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

class AnnotationValidator {
    private val validator = Validation.buildDefaultValidatorFactory().validator

    fun validateAndGetViolations(any: Any): List<String> {
        val results = validator.validate(any)
        return results.map { it.message }
    }
}