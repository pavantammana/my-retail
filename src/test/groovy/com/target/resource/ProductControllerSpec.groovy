package com.target.resource
import com.fasterxml.jackson.databind.ObjectMapper
import com.target.domain.Error
import com.target.domain.Price
import com.target.domain.Product
import com.target.domain.ProductPrice
import com.target.exception.ResourceNotFoundException
import com.target.service.ProductMetaDataService
import com.target.service.ProductPricingService
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest
/**
 * Created by a1066475 on 3/15/16.
 */
class ProductControllerSpec extends Specification {

    ProductMetaDataService productMetaDataService = Mock(ProductMetaDataService)
    ProductPricingService productPricingService = Mock(ProductPricingService)

    ProductController productController = new ProductController(productMetaDataService: productMetaDataService, productPricingService:  productPricingService)

    def "GetProductAggregatedData"() {
        setup:
            Map productMetaData = new ObjectMapper().readValue(getClass().getClassLoader().getResourceAsStream('product-metadata.json'), Map)
            ProductPrice productPrice = new ProductPrice(id: 'someId', skuId: '16483589', price: '10.50', currencyCode: 'USD')
        when:
            Product product = productController.getProductAggregatedData('16483589')
        then:
            1 * productMetaDataService.getProductMetaData('16483589') >> productMetaData
            1 * productPricingService.getProductPrice('16483589') >> productPrice
            product
            product.id == '16483589'
            product.name == productMetaData.general_description
            product.price.value == productPrice.price
            productPrice.currencyCode == productPrice.currencyCode
    }

    def "GetProductAggregatedData - Product MetaData call failed"() {
        when:
            productController.getProductAggregatedData('16483589')
        then:
            1 * productMetaDataService.getProductMetaData('16483589') >> { throw new ResourceNotFoundException('Error')}
            1 * productPricingService.getProductPrice('16483589')
            thrown(ResourceNotFoundException)
    }

    def "GetProductAggregatedData - Product Price call failed"() {
        setup:
            Map productMetaData = new ObjectMapper().readValue(getClass().getClassLoader().getResourceAsStream('product-metadata.json'), Map)
        when:
            productController.getProductAggregatedData('16483589')
        then:
            1 * productMetaDataService.getProductMetaData('16483589') >> productMetaData
            1 * productPricingService.getProductPrice('16483589') >> { throw new ResourceNotFoundException('Error')}
            thrown(ResourceNotFoundException)
    }

    def "HandleResourceNotFound"(){
        setup:
            ResourceNotFoundException exception = new ResourceNotFoundException('Error Message')
            HttpServletRequest httpServletRequest = Mock(HttpServletRequest)
        when:
            Error error = productController.handleResourceNotFound(exception, httpServletRequest)
        then:
            1 * httpServletRequest.getRequestURI()
            error
            error.message == 'Error Message'
    }

    def "HandleExceptions"(){
        setup:
            Throwable exceptionToReturn = exception
            HttpServletRequest httpServletRequest = Mock(HttpServletRequest)
        when:
            Error error = productController.handleExceptions(exceptionToReturn, httpServletRequest)
        then:
            1 * httpServletRequest.getRequestURI()
            error
            error.message == errorMessage
        where:
            exception | errorMessage
            new RuntimeException('Error Message') | 'Internal Server Error'
            new Exception('Error Message2') | 'Internal Server Error'
    }

    def "UnwrapExecutionException"() {
        when:
            Object returnObject = productController.unwrapExecutionException { new ResourceNotFoundException('Error message')}
        then:
            returnObject.getClass() == ResourceNotFoundException.class
            ((ResourceNotFoundException)returnObject).message == 'Error message'
    }

    def "UpdateProductPrice"(){
        setup:
            Price priceToUpdate = new Price(value: 'Some Value', currencyCode: 'Some Currency')
            ProductPrice productPrice = new ProductPrice(skuId: 'Some skuId', price: 'Some Value', currencyCode: 'Some Currency')
            ProductPrice mergedProductPrice = new ProductPrice(id: 'Some id', skuId: 'Some skuId', price: 'Some Value', currencyCode: 'Some Currency')
        when:
            Price result = productController.updateProductPrice('Some skuId', priceToUpdate)
        then:
            1 * productPricingService.updateProductPrice(productPrice) >> mergedProductPrice
            result
            result == priceToUpdate
    }
}
