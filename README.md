##Overview
This project, my-retail, is a Spring Boot project.  It has the following dependencies.
   - Groovy
   - Spring (for DI and endpoint processing)
   - MongoDB
   - Spock for testing

It makes use of Spring for DI and endpoint processing. Uses MongoDB as NoSQL store for storing the price related documents

##Setup
Repository location is:

MongoDB running with default configuration (localhost:27017 and the default db 'test' and collection name for prices is 'productPrice').

##IDE Configuration
After cloning the project, In IntelliJ, simply File->Open and click the pom.xml file in the my-retail directory.

IntelliJ 14.1+ is Spring Boot aware and has excellent help and completion for Spring Boot configuration.  After loading the project, you can run the project in IntelliJ by simply doing a right-click on the MyRetailApplication class and selecting Run. IntelliJ will recognize it as a Spring Boot app and start it accordingly.  It also gives some special configuration options for Spring Boot apps in the Run/Debug Configuration.

# my-retail
