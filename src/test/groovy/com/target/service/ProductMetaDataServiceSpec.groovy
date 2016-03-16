package com.target.service
import com.fasterxml.jackson.databind.ObjectMapper
import com.target.exception.ResourceNotFoundException
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.web.client.RestTemplate
import spock.lang.Specification
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess

class ProductMetaDataServiceSpec extends Specification {

    RestTemplate restTemplate = new RestTemplate()
    ProductMetaDataService productMetaDataService = new ProductMetaDataService(productDescriptionTemplate: '/products/v3/{skuId}', productDescriptionRestTemplate: restTemplate)

    def "GetProductMetaData - valid sku id"(){
        setup:
            MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);
            String productDataString = getClass().getClassLoader().getResource('valid-product.json').text
        when:
            mockServer.expect(requestTo("/products/v3/16483589")).andExpect(method(HttpMethod.GET))
                    .andRespond(withSuccess(productDataString, MediaType.APPLICATION_JSON));
            Map productData = productMetaDataService.getProductMetaData('16483589')
        then:
            mockServer.verify()
            productData
            !productData.errors
            productData.general_description == 'AT&T iPhone 6 plus Gold 128GB'

    }

    def "GetProductMetaData - invalid sku id"(){
        setup:
            MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);
            String productDataString = getClass().getClassLoader().getResource('invalid-product.json').text
        when:
            mockServer.expect(requestTo("/products/v3/junk")).andExpect(method(HttpMethod.GET))
                    .andRespond(withSuccess(productDataString, MediaType.APPLICATION_JSON));
            productMetaDataService.getProductMetaData('junk')
        then:
            mockServer.verify()
            thrown(ResourceNotFoundException)

    }

    def "GetItem - valid sku id"() {
        setup:
            Map productData = new ObjectMapper().readValue(getClass().getClassLoader().getResourceAsStream('valid-product.json'), Map)
        when:
            Map result = productMetaDataService.getItem(productData)
        then:
            result
            result.general_description

    }

    def "GetItem - invalid sku id"() {
        setup:
            Map productData = new ObjectMapper().readValue(getClass().getClassLoader().getResourceAsStream('invalid-product.json'), Map)
        when:
            Map result = productMetaDataService.getItem(productData)
        then:
            result == null

    }
}
