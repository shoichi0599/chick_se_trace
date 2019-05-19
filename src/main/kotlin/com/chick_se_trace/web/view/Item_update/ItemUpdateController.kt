package com.chick_se_trace.web.view.Item_update

import com.chick_se_trace.domain.Category
import com.chick_se_trace.domain.CategoryRepository
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
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.SessionAttribute
import org.springframework.web.servlet.ModelAndView

/**
 * Controller for item update screen.
 *
 * @author shoichi.sato
 */
@Controller
class ItemUpdateController(
        @Autowired private var itemRepository: ItemRepository,
        @Autowired private var categoryRepository: CategoryRepository
): BaseController() {

    private val logger = LoggerFactory.getLogger(ItemUpdateController::class.java)

    /**
     * Shows the item update screen.
     */
    @GetMapping("item/update/{itemId}")
    fun show(
            @PathVariable itemId: String,
            @SessionAttribute(ChickSeSession.SESSION_ATTRIBUTE_NAME) chickSeSession: ChickSeSession?
    ): ModelAndView {
        if (chickSeSession == null) {
            logger.info("unauthorized request. user has not logged in yet.")
            return ModelAndView("errors/401_unauthorized")
        }

        // Find item by item ID
        val item: Item? = this.itemRepository.findByItemId(itemId.toInt())
        if (item == null) {
            logger.info("item is not found by item_id=$itemId")
            return ModelAndView("errors/404_not_found")
        }

        // Find categories by item ID
        val category = this.categoryRepository.findByItemId(itemId.toInt())

        return ModelAndView().apply {
            this.viewName = "item_update"
            // Set up forms with default values
            this.addObject("updateForm", UpdateForm().apply {
                this.itemId = item.id.toString()
                this.title = item.title
                this.categoryName = if (category != null) category.name else ""
                this.content = item.content
            })
            this.addObject("deleteForm", DeleteForm().apply {
                this.itemId = item.id.toString()
            })
        }
    }

    /**
     * Updates the item specified by the item ID.
     */
    @PostMapping("item/updateExisting")
    fun update(
            @ModelAttribute form: UpdateForm,
            bindingResult: BindingResult,
            @SessionAttribute(ChickSeSession.SESSION_ATTRIBUTE_NAME) chickSeSession: ChickSeSession?
    ): String {
        if (chickSeSession == null) {
            logger.info("unauthorized request. user has not logged in yet.")
            return "errors/401_unauthorized"
        }

        // Validation result
        if (bindingResult.hasErrors()) {
            return "item_update"
        }

        // Find item by item ID
        val item: Item? = this.itemRepository.findByItemId(form.itemId!!.toInt())
        if (item == null) {
            logger.info("item is not found by item_id=${form.itemId}")
            return "errors/404_not_found"
        }

        // Update item
        this.itemRepository.update(
                item = item.apply {
                    this.title = form.title!!
                    this.content = form.content!!
                },
                categoryName = form.categoryName!!
        )

        // Redirect to item screen
        return "redirect:/item/${form.itemId}"
    }

    /**
     * Deletes the item specified by the item ID.
     */
    @PostMapping("item/deleteExisting")
    fun delete(
            @ModelAttribute form: DeleteForm,
            bindingResult: BindingResult,
            @SessionAttribute(ChickSeSession.SESSION_ATTRIBUTE_NAME) chickSeSession: ChickSeSession?
    ): String {
        if (chickSeSession == null) {
            logger.info("unauthorized request. user has not logged in yet.")
            return "errors/401_unauthorized"
        }

        if (bindingResult.hasErrors()) {
            return "item_update"
        }

        // Find item by item ID
        val item: Item? = this.itemRepository.findByItemId(form.itemId!!.toInt())
        if (item == null) {
            logger.info("item is not found by item_id=${form.itemId}")
            return "errors/404_not_found"
        }

        // Delete item
        this.itemRepository.delete(item.id!!)

        // Redirect to categories screen
        val category: Category? = this.categoryRepository.findByItemId(item.id)
        if (category == null) {
            logger.info("category is not found by item_id=${item.id}")
            return "redirect:/"
        }
        return "redirect:/category/${category.name}"
    }
}