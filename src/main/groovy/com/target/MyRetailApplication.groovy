package com.target

import groovy.util.logging.Slf4j
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
@Slf4j
class MyRetailApplication{

    public static void main(String[] args) {
        SpringApplication.run MyRetailApplication.class, args
    }
}
