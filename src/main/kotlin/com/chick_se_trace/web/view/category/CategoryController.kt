package com.chick_se_trace.web.view.category

import com.chick_se_trace.domain.ItemRepository
import com.chick_se_trace.web.view.BaseController
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.servlet.ModelAndView

/**
 * Controller for category screen.
 *
 * @author shoichi.sato
 */
@Controller
class CategoryController(
        @Autowired private var itemRepository: ItemRepository
) : BaseController() {

    private val logger = LoggerFactory.getLogger(CategoryController::class.java)

    /**
     * Shows the category screen.
     */
    @GetMapping("category/{categoryName}")
    fun show(@PathVariable categoryName: String
    ): ModelAndView {

        // Find items by category name
        val items = this.itemRepository.findByCategoryName(categoryName)
        if (items == null) {
            logger.info("item is not found by category_name=$categoryName")
            return ModelAndView("errors/404_not_found")
        }
        return ModelAndView().apply {
            this.viewName = "category"
            this.addObject("categoryName", categoryName)
            this.addObject("items", items.sortedByDescending { it.createTime })
        }
    }
}