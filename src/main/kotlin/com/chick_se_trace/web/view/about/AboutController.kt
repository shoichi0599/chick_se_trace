package com.chick_se_trace.web.view.about

import com.chick_se_trace.web.view.BaseController
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

/**
 * Controller for about screen.
 *
 * @author shoichi.sato
 */
@Controller
class AboutController : BaseController() {

    /**
     * Shows the about screen.
     */
    @GetMapping("about")
    fun show(): String {
        return "about"
    }
}