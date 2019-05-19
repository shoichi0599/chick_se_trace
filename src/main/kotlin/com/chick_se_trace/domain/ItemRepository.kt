package com.chick_se_trace.domain

import com.chick_se_trace.base.db.CategoriesMapper
import com.chick_se_trace.base.db.CategoryEntity
import com.chick_se_trace.base.db.ItemCategoryEntity
import com.chick_se_trace.base.db.ItemEntity
import com.chick_se_trace.base.db.ItemsCategoriesMapper
import com.chick_se_trace.base.db.ItemsMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.TimeZone

@Repository
class ItemRepository(
        @Autowired private var itemsMapper: ItemsMapper,
        @Autowired private var categoriesMapper: CategoriesMapper,
        @Autowired private var itemsCategoriesMapper: ItemsCategoriesMapper
) {
    private val logger = LoggerFactory.getLogger(ItemRepository::class.java)
    private val jstZoneId = TimeZone.getTimeZone("JST").toZoneId()

    fun findByItemId(itemId: Int): Item? {
        val itemEntity: ItemEntity? = this.itemsMapper.selectByItemId(itemId)
        if (itemEntity == null) {
            logger.info("item is not found by itemId=$itemId")
            return null
        }
        return Item.fromEntity(itemEntity)
    }

    fun findByCategoryName(categoryName: String): List<Item>? {
        val categoryEntity: CategoryEntity? = categoriesMapper.selectByCategoryName(categoryName)
        if (categoryEntity == null) {
            logger.info("category is not found by categoryName=$categoryName")
            return null
        }
        val itemsCategoryEntity = itemsCategoriesMapper.selectByCategoryId(categoryEntity.categoryId!!)
        if (itemsCategoryEntity.isEmpty()) {
            logger.info("itemsCategories are not found by categoryId=${categoryEntity.categoryId}")
            return null
        }
        return this.itemsMapper.selectByItemIds(
                itemsCategoryEntity.map { it.itemId!! }
        ).map { Item.fromEntity(it) }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = arrayOf(Throwable::class))
    fun create(item: Item, categoryName: String): Int {
        if (item.id != null) {
            logger.info("item ID must be null for creating new item. Item=${item.toString()}")
            throw IllegalArgumentException("item ID must be null for creating new item. Item=${item.toString()}")
        }
        if (item.createTime != null) {
            logger.info("create time must be null for creating new item. Item=${item.toString()}")
            throw IllegalArgumentException("create time must be null for creating new item. Item=${item.toString()}")
        }
        if (item.updateTime != null) {
            logger.info("update time must be null for creating new item. Item=${item.toString()}")
            throw IllegalArgumentException("update time must be null for creating new item. Item=${item.toString()}")
        }
        // Time commonly used
        val now = LocalDateTime.now(jstZoneId)

        // Insert into items
        val itemId = itemsMapper.insert(
                ItemEntity().apply {
                    this.title = item.title
                    this.content = item.content
                    this.createTime = now
                    this.updateTime = now
                }
        )

        // Get categoryId if not found by category name, create a new one
        val categoryEntity: CategoryEntity? = this.categoriesMapper.selectByCategoryName(categoryName)
        val categoryId = if (categoryEntity == null) {
            this.categoriesMapper.insert(
                    CategoryEntity().apply {
                        this.categoryName = categoryName
                        this.createTime = now
                        this.updateTime = now
                    }
            )
        } else { categoryEntity.categoryId!! }

        // Insert into items_categories
        this.itemsCategoriesMapper.insert(
                ItemCategoryEntity().apply {
                    this.itemId = itemId
                    this.categoryId = categoryId
                    this.createTime = now
                    this.updateTime
                }
        )
        return itemId
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = arrayOf(Throwable::class))
    fun update(item: Item, categoryName: String) {
        if (item.id == null) {
            logger.info("item ID must not be null for updating the existing item. item=${item.toString()}")
            throw IllegalArgumentException("item ID must not be null for updating the existing item. item=${item.toString()}")
        }
        if (item.createTime == null) {
            logger.info("create time must not be null for updating the existing item. item=${item.toString()}")
            throw IllegalArgumentException("create time must not be null for updating the existing item. item=${item.toString()}")
        }
        if (item.updateTime == null) {
            logger.info("update time must not be null for updating the existing item. item=${item.toString()}")
            throw IllegalArgumentException("update time must not be null for updating the existing item. item=${item.toString()}")
        }

        // Time commonly used
        val now = LocalDateTime.now(jstZoneId)
        this.itemsMapper.update(
                ItemEntity().apply {
                    this.itemId = item.id
                    this.title = item.title
                    this.content = item.content
                    this.updateTime = now
                }
        )

        // Find category by item id to check if category has already been registered
        val categoryEntities: List<CategoryEntity> = this.categoriesMapper.selectByCategoryIds(
                this.itemsCategoriesMapper.selectByItemId(item.id)
                        .map { it.categoryId!! }
        )
        // If category name has not registered yet, insert the new one as well as items_categories
        if (!categoryEntities.any { it.categoryName == categoryName }) {
            // Insert into categories
            val categoryId = this.categoriesMapper.insert(
                    CategoryEntity().apply {
                        this.categoryName = categoryName
                        this.createTime = now
                        this.updateTime = now
                    }
            )
            // Insert into items_categories
            this.itemsCategoriesMapper.insert(
                    ItemCategoryEntity().apply {
                        this.itemId = item.id
                        this.categoryId = categoryId
                        this.createTime = now
                        this.updateTime = now
                    }
            )
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = arrayOf(Throwable::class))
    fun delete(itemId: Int) {
        // Delete from items
        this.itemsMapper.delete(itemId)
        // Delete from items_categories
        this.itemsCategoriesMapper.deleteByItemId(itemId)
    }
}