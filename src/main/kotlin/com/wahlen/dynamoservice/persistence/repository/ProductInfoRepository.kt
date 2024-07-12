package com.wahlen.dynamoservice.persistence.repository

import com.wahlen.dynamoservice.persistence.model.ProductInfo
import org.socialsignin.spring.data.dynamodb.repository.EnableScan
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import java.util.*

@EnableScan
interface ProductInfoRepository : CrudRepository<ProductInfo, String> {
    override fun findById(id: String): Optional<ProductInfo>
}
