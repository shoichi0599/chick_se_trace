package com.chick_se_trace.web.view.login

import javax.validation.constraints.NotBlank

data class LoginForm(
        @get : NotBlank
        var email: String?,

        @get : NotBlank
        var password: String?
) {
    constructor(): this(null, null)
}