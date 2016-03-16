package com.target.resource

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeController {

    @Value('${application.name}')
    String applicationName

    @Value('${application.version}')
    String applicationVersion

    @RequestMapping("/")
    String home(){
        "$applicationName - $applicationVersion"
    }
}
