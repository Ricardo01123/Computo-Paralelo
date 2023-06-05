// Practica 5 - Varios clientes, Varios Servidores
package servidor.clientes.hilos;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese la dirección IP del servidor: ");
        String serverIP = scanner.nextLine();

        System.out.print("Ingrese el número de puerto del servidor: ");
        int serverPort = scanner.nextInt();

        try {
            Socket socket = new Socket(serverIP, serverPort);

            DataInputStream dataIS = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOS = new DataOutputStream(socket.getOutputStream());

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
        } catch (UnknownHostException e) {
            System.out.println("Error: Dirección IP del servidor no válida.");
        } catch (IOException e) {
            System.out.println("Error al conectar con el servidor: " + e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
