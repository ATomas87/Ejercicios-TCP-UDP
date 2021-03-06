package ejercicio2;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientLlista extends Thread {
    String hostname;
    int port;
    boolean continueConnected;
    Llista ret = null;
    String nombre;
    List<Integer> numberList;
    int numero = 0;
    Scanner sc = new Scanner(System.in);
    Socket socket;
    InputStream in;
    ObjectInputStream oiStream;

    OutputStream out;
    ObjectOutputStream ooStream;

    public ClientLlista(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        continueConnected = true;
    }

    public void run() {
        numberList = new ArrayList<>();
        System.out.println("Introduce tu nombre:");
        nombre = sc.nextLine();
        System.out.print("Hola " + nombre + ". ");

        do {
            System.out.println("Introduce un número (introduce 0 para terminar):");
            numero = sc.nextInt();
            if (numero > 0) numberList.add(numero);
        } while (numero > 0);
        ret = new Llista(nombre, numberList);

        try {
            socket = new Socket(InetAddress.getByName(hostname), port);
            out = socket.getOutputStream();
            ooStream = new ObjectOutputStream(out);
            ooStream.writeObject(ret);

            in = socket.getInputStream();
            oiStream = new ObjectInputStream(in);
            ret = (Llista) oiStream.readObject();
            getRequest(ret);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    private void getRequest(Llista serverData) {

        System.out.print(serverData.getNom() + ", esta es la lista ordenada con los números que has enviado: ");
        serverData.getNumberList().forEach(integer -> System.out.print(integer + ", "));
        System.out.println();
        System.out.println("¡Hasta luego!");

    }


    public static void main(String[] args) {
        ClientLlista clientLlista = new ClientLlista("localhost", 5557);
        clientLlista.run();
    }
}
