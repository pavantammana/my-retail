package com.target.service

import com.target.exception.ResourceNotFoundException
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
@Slf4j
class ProductMetaDataService {

    @Value('${productService.productDescriptionTemplate}')
    String productDescriptionTemplate

    @Autowired
    RestTemplate productDescriptionRestTemplate

    Map getProductMetaData(String skuId){
        Map productResponse = productDescriptionRestTemplate.getForObject(productDescriptionTemplate, Map, skuId)
        log.info("ProductData: ${productResponse}")
        Map productMetaData = getItem(productResponse)
        if(!productMetaData){
            throw new ResourceNotFoundException('The productId does not represent a valid product in the system')
        }
        productMetaData
    }

    Map getItem(Map productData){
        Map item = null
        def items = productData?.product_composite_response?.items
        if(items && !items[0].errors){
            item = items[0]
        }
        item
    }
}
