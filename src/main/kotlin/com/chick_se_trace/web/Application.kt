package com.chick_se_trace.web

import nz.net.ultraq.thymeleaf.LayoutDialect
import nz.net.ultraq.thymeleaf.decorators.strategies.GroupingStrategy
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect
import org.thymeleaf.spring5.SpringTemplateEngine
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver
import java.nio.charset.StandardCharsets

/**
 * Main method to start this application.
 *
 * @param args
 */
fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args);
}


/**
 * Configuration class for this application.
 *
 * @author shoichi.sato
 */
@SpringBootApplication
@ComponentScan(value = ["com.chick_se_trace"])
@EnableAutoConfiguration(exclude = [ErrorMvcAutoConfiguration::class])
@EnableRedisHttpSession
class Application {

    //-------------------------------------------------------------------------
    // Thymeleaf
    //-------------------------------------------------------------------------
    @Bean
    fun springResourceTemplateResolver(): SpringResourceTemplateResolver {
        return SpringResourceTemplateResolver().apply {
            this.prefix = "classpath:/templates/"
            this.suffix = ".html"
            this.characterEncoding = StandardCharsets.UTF_8.name()
            this.isCacheable = false
        }
    }
    /**
     * A bean for [SpringTemplateEngine]
     *
     * [Java8TimeDialect] Thymeleaf - Module for Java 8 Time API compatibility
     *                    Usage: th:text="${#temporals.format(item.updateTime,'yyyy-MM-dd')}"
     *                    See  : https://github.com/thymeleaf/thymeleaf-extras-java8time
     */
    @Bean
    fun templateEngine(): SpringTemplateEngine {
        val templateEngine = SpringTemplateEngine()
        templateEngine.setTemplateResolver(springResourceTemplateResolver())
        templateEngine.addDialect(LayoutDialect(GroupingStrategy()))
        templateEngine.addDialect(Java8TimeDialect())
        return templateEngine
    }

    //-------------------------------------------------------------------------
    // Message Source
    //-------------------------------------------------------------------------
    @Bean
    fun messageSource(): MessageSource {
        return ResourceBundleMessageSource().apply {
            setBasename("i18n/messages")
            setDefaultEncoding(StandardCharsets.UTF_8.name())
        }
    }
}