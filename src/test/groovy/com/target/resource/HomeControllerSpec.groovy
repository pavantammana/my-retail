package com.target.resource
import com.target.BaseIntegrationSpec
import org.springframework.beans.factory.annotation.Autowired

class HomeControllerSpec extends BaseIntegrationSpec {

    @Autowired
    HomeController homeController

    def "verify properties"(){
        expect:
            homeController.applicationVersion
            homeController.applicationName
    }

    def "Home"() {
        when:
            String result = homeController.home()
        then:
            result == "${homeController.applicationName} - ${homeController.applicationVersion}"
    }
}
