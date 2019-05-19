package com.chick_se_trace.web.view.item

import com.chick_se_trace.domain.ItemRepository
import com.chick_se_trace.web.view.BaseController
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.servlet.ModelAndView

/**
 * Controller for item screen.
 *
 * @author shoichi.sato
 */
@Controller
class ItemController(
        @Autowired private var itemRepository: ItemRepository
) : BaseController() {

    private val logger = LoggerFactory.getLogger(ItemController::class.java)

    /**
     * Shows the item screen.
     */
    @GetMapping("item/{itemId}")
    fun show(@PathVariable itemId: String): ModelAndView {
        // Find item by item ID
        val item = this.itemRepository.findByItemId(itemId.toInt())
        if (item == null) {
            logger.info("item is not found by item_id=$itemId")
            return ModelAndView("errors/404_not_found")
        }
        return ModelAndView().apply {
            this.viewName = "item"
            this.addObject("item", item)
        }
    }
}