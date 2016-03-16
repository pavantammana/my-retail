package com.target.resource
import com.target.domain.Error
import com.target.domain.Price
import com.target.domain.Product
import com.target.domain.ProductPrice
import com.target.exception.ResourceNotFoundException
import com.target.service.ProductMetaDataService
import com.target.service.ProductPricingService
import groovy.util.logging.Slf4j
import groovyx.gpars.GParsPool
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

import javax.servlet.http.HttpServletRequest
import java.util.concurrent.ExecutionException
import java.util.concurrent.Future

@RestController
@RequestMapping(value = '/api/v1/products', produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
class ProductController {

    @Autowired
    ProductMetaDataService productMetaDataService

    @Autowired
    ProductPricingService productPricingService


    @RequestMapping(value = '/{skuId}', method = RequestMethod.GET)
    Product getProductAggregatedData(@PathVariable String skuId) {

        Future<Map> productMetaDataFuture
        Future<ProductPrice> productPriceFuture

        Map productMetaData

        ProductPrice productPrice

        GParsPool.withPool {
            productMetaDataFuture = { productMetaDataService.getProductMetaData(skuId) }.callAsync()

            productPriceFuture = { productPricingService.getProductPrice(skuId) }.callAsync()

            unwrapExecutionException {
                productMetaData = productMetaDataFuture.get()
                productPrice = productPriceFuture.get()
            }


            Product product = new Product(
                    id: skuId,
                    name: productMetaData.general_description,
                    price: new Price(
                            value: productPrice.price,
                            currencyCode: productPrice.currencyCode
                    )
            )
            product
        }

    }

    @RequestMapping(value = '/{skuId}/price', method = RequestMethod.PUT)
    Price updateProductPrice(@PathVariable String skuId, @RequestBody Price price){
        ProductPrice updatedPrice = productPricingService.updateProductPrice(new ProductPrice(skuId: skuId, price: price.value, currencyCode: price.currencyCode))
        new Price(value: updatedPrice.price, currencyCode: updatedPrice.currencyCode)
    }

    Object unwrapExecutionException(Closure action) {
        try {
            return action()
        } catch (ExecutionException e) {
            log.error('ExecutionException in unwrapExecutionException', e)
            throw e.cause
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    Error handleResourceNotFound(ResourceNotFoundException exception, HttpServletRequest request) {
        log.error("ResourceNotFound received for path:${request.requestURI}, message:${exception.getMessage()}", exception)
        new Error(message: exception.message)
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    Error handleExceptions(Throwable throwable, HttpServletRequest request) {
        log.error("Exception received for path:${request.requestURI}, message:${throwable.getMessage()}", throwable)
        new Error(message: 'Internal Server Error')
    }
}
