package edu.escuelaing.arep.lab3;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class Prueba {

    public static void muestraContenido(String archivo) throws FileNotFoundException, IOException {
        String cadena;
        FileReader f = new FileReader(archivo);
        BufferedReader b = new BufferedReader(f);
        String out= "\r\n";
        while((cadena = b.readLine())!=null) {
            out= out + cadena +"\n";
            System.out.println(cadena);
        }
        b.close();
        System.out.println("---------------------------------");
        System.out.println(out);
    }

    public static void main(String[] args) throws IOException {
        muestraContenido2("panda.jpg");
    }

    public static void muestraContenido2(String archivo) throws FileNotFoundException, IOException {
        FileReader f = new FileReader(archivo);
        BufferedImage b = ImageIO.read(new File(archivo));
        System.out.println(b);
    }

}