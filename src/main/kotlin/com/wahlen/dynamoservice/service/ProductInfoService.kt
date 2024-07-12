package com.wahlen.dynamoservice.service

import com.wahlen.dynamoservice.persistence.model.ProductInfo
import com.wahlen.dynamoservice.persistence.repository.ProductInfoRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class ProductInfoService(
    private val productInfoRepository: ProductInfoRepository
) {
    fun getAll(): Iterable<ProductInfo> {
        return productInfoRepository.findAll()
    }

    fun get(id: String): ProductInfo {
        return productInfoRepository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "product info not found")
        }
    }

    fun create(productInfoDTO: ProductInfo): ProductInfo {
        return productInfoRepository.save(productInfoDTO)
    }

    fun update(id: String, productInfoUpdateDTO: ProductInfo): ProductInfo {
        val productInfo = productInfoRepository.findById(id).orElseThrow{
            ResponseStatusException(HttpStatus.NOT_FOUND, "product info not found")
        }
        productInfo.msrp = productInfoUpdateDTO.msrp
        productInfo.cost = productInfoUpdateDTO.cost
        return productInfoRepository.save(productInfo)
    }

    fun delete(id: String) {
        if(!productInfoRepository.existsById(id))
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Product info not found")
        productInfoRepository.deleteById(id)
    }

}