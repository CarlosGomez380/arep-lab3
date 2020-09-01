package edu.escuelaing.arep.lab3;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;

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
            if(firstLine.equals("/App")){

            }
            else{
                outputLine= lecturaStaticFile(firstLine, outputLine);
            }
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Recibí: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }
            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    public static String lecturaStaticFile(String firstLine, String outputLine) throws IOException {
        String linea[]=firstLine.split(" ");
        String head[]= linea[1].split("/");
        String tipo;
        String lectura;
        if(head.length==0){
            tipo= "html";
            lectura= muestraContenido("index.html");
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
}