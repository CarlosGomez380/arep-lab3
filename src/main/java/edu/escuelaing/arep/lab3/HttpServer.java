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
            serverSocket = new ServerSocket(36000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
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

            String firstLine=in.readLine();

            System.out.println(firstLine);

            String linea[]=firstLine.split(" ");

            String head[]= linea[1].split("/");

            String lectura= muestraContenido(head[1]);

            String typeList[]= head[1].split("\\.");

            while ((inputLine = in.readLine()) != null) {
                System.out.println("Recibí: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }

            outputLine = "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: text/" + typeList[1] + "\r\n"
                    + lectura;



            int i;
            FileInputStream fis = new FileInputStream ("panda1.jpg");
            DataOutputStream os = new DataOutputStream(clientSocket.getOutputStream());
            while ((i = fis.read()) > -1){
                os.write(i);
            }
            fis.close();
            os.close();

            //out.println(outputLine);

            out.close();
            in.close();

            clientSocket.close();
        }
        serverSocket.close();
    }

    public static String muestraContenido(String archivo) throws FileNotFoundException, IOException {
        String cadena;
        FileReader f = new FileReader(archivo);
        BufferedReader b = new BufferedReader(f);
        String out= "\r\n";
        while((cadena = b.readLine())!=null) {
            out= out + cadena +"\n";
        }
        b.close();
        return out;
    }
}