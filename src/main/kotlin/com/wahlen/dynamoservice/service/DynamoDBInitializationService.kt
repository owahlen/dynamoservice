package com.wahlen.dynamoservice.service

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput
import com.amazonaws.services.dynamodbv2.model.ResourceInUseException
import com.wahlen.dynamoservice.persistence.model.ProductInfo
import com.wahlen.dynamoservice.persistence.repository.ProductInfoRepository
import org.springframework.stereotype.Service

@Service
class DynamoDBInitializationService(
    private val amazonDynamoDB: AmazonDynamoDB,
    private val dynamoDBMapper: DynamoDBMapper,
    private val productInfoRepository: ProductInfoRepository
) {

    fun initializeProduction() {
        createProductInfos()
    }

    fun initializeDevelopment() {
        val nProducts = productInfoRepository.count()
        if (nProducts == 0L) {
            val productInfo = ProductInfo.create("test product1", "20", "50")
            productInfoRepository.save(productInfo)
        }
    }

    private fun createProductInfos() {
        createTableIfNotExists(ProductInfo::class.java)
    }

    private fun createTableIfNotExists(clazz: Class<*>) {
        try {
            val createTableRequest = dynamoDBMapper.generateCreateTableRequest(clazz)
            createTableRequest.provisionedThroughput = ProvisionedThroughput(1L, 1L)
            createTableRequest.globalSecondaryIndexes.forEach {
                it.provisionedThroughput = ProvisionedThroughput(1L, 1L)
            }
            amazonDynamoDB.createTable(createTableRequest)
        } catch (e: ResourceInUseException) {
            // Ignore this exception that indicates that the table already exists
        }
    }

}