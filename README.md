##Overview
This project, my-retail, is a Spring Boot project.  It has the following dependencies.
   - Groovy
   - Spring (for DI and endpoint processing)
   - MongoDB
   - Spock for testing

It makes use of Spring for DI, implementing REST endpoints and also invoking external REST calls. Uses MongoDB as NoSQL store for storing the price related documents

##Setup
Repository location is: https://github.com/pavantammana/my-retail

MongoDB running with default configuration (localhost:27017 and the default db 'test' and collection name for prices is 'productPrice').

##Data Setup
Start MongoDB locally and once you are in the Mongo Shell run the following following commands to setup data

use test

db.productPrice.drop()

db.createCollection("productPrice")

db.productPrice.insert({"skuId" : "16483589", "price" : "13.25", "currencyCode" : "USD" })



##IDE Configuration
After cloning the project from Git, In IntelliJ, simply File->Open and click the pom.xml file in the my-retail directory.

IntelliJ 14.1+ is Spring Boot aware and has excellent help and completion for Spring Boot configuration.  After loading the project, you can run the project in IntelliJ by simply doing a right-click on the MyRetailApplication class and selecting Run. IntelliJ will recognize it as a Spring Boot app and start it accordingly.  It also gives some special configuration options for Spring Boot apps in the Run/Debug Configuration.

##Maven Configuration

Got to folder where your project is located and run following commands

mvn clean install

mvn spring-boot:run

##Tests
The project contains both Unit Tests and Integration tests. To run all tests from IDE just select test/groovy folder and select 'Run All Tests'

To run tests from Maven just run 'mvn clean install'

Pre-requisite: To make sure your integration tests run please run 'Data Setup' section

## Accessing API

API only supports application/json as the media-type. Please set both the Content-Type and Accepts header to 'application/json' before accessing the api.

GET Product

  curl -X GET -H 'Content-Type:application/json' -H 'Accept:application/json' http://localhost:8080/api/v1/products/16483589

PUT Price

  curl -X PUT -H 'Content-Type:application/json' -H 'Accept:application/json' -d '{"currencyCode": "USD", "value": "13.25"}' http://localhost:8080/api/v1/products/16483589/price

##Assumptions

- The GET API is designed to return the aggregated data for a product only if both product information and price information is available, otherwise it will return 404
- The PUT API is designed to only update if there is a product given already has a price associated with it, otherwise it will return 404.

inserting something
