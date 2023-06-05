//Optimizado
package servidor.clientes.hilos;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {
    private static final int PORT = 6666;

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in);
             Socket socket = new Socket("192.168.100.12", PORT);
             DataInputStream dataIS = new DataInputStream(socket.getInputStream());
             DataOutputStream dataOS = new DataOutputStream(socket.getOutputStream())) {

            System.out.println("Conexión establecida con el servidor.");

            // Hilo para enviar mensajes al servidor
            Thread sendMessageThread = new Thread(() -> {
                try {
                    while (true) {
                        String message = scanner.nextLine();
                        dataOS.writeUTF(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            // Hilo para recibir mensajes del servidor
            Thread receiveMessageThread = new Thread(() -> {
                try {
                    while (true) {
                        String message = dataIS.readUTF();
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            sendMessageThread.start();
            receiveMessageThread.start();

            sendMessageThread.join();
            receiveMessageThread.join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
