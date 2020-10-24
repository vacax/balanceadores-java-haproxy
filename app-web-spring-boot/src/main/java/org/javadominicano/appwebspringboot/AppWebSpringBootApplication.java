package org.javadominicano.appwebspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class AppWebSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppWebSpringBootApplication.class, args);
    }

    @RestController
    @RequestMapping(path = "/")
    public class MiControlador{

        @GetMapping(path = "/")
        public String index(){
            return "Hola mundo en Spring Boot";
        }
    }

}
