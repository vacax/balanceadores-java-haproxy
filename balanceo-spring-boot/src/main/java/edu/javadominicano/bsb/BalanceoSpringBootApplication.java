package edu.javadominicano.bsb;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.session.MapSession;
import org.springframework.session.hazelcast.HazelcastIndexedSessionRepository;
import org.springframework.session.hazelcast.HazelcastSessionSerializer;
import org.springframework.session.hazelcast.config.annotation.web.http.EnableHazelcastHttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.*;

@SpringBootApplication
@EnableHazelcastHttpSession
public class BalanceoSpringBootApplication {

    private final static String SESSIONS_MAP_NAME = "EJEMPLO-HAZELCAST";

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
            Config config = new Config();
            config.setClusterName("spring-session-cluster");

            // Add this attribute to be able to query sessions by their PRINCIPAL_NAME_ATTRIBUTE's
            AttributeConfig attributeConfig = new AttributeConfig()
                    .setName(HazelcastIndexedSessionRepository.PRINCIPAL_NAME_ATTRIBUTE)
                    .setExtractorClassName(HazelcastIndexedSessionRepository.class.getName());

            // Configure the sessions map
            config.getMapConfig(SESSIONS_MAP_NAME)
                    .addAttributeConfig(attributeConfig).addIndexConfig(
                            new IndexConfig(IndexType.HASH, HazelcastIndexedSessionRepository.PRINCIPAL_NAME_ATTRIBUTE));

            // Use custom serializer to de/serialize sessions faster. This is optional.
            SerializerConfig serializerConfig = new SerializerConfig();
            serializerConfig.setImplementation(new HazelcastSessionSerializer()).setTypeClass(MapSession.class);
            config.getSerializationConfig().addSerializerConfig(serializerConfig);

            return Hazelcast.newHazelcastInstance(config);
        }
    }



}
