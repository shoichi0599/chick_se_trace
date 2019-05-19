package com.chick_se_trace.web.view.item_create

import com.chick_se_trace.domain.Item
import com.chick_se_trace.domain.ItemRepository
import com.chick_se_trace.web.view.BaseController
import com.chick_se_trace.web.view.ChickSeSession
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.SessionAttribute
import javax.validation.Valid

/**
 * Controller for item create screen.
 *
 * @author shoichi.sato
 */
@Controller
class ItemCreateController(
        @Autowired private var itemRepository: ItemRepository
): BaseController() {

    private val logger = LoggerFactory.getLogger(ItemCreateController::class.java)

    /* ------------------------------------------------------------------------
     * Sets up form used on the screen
     * --------------------------------------------------------------------- */
    @ModelAttribute("createForm")
    fun setUpCreateForm(): CreateForm {
        return CreateForm()
    }

    /**
     * Shows the item create screen.
     */
    @GetMapping("item/create")
    fun show(
            @SessionAttribute(ChickSeSession.SESSION_ATTRIBUTE_NAME) chickSeSession: ChickSeSession?
    ): String {
        if (chickSeSession == null) {
            logger.info("unauthorized request. user has not logged in yet.")
            return "errors/401_unauthorized"
        }
        return "item_create"
    }

    /**
     * Creates a new item.
     */
    @PostMapping("item/createNew")
    fun create(
            @Valid form: CreateForm,
            bindingResult: BindingResult,
            @SessionAttribute(ChickSeSession.SESSION_ATTRIBUTE_NAME) chickSeSession: ChickSeSession?
    ): String {
        if (chickSeSession == null) {
            logger.info("unauthorized request. user has not logged in yet.")
            return "errors/401_unauthorized"
        }

        // Validation result
        if (bindingResult.hasErrors()) {
            return "item_create"
        }

        // Create a new item
        val itemId = this.itemRepository.create(
                item = Item(id = null,
                            title = form.title!!,
                            content = form.content!!,
                            createTime = null,
                            updateTime = null),
                categoryName = form.categoryName!!
        )

        // Redirect item screen
        return "redirect:/item/$itemId"
    }
}