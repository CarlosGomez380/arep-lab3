package edu.escuelaing.arep.lab3;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HttpServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(getPort());
        } catch (IOException e) {
            System.err.println("Could not listen on port: "+ getPort() +".");
            System.exit(1);
        }

        boolean running = true;
        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
                System.out.println(clientSocket);
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(
                    clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            String inputLine, outputLine;
            outputLine=null;
            String firstLine=in.readLine();
            System.out.println(firstLine);
            System.out.println(firstLine);
            System.out.println(firstLine);
            //String linea[]=firstLine.split(" ");
            //String head[]= linea[1].split("/");
            //System.out.println(head.length);
            //if(firstLine.contains("/App")){
            //    if(head.length>2) {
            //        System.out.println("Estoy aqui");
            //        imprimirQuery(parseQuery(head[2]));
            //    }
            //}
            //else{
            //
            //    outputLine= lecturaStaticFile(head);
            //}
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Recibí: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }
            Path file = Paths.get("estilos.css");
            InputStream in2 = Files.newInputStream(file);
            BufferedReader reader
                    = new BufferedReader(new InputStreamReader(in2));
            String line = null;
            while ((line = reader.readLine()) != null) {
                out.println(line);
                System.out.println(line);
            }


            //out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    public static String lecturaStaticFile(String[] head) throws IOException {
        String tipo;
        String lectura;
        String outputLine;
        if(head.length==0){
            tipo= "html";
            lectura= muestraContenido("estilos.css");
        }
        else{
            lectura= muestraContenido(head[1]);
            String typeList[]= head[1].split("\\.");
            tipo=typeList[1];
        }

        outputLine = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/" + tipo + "\r\n"
                + "\r\n"
                + lectura;

        return outputLine;
    }

    public static String muestraContenido(String archivo) throws FileNotFoundException, IOException {
        String cadena;
        try {
            FileReader f = new FileReader(archivo);
            BufferedReader b = new BufferedReader(f);
            String out= "\r\n";
            while((cadena = b.readLine())!=null) {
                out= out + cadena +"\n";
            }
            b.close();
            return out;
        }catch(FileNotFoundException ex){
            FileReader f = new FileReader("estilos.css");
            BufferedReader b = new BufferedReader(f);
            String out= "\r\n";
            while((cadena = b.readLine())!=null) {
                out= out + cadena +"\n";
            }
            b.close();
            return out;
        }
    }

    /**
     * This method reads the default port as specified by the PORT variable in
     * the environment.
     *
     * Heroku provides the port automatically so you need this to run the
     * project on Heroku.
     */
    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567; //returns default port if heroku-port isn't set (i.e. on localhost)
    }

    private static Map<String, String> parseQuery(String query) {
        if( query == null) return null;
        Map<String, String> theQuery = new HashMap();
        String[] nameValuePairs = query.split("&");
        for(String nameValuePair: nameValuePairs){
            int index = nameValuePair.indexOf("=");
            if(index!=-1){
                theQuery.put(nameValuePair.substring(0, index), nameValuePair.substring(index+1));
            }
        }
        return theQuery;
    }

    private static void imprimirQuery(Map<String, String> codes){
        Iterator<Map.Entry<String, String>> i = codes.entrySet().iterator();
        while(i.hasNext()){
            String key = i.next().getKey();
            System.out.println(key+", "+codes.get(key));
        }
    }
}