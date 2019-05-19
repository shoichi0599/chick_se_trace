package com.chick_se_trace.web.view.logout

import com.chick_se_trace.web.view.BaseController
import com.chick_se_trace.web.view.ChickSeSession
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.SessionAttribute
import javax.servlet.http.HttpSession

/**
 * Controller for logout screen.
 *
 * @author shoichi.sato
 */
@Controller
class LogoutController: BaseController() {

    private val logger = LoggerFactory.getLogger(LogoutController::class.java)

    /**
     * Executes the logout process.
     * Invalidates the existing session.
     */
    @GetMapping("logout")
    fun logout(
            @SessionAttribute(ChickSeSession.SESSION_ATTRIBUTE_NAME) chickSeSession: ChickSeSession?,
            httpSession: HttpSession
    ): String {
        if (chickSeSession == null) {
            logger.info("unauthorized request. user has not logged in yet.")
            return "errors/401_unauthorized"
        }
        httpSession.invalidate()
        return "logout"
    }
}