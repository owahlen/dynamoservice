package com.wahlen.dynamoservice.persistence.repository

import com.wahlen.dynamoservice.persistence.model.ProductInfo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ProductInfoRepositoryIntegrationTests(
    @Autowired val productInfoRepository: ProductInfoRepository
) {

    @Test
    fun `create and load a ProductInfo entity`() {
        // setup
        val expectedCost = "30"
        val expectedPrice = "60"
        val productInfo = ProductInfo(expectedCost, expectedPrice)

        // when
        productInfoRepository.save(productInfo)

        // then
        val productInfos = productInfoRepository.findAll().toList()
        assertThat(productInfos.size).isGreaterThanOrEqualTo(1)
        val containsExpectedProduct = productInfos.any { it.cost == expectedCost && it.msrp == expectedPrice }
        assertThat(containsExpectedProduct).isTrue()
    }

}
