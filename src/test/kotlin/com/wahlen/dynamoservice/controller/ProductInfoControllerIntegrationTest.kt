package com.wahlen.dynamoservice.controller

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.wahlen.dynamoservice.persistence.model.ProductInfo
import com.wahlen.dynamoservice.persistence.repository.ProductInfoRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.AssertionsForClassTypes
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*

@SpringBootTest
@AutoConfigureMockMvc
class ProductInfoControllerIntegrationTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper,
    @Autowired val productInfoRepository: ProductInfoRepository
) {

    // #################### get ####################
    @Test
    fun `test 'getAll' returns 200`() {
        // setup
        val productInfo = productInfoRepository.findAll().first()

        // when
        mockMvc.get("/product")
            // then
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
            }.andDo {
                handle {
                    val responseJson = it.response.contentAsString
                    val productInfoResponseDTOs = objectMapper
                        .readValue(responseJson, object : TypeReference<List<ProductInfo>>() {})
                        .toList()
                    AssertionsForClassTypes.assertThat(productInfoResponseDTOs).isEqualTo(listOf(productInfo))
                }
            }
    }

    @Test
    fun `test 'get' returns 200`() {
        // setup
        val productInfo = productInfoRepository.findAll().first()

        // when
        mockMvc.get("/product/${productInfo.id}")
            // then
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
            }.andDo {
                handle {
                    val responseJson = it.response.contentAsString
                    val productInfoResponseDTO = objectMapper.readValue(responseJson, ProductInfo::class.java)
                    assertThat(productInfoResponseDTO).isEqualTo(productInfo)
                }
            }
    }

    // #################### create ####################
    @Test
    fun `test 'create' returns 200`() {
        // setup
        val productInfoCreateDTO = ProductInfo("80","90")

        // when
        lateinit var productInfoResponseDTO : ProductInfo
        mockMvc.post("/product"){
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(productInfoCreateDTO)
        }
            // then
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
            }.andDo {
                handle {
                    val responseJson = it.response.contentAsString
                    productInfoResponseDTO = objectMapper.readValue(responseJson, ProductInfo::class.java)
                    assertThat(productInfoResponseDTO.id).isNotNull()
                    assertThat(productInfoResponseDTO.cost).isEqualTo(productInfoCreateDTO.cost)
                    assertThat(productInfoResponseDTO.msrp).isEqualTo(productInfoCreateDTO.msrp)
                }
            }

        // cleanup
        productInfoRepository.deleteById(productInfoResponseDTO.id!!)
    }

    // #################### update ####################
    @Test
    fun `test 'update' returns 200`() {
        // setup
        val productInfo = productInfoRepository.findAll().first()
        val productInfoUpdateDTO = ProductInfo("10","20")

        // when
        mockMvc.put("/product/${productInfo.id}"){
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(productInfoUpdateDTO)
        }
            // then
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
            }.andDo {
                handle {
                    val responseJson = it.response.contentAsString
                    val productInfoResponseDTO = objectMapper.readValue(responseJson, ProductInfo::class.java)
                    assertThat(productInfoResponseDTO.id).isEqualTo(productInfo.id)
                    assertThat(productInfoResponseDTO.cost).isEqualTo(productInfoUpdateDTO.cost)
                    assertThat(productInfoResponseDTO.msrp).isEqualTo(productInfoUpdateDTO.msrp)
                }
            }

        // cleanup
        productInfoRepository.save(productInfo) // save the old values
    }

    // #################### delete ####################
    @Test
    fun `test 'delete' returns 200`() {
        val productInfo = productInfoRepository.findAll().first()

        // when
        mockMvc.delete("/product/${productInfo.id}")
            // then
            .andExpect {
                status { isNoContent() }
            }
        // the product should now be removed from the database
        assertThat(productInfoRepository.findById(productInfo.id!!)).isEmpty()

        // cleanup
        productInfoRepository.save(productInfo) // save the old values
    }

}
