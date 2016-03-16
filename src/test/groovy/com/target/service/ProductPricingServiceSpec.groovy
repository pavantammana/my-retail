package com.target.service

import com.target.domain.ProductPrice
import com.target.exception.ResourceNotFoundException
import com.target.repository.ProductPriceRepository
import spock.lang.Specification

class ProductPricingServiceSpec extends Specification {

    ProductPriceRepository repository = Mock(ProductPriceRepository)

    ProductPricingService productPricingService = new ProductPricingService(repository: repository)

    def "GetProductPrice - valid sku"() {
        when:
            ProductPrice productPrice = productPricingService.getProductPrice('SomeSkuId')
        then:
            1 * repository.findBySkuId('SomeSkuId') >> new ProductPrice(id: 'someId', skuId: 'SomeSkuId', price: 'SomePrice', currencyCode: 'SomeCurrency')
            productPrice
            productPrice.skuId == 'SomeSkuId'
            productPrice.price == 'SomePrice'
            productPrice.currencyCode == 'SomeCurrency'
    }

    def "GetProductPrice - invalid sku"() {
        when:
            productPricingService.getProductPrice('junk')
        then:
            1 * repository.findBySkuId('junk') >> null
            thrown(ResourceNotFoundException)
    }

    def "updateProductPrice"() {
        setup:
            ProductPrice newProductPrice = new ProductPrice(skuId: 'SomeSkuId', price: 'SomeNewPrice', currencyCode: 'SomeNewCurrency')
            ProductPrice existingProductPrice = new ProductPrice(id: 'someId', skuId: 'SomeSkuId', price: 'SomePrice', currencyCode: 'SomeCurrency')
            ProductPrice mergedProductPrice = new ProductPrice(id: 'someId', skuId: 'SomeSkuId', price: 'SomeNewPrice', currencyCode: 'SomeNewCurrency')

        when:
            ProductPrice result = productPricingService.updateProductPrice(newProductPrice)
        then:
            1 * repository.findBySkuId('SomeSkuId') >> existingProductPrice
            1 * repository.save(mergedProductPrice) >> mergedProductPrice
            result == mergedProductPrice

    }
}
