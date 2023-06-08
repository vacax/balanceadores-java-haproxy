package org.javadominicano.aws;

import io.javalin.Javalin;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.session.*;

import java.io.File;

public class Main {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        //
        int puerto = 7000;
        if(args.length>=1){
            puerto = Integer.parseInt(args[0]);
        }
        System.out.println("Iniciando la aplicación en el puerto: "+puerto);

        //subiendo el servidor.
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/publico");
        });

        //Configurando el manejador de sesion.
        //app.config.sessionHandler(()->Main.fileSessionHandler(app.server().server()));

        //iniciando el servidor
        app.start(puerto);

        /**
         * Configuración de los Endpoint.
         */

        //respuesta simple
        app.get("/ejemplo-peticion", ctx -> {
           ctx.html("<h3>Servidor escuchando en el puerto: "+app.port()+"</h3>");
        });

        //Ejemplo de contador en sesiones.
        app.get("/contador-sesion", ctx -> {
            Integer contador = ctx.sessionAttribute("contador");
            if(contador == null){
                contador = 0;
            }
            contador++;
            ctx.sessionAttribute("contador", contador);
            //
            String idSesion = ctx.req().getSession().getId();
            //
            ctx.html(String.format("<h3>Usted ha visitado %d veces - Puerto: %d - ID Sesion: %s </h3>", contador, app.port(), idSesion));
        });

    }

    /**
     * Implementación simple para cambiar la forma la manera de persistir
     * las sesiones por defecto es en RAM para Jetty.
     * Código desde: https://javalin.io/tutorials/jetty-session-handling
     * @return
     */
    public static SessionHandler fileSessionHandler(Server server) {
        SessionHandler sessionHandler = new SessionHandler();
        SessionCache sessionCache = new DefaultSessionCache(sessionHandler);
        sessionCache.setSessionDataStore(fileSessionDataStore());
        sessionHandler.setSessionCache(sessionCache);
        sessionHandler.setHttpOnly(true);
        // make additional changes to your SessionHandler here
        return sessionHandler;
    }

    /**
     * Guardando la sesiones en un archivo local, puede ser modificado para guardar en
     * base de datos, servidor Grid Memory.
     * Código desde: https://javalin.io/tutorials/jetty-session-handling
     * @return
     */
    private static FileSessionDataStore fileSessionDataStore() {
        FileSessionDataStore fileSessionDataStore = new FileSessionDataStore();
        File baseDir = new File(System.getProperty("java.io.tmpdir"));
        File storeDir = new File(baseDir, "javalin-session-store");
        storeDir.mkdir();
        fileSessionDataStore.setStoreDir(storeDir);
        return fileSessionDataStore;
    }
}
