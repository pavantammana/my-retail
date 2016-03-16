package com.target.spring

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

/**
 * Created by a1066475 on 3/12/16.
 */
@Configuration
class RootConfig {

    @Value('${productService.uri}')
    String productUri


    @Bean
    RestTemplate productDescriptionRestTemplate(){
        new RestTemplate().with {
            uriTemplateHandler.baseUrl = productUri
            it
        }
    }

}
