package com.target.domain

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode
@ToString
class Product {
    String id
    String name
    Price price

}

@EqualsAndHashCode
@ToString
class Price{
    String value
    String currencyCode
}
