package com.target
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.WebIntegrationTest
import spock.lang.Specification

@SpringApplicationConfiguration(MyRetailApplication.class)
@WebIntegrationTest
abstract class BaseIntegrationSpec extends Specification {
    static final APP_URL = 'http://localhost:8080'
}
