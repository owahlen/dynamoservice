package com.wahlen.dynamoservice.api.controller

import com.wahlen.dynamoservice.persistence.model.ProductInfo
import com.wahlen.dynamoservice.service.ProductInfoService
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/product")
@Validated
class ProductInfoController (
    private val productInfoService: ProductInfoService
) {
    @GetMapping(produces = [APPLICATION_JSON_VALUE])
    fun getAll() : Iterable<ProductInfo> {
        return productInfoService.getAll()
    }

    @GetMapping("/{productId}", produces = [APPLICATION_JSON_VALUE])
    fun get(@PathVariable @NotNull productId: String?) : ProductInfo {
        return productInfoService.get(productId!!)
    }

    @PostMapping(produces = [APPLICATION_JSON_VALUE])
    fun create(
        @Valid @RequestBody
        productInfoDTO: ProductInfo
    ) : ProductInfo {
        return productInfoService.create(productInfoDTO)
    }

    @PutMapping("/{productId}", produces = [APPLICATION_JSON_VALUE])
    fun update(
        @PathVariable @NotNull
        productId: String?,
        @Valid @RequestBody
        productInfoUpdateDTO: ProductInfo
    ) : ProductInfo {
        return productInfoService.update(productId!!, productInfoUpdateDTO)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{productId}")
    fun delete(
        @PathVariable @NotNull
        productId: String?
    ) {
        productInfoService.delete(productId!!)
    }
}
