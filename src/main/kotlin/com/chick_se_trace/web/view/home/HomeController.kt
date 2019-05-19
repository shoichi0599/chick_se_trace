package com.chick_se_trace.web.view.home

import com.chick_se_trace.web.view.BaseController
import com.chick_se_trace.web.view.ChickSeSession
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.SessionAttribute

/**
 * Controller for home screen.
 *
 * @author shoichi.sato
 */
@Controller
class HomeController : BaseController() {

    /**
     * Shows the home screen.
     */
    @GetMapping("/", "home")
    fun show(): String {
        return "home"
    }
}