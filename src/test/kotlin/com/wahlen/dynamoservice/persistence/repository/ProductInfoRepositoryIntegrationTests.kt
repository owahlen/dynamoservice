package com.wahlen.dynamoservice.persistence.repository

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException
import com.wahlen.dynamoservice.persistence.extension.LocalDynamoExtension
import com.wahlen.dynamoservice.persistence.model.ProductInfo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@ExtendWith(LocalDynamoExtension::class)
class ProductInfoRepositoryIntegrationTests(
    @Autowired val productInfoRepository: ProductInfoRepository,
    @Autowired val dynamoDBMapper: DynamoDBMapper,
    @Autowired val amazonDynamoDB: AmazonDynamoDB
) {

    @BeforeEach
    fun setup() {
        try {
            val deleteTableRequest = dynamoDBMapper.generateDeleteTableRequest(ProductInfo::class.java)
            amazonDynamoDB.deleteTable(deleteTableRequest)
        } catch (e: ResourceNotFoundException) {
            // Do nothing, table does not exist
        }

        val createTableRequest = dynamoDBMapper.generateCreateTableRequest(ProductInfo::class.java)
        createTableRequest.provisionedThroughput = ProvisionedThroughput(1L, 1L)
        amazonDynamoDB.createTable(createTableRequest)
    }

    @Test
    fun `create and load a ProductInfo entity`() {
        // setup
        val expectedCost = "20"
        val expectedPrice = "50"
        val productInfo = ProductInfo(expectedCost, expectedPrice)

        // when
        productInfoRepository.save(productInfo)

        // then
        val productInfos = productInfoRepository.findAll().toList()
        assertThat(productInfos.size).isEqualTo(1)
        val createdProductInfo = productInfos.first()
        assertThat(createdProductInfo.cost).isEqualTo(expectedCost)
        assertThat(createdProductInfo.msrp).isEqualTo(expectedPrice)
    }

}