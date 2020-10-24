package org.javadominicano.appwebspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

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

        @GetMapping(path = "/contador-sesion")
        public String contadorSesion(HttpSession session){
            Integer contador = (Integer) session.getAttribute("contador");
            if(contador == null){
                contador = 0;
            }
            contador++;
            session.setAttribute("contador", contador);
            //
            String idSesion = session.getId();
            //
            return String.format("<h3>Spring Boot - Usted ha visitado %d veces - ID Sesion: %s </h3>", contador, idSesion);
        }
    }

}
