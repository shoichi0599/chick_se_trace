package com.chick_se_trace.web.view

import java.io.Serializable

/**
 * Class to store a session info.
 *
 * @author shoichi.sato
 */
data class ChickSeSession(
        val userName: String?
): Serializable {
    companion object {
        const val SESSION_ATTRIBUTE_NAME: String = "chickSeSession"
    }
}