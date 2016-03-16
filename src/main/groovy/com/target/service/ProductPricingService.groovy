package com.target.service

import com.target.domain.ProductPrice
import com.target.exception.ResourceNotFoundException
import com.target.repository.ProductPriceRepository
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
@Slf4j
class ProductPricingService {
    @Autowired
    ProductPriceRepository repository

    ProductPrice getProductPrice(String skuId){
        ProductPrice productPrice = repository.findBySkuId(skuId)
        if(!productPrice) {
            throw new ResourceNotFoundException('Product price not found')
        }
        productPrice
    }

    ProductPrice updateProductPrice(ProductPrice productPrice){
        ProductPrice existingPrice = getProductPrice(productPrice.skuId)
        existingPrice.price = productPrice.price
        existingPrice.currencyCode = productPrice.currencyCode
        repository.save(existingPrice)
    }


}
