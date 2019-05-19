package com.chick_se_trace.web.view.item_create

import java.io.Serializable
import javax.validation.constraints.NotBlank

/**
 * Form for item create.
 */
data class CreateForm(
        @get: NotBlank
        var title: String?,

        @get: NotBlank
        var categoryName: String?,

        @get: NotBlank
        var content: String?
): Serializable {
    constructor(): this(null, null, null)
}