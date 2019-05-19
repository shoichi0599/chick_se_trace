package com.chick_se_trace.web.view.Item_update

import java.io.Serializable
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

/**
 * Form for item update.
 */
data class UpdateForm(
        @get: NotBlank
        @get: Pattern(regexp = "[1-9]+")
        var itemId: String?,

        @get: NotBlank
        var title: String?,

        @get: NotBlank
        var categoryName: String?,

        @get: NotBlank
        var content: String?
): Serializable {
    constructor(): this(null, null, null, null)
}

/**
 * Form for item delete.
 */
data class DeleteForm(
        @get: NotBlank
        @get: Pattern(regexp = "[1-9]+")
        var itemId: String?
): Serializable {
    constructor(): this(null)
}