package com.wahlen.dynamoservice.persistence.model

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "ProductInfo")
class ProductInfo {
    @get:DynamoDBAutoGeneratedKey
    @get:DynamoDBHashKey
    var id: String? = null

    @get:DynamoDBAttribute
    var msrp: String? = null

    @get:DynamoDBAttribute
    var cost: String? = null

    constructor()

    constructor(cost: String?, msrp: String?) {
        this.msrp = msrp
        this.cost = cost
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductInfo

        if (id != other.id) return false
        if (msrp != other.msrp) return false
        if (cost != other.cost) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (msrp?.hashCode() ?: 0)
        result = 31 * result + (cost?.hashCode() ?: 0)
        return result
    }


}
