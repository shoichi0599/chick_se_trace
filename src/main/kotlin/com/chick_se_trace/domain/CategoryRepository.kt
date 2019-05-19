package com.chick_se_trace.domain

import com.chick_se_trace.base.db.CategoriesMapper
import com.chick_se_trace.base.db.CategoryEntity
import com.chick_se_trace.base.db.ItemCategoryEntity
import com.chick_se_trace.base.db.ItemsCategoriesMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class CategoryRepository(
        @Autowired private var categoriesMapper: CategoriesMapper,
        @Autowired private var itemsCategoriesMapper: ItemsCategoriesMapper
) {
    private val logger = LoggerFactory.getLogger(CategoryRepository::class.java)

    fun findByItemId(itemId: Int): Category? {
        val itemCategoryEntities: List<ItemCategoryEntity> = this.itemsCategoriesMapper.selectByItemId(itemId)
        if (itemCategoryEntities.isEmpty()) {
            logger.info("itemsCategories are not found by itemId=$itemId")
            return null
        }
        val categoryEntity: CategoryEntity? = this.categoriesMapper.selectByCategoryId(
                itemCategoryEntities.first().categoryId!!
        )
        return Category.fromEntity(categoryEntity!!)
    }

    fun findByCategoryName(categoryName: String): Category {
        val categoryEntity: CategoryEntity? = this.categoriesMapper.selectByCategoryName(categoryName)
        categoryEntity?: return Category(id = null, name = categoryName)
        return Category.fromEntity(categoryEntity)
    }

    fun create(category: Category): Int {
        TODO("To be implemented")
        return 0
    }
}