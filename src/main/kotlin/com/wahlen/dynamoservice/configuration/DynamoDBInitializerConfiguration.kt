package com.wahlen.dynamoservice.configuration

import com.wahlen.dynamoservice.service.DynamoDBInitializationService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Profile
import org.springframework.beans.factory.InitializingBean

/**
 * Populates the database with initial data used for production, development and testing, respectively.
 */
@Configuration
class DynamoDBInitializerConfiguration(
    private val dynamoDBInitializationService: DynamoDBInitializationService
) {

    @Bean
    fun productionDatabaseInitializer() = InitializingBean {
        dynamoDBInitializationService.initializeProduction()
    }

    @Bean
    @DependsOn("productionDatabaseInitializer")
    @Profile("development", "test")
    fun developmentDatabaseInitializer() = InitializingBean {
        dynamoDBInitializationService.initializeDevelopment()
    }


}