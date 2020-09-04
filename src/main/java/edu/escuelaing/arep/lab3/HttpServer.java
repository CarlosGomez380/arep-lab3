package edu.escuelaing.arep.lab3;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.*;
import java.io.*;
import java.util.*;

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
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String firstLine=in.readLine();
            firstLine= establecerConexion(firstLine,serverSocket);
            System.out.println(firstLine);
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Recibí: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }
            leerFormato(firstLine,clientSocket);
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    /**
     * Metodo encargado de devolver la primera linea no nula de parte del cliente
     * @param firstLine, primera linea del inputStream
     * @param serverSocket, Socket del servidor
     * @return Primera linea no vacia del inputStream
     * @throws IOException
     */
    public static String establecerConexion(String firstLine,ServerSocket serverSocket) throws IOException {
        while(firstLine==null){
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            firstLine=in.readLine();
        }
        return firstLine;
    }

    /**
     * Metodo encargado de decidir archivos estaticos o un query
     * @param firstLine, Primera linea del inputStream
     * @param clientSocket, Socket sel cliente
     * @throws IOException
     */
    public static void leerFormato(String firstLine,Socket clientSocket) throws IOException {
        String linea[]=firstLine.split(" ");
        String head[]= linea[1].split("/");
        if(firstLine.contains("/App")){
            if(head.length>2) {
                System.out.println("Estoy aqui");
                imprimirQuery(parseQuery(head[2]));
            }
        }
        else{
            lecturaStaticFile(head,clientSocket);
        }
    }

    /**
     * Metodo encargado de leer los archivos estaticos dependiendo de su extensión.
     * @param head, Nombre del archivo a leer
     * @param clientSocket, Socket del cliente
     * @throws IOException
     */
    public static void lecturaStaticFile(String[] head, Socket clientSocket) throws IOException {
        String tipo;
        String lectura;
        if(head.length==0){
            tipo= "css";
            lectura= muestraContenido("estilos.css");
            outputFile(lectura,tipo,clientSocket);
        }
        else{
            String typeList[]= head[1].split("\\.");
            tipo=typeList[1];
            if(tipo.equals("jpg")){
                outputImagen(head[1],clientSocket);
            }
            else{
                lectura= muestraContenido(head[1]);
                outputFile(lectura,tipo,clientSocket);
            }
        }
    }

    /**
     * Metodo encargado de leer un archivo especifico
     * @param archivo, Nombre del archivo
     * @return Cadena con el contenido del archivo
     * @throws FileNotFoundException
     * @throws IOException
     */
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
     * Metodo encargado de mostrar imagenes jpg en al cliente
     * @param file, Nombre del archivo jpg
     * @param clientSocket, Socket del cliente
     * @throws IOException
     */
    public static void outputImagen(String file, Socket clientSocket) throws IOException {
        BufferedImage image = ImageIO.read(new File(file));
        ByteArrayOutputStream ArrBytes = new ByteArrayOutputStream();
        OutputStream outputStream=clientSocket.getOutputStream();
        DataOutputStream out = new DataOutputStream(outputStream);
        ImageIO.write(image, "JPG", ArrBytes);
        out.writeBytes("HTTP/1.1 200 OK \r\n"
                + "Content-Type: image/jpg \r\n"
                + "\r\n");
        out.write(ArrBytes.toByteArray());
        out.close();
    }

    /**
     * Metodo encargado de mostrar archivos diferentes a imagenes
     * @param lectura, Cadena que muestra como leer el archivo por el navegador
     * @param tipo, Extensión del archivo
     * @param clientSocket, Socket del cliente
     * @throws IOException
     */
    public static void outputFile(String lectura,String tipo, Socket clientSocket) throws IOException {
        PrintWriter out = new PrintWriter(
                clientSocket.getOutputStream(), true);
        String outputLine= "HTTP/1.1 200 OK\r\n"
                            + "Content-Type: text/" + tipo + "\r\n"
                            + "\r\n"
                            +lectura;
        out.println(outputLine);
        out.close();
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