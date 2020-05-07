package edu.javadominicano.bsb;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapAttributeConfig;
import com.hazelcast.config.MapIndexConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.session.hazelcast.HazelcastIndexedSessionRepository;
import org.springframework.session.hazelcast.PrincipalNameExtractor;
import org.springframework.session.hazelcast.config.annotation.web.http.EnableHazelcastHttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@SpringBootApplication
@EnableHazelcastHttpSession
public class BalanceoSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(BalanceoSpringBootApplication.class, args);
    }

    @RestController
    @RequestMapping(value = "/")
    static class MiControlador{

        @Value("${server.port}")
        private int puerto;

        @GetMapping(path = "/ejemplo-peticion")
        public String pruebaPeticion(){
            return "<h3>Spring Boot - Servidor escuchando en el puerto: "+puerto+"</h3>";
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
            return String.format("<h3>Spring Boot - Usted ha visitado %d veces - Puerto: %d - ID Sesion: %s </h3>", contador, puerto, idSesion);
        }

        @Bean
        public HazelcastInstance hazelcastInstance() {
            //Configuración basica.
            MapAttributeConfig attributeConfig = new MapAttributeConfig()
                    .setName(HazelcastIndexedSessionRepository.PRINCIPAL_NAME_ATTRIBUTE)
                    .setExtractor(PrincipalNameExtractor.class.getName());

            Config config = new Config();

            config.getMapConfig(HazelcastIndexedSessionRepository.DEFAULT_SESSION_MAP_NAME)
                    .addMapAttributeConfig(attributeConfig)
                    .addMapIndexConfig(new MapIndexConfig(
                            HazelcastIndexedSessionRepository.PRINCIPAL_NAME_ATTRIBUTE, false));

            return Hazelcast.newHazelcastInstance(config);
        }
    }



}
