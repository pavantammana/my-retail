package com.target.repository

import com.target.domain.ProductPrice
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * Created by a1066475 on 3/13/16.
 */
interface ProductPriceRepository extends MongoRepository<ProductPrice, String>{

    ProductPrice findBySkuId(String skuId)

}