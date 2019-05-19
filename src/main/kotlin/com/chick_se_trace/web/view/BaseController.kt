package com.chick_se_trace.web.view

import org.apache.commons.lang3.exception.ExceptionUtils
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Base Controller for all screens.
 *
 * @author shoichi.sato
 */
@Controller
class BaseController {

    private val logger = LoggerFactory.getLogger(BaseController::class.java)

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(e: Exception): String {
        logger.error(ExceptionUtils.getStackTrace(e))
        return "errors/500_internal_server_error"
    }
}