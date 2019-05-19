package com.chick_se_trace.web.view.login

import com.chick_se_trace.domain.UserRepository
import com.chick_se_trace.web.view.BaseController
import com.chick_se_trace.web.view.ChickSeSession
import org.apache.commons.codec.digest.DigestUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import javax.servlet.http.HttpServletRequest

/**
 * Controller for login screen.
 *
 * @author shoichi.sato
 */
@Controller
class LoginController(
        @Autowired private val userRepository: UserRepository
) : BaseController() {

    private val logger = LoggerFactory.getLogger(LoginController::class.java)

    /* ------------------------------------------------------------------------
     * Sets up form used on the screen
     * --------------------------------------------------------------------- */
    @ModelAttribute("loginForm")
    fun setUpLoginForm(): LoginForm {
        return LoginForm()
    }

    /**
     * Shows the login screen.
     */
    @GetMapping("login")
    fun show(): String {
        return "login"
    }

    /**
     * Executes the login process.
     * Creates and sets a new session.
     */
    @PostMapping("login/go")
    fun login(form: LoginForm,
              bindingResult: BindingResult,
              httpServletRequest: HttpServletRequest
    ): String {
        if (bindingResult.hasErrors()) {
            return "login"
        }

        // Conduct authentication
        val user = userRepository.findByEmail(form.email!!)
        if (user == null) {
            logger.info("user is not found by email=${form.email}")
            return "login"
        }
        if (user.password != DigestUtils.sha256Hex(form.password!!)) {
            logger.info("password is invalid. password=${form.password}")
            return "login"
        }

        // Set session
        httpServletRequest.getSession().setAttribute(ChickSeSession.SESSION_ATTRIBUTE_NAME, ChickSeSession(user.name))
        httpServletRequest.changeSessionId()

        return "redirect:/"
    }
}