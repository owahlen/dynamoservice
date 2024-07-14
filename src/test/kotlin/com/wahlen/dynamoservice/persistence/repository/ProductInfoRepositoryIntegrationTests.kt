package com.wahlen.dynamoservice.persistence.repository

import com.wahlen.dynamoservice.persistence.model.ProductInfo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime.now

@SpringBootTest
class ProductInfoRepositoryIntegrationTests(
    @Autowired val productInfoRepository: ProductInfoRepository
) {

    @Test
    fun `create and load a ProductInfo entity`() {
        // setup
        val testStart = now()
        val expectedName = "foo"
        val expectedPrice = "60"
        val expectedCost = "30"
        val productInfo = ProductInfo.create(expectedName, expectedPrice, expectedCost)

        // when
        productInfoRepository.save(productInfo)

        // then
        val productInfos = productInfoRepository.findAll().toList()
        assertThat(productInfos.size).isGreaterThanOrEqualTo(1)
        val foundProduct = productInfos.find {
            it.name == expectedName && it.msrp == expectedPrice && it.cost == expectedCost
        }
        assertThat(foundProduct).isNotNull
        assertThat(foundProduct!!.createdAt).isAfterOrEqualTo(testStart)
    }

}
