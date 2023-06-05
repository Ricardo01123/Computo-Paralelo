package servidor.clientes.hilos;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {
    static int port = 6666;

    public static void main(String[] args) throws UnknownHostException, IOException {
        Scanner reader = new Scanner(System.in);
        InetAddress ip = InetAddress.getByName("192.168.100.12"); // Cambiar la IP del servidor
        Socket socket = new Socket(ip, port);

        DataInputStream dataIS = new DataInputStream(socket.getInputStream());
        DataOutputStream dataOS = new DataOutputStream(socket.getOutputStream());

        // Hilo para enviar mensajes al servidor
        Thread sendMessage = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    // Leer el mensaje a enviar
                    String message = reader.nextLine();
                    try {
                        dataOS.writeUTF(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // Hilo para recibir mensajes del servidor
        Thread readMessage = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        // Leer el mensaje enviado a este cliente desde el servidor
                        String message = dataIS.readUTF();
                        System.out.println(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        sendMessage.start();
        readMessage.start();
    }
}