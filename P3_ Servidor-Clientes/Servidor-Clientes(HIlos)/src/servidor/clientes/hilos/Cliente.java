package servidor.clientes.hilos;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {
    static int port = 6666;

    public static void main(String[] args) throws UnknownHostException, IOException {
        Scanner reader = new Scanner(System.in);
        InetAddress ip = InetAddress.getByName("187.190.39.86"); // Reemplazar con la dirección IPv4

        // Establecer la conexión con el servidor
        Socket socket = new Socket(ip, port);

        // Obtener flujos de entrada y salida
        DataInputStream dataIS = new DataInputStream(socket.getInputStream());
        DataOutputStream dataOS = new DataOutputStream(socket.getOutputStream());

        // Hilo para enviar mensajes al servidor
        Thread sendMessage = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    // Leer el mensaje a enviar desde el usuario
                    String message = reader.nextLine();
                    try {
                        dataOS.writeUTF(message); // Enviar el mensaje al servidor
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // Hilo para leer mensajes del servidor
        Thread readMessage = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        // Leer el mensaje enviado por el servidor
                        String message = dataIS.readUTF();
                        System.out.println(message); // Mostrar el mensaje en la consola
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        sendMessage.start(); // Iniciar el hilo para enviar mensajes
        readMessage.start(); // Iniciar el hilo para leer mensajes
    }
}
