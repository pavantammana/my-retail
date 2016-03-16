package com.target.resource

import com.target.BaseIntegrationSpec
import com.target.domain.Price
import com.target.domain.Product
import org.springframework.boot.test.TestRestTemplate
import org.springframework.http.*
import org.springframework.web.client.RestTemplate
/**
 * Created by a1066475 on 3/15/16.
 */
class ProductControllerIntegrationSpec extends BaseIntegrationSpec {

    RestTemplate restTemplate
    HttpEntity httpEntity
    HttpHeaders headers

    def setup(){
        restTemplate = new TestRestTemplate()
        headers = new HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON)
        headers.setAccept([MediaType.APPLICATION_JSON])
        httpEntity = new HttpEntity(headers)
    }


    def "GetProductAggregatedData - valid product"() {
        when:
            ResponseEntity<Product> responseEntity = restTemplate.exchange("${APP_URL}/api/v1/products/16483589", HttpMethod.GET, httpEntity, Product.class)
            Product product = responseEntity.getBody()
        then:
            responseEntity
            responseEntity.statusCode == HttpStatus.OK
            product
            product.id == '16483589'
            product.name == 'AT&T iPhone 6 plus Gold 128GB'
            product.price.value == '13.25'
            product.price.currencyCode == 'USD'
    }

    def "GetProductAggregatedData - invalid product id"() {
        when:
            ResponseEntity<String> responseEntity = restTemplate.exchange("${APP_URL}/api/v1/products/junk", HttpMethod.GET, httpEntity, String.class)
        then:
            responseEntity
            responseEntity.statusCode == HttpStatus.NOT_FOUND
            responseEntity.getBody() == '{"message":"The productId does not represent a valid product in the system"}'
    }

    def "GetProductAggregatedData - product without price"() {
        when:
            ResponseEntity<String> responseEntity = restTemplate.exchange("${APP_URL}/api/v1/products/16752456", HttpMethod.GET, httpEntity, String.class)
        then:
            responseEntity
            responseEntity.statusCode == HttpStatus.NOT_FOUND
            responseEntity.getBody() == '{"message":"Product price not found"}'
    }

    def "UpdateProductPrice - valid product and price"(){
        setup:
            Price price = new Price(value: '13.25', currencyCode: 'USD')

            HttpEntity<Price> priceHttpEntity = new HttpEntity<Price>(price, headers)
        when:
            ResponseEntity<Price> responseEntity = restTemplate.exchange("${APP_URL}/api/v1/products/16483589/price", HttpMethod.PUT, priceHttpEntity, Price.class)
            Price result = responseEntity.getBody()
        then:
            responseEntity
            responseEntity.statusCode == HttpStatus.OK
            result
            result == price
    }
}
