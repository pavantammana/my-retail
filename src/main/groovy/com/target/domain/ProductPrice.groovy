package com.target.domain

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.springframework.data.annotation.Id;

@EqualsAndHashCode(excludes = ['id'])
@ToString
class ProductPrice {
    @Id
    private String id

    String skuId

    String price

    String currencyCode
}
