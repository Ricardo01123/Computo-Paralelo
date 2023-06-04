package servidor.clientes.hilos;

import java.io.*;
import java.net.*;
import java.util.*;

// Clase ServidorClientesHIlos
public class ServidorClientesHIlos {
    // Vector para almacenar los clientes activos
    static Vector<ClientHandler> clients = new Vector<>();
    // Contador para los clientes
    static int i = 0;

    public static void main(String[] args) throws IOException {
        int port = 6666;
        ServerSocket serverSocket = new ServerSocket(port);

        System.out.println("El servidor está esperando conexiones...");

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Nueva solicitud de cliente recibida: " + socket);

            // Obtener flujos de entrada y salida
            DataInputStream dataIS = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOS = new DataOutputStream(socket.getOutputStream());

            System.out.println("Creando un nuevo controlador para este cliente...");
            ClientHandler clientHandler = new ClientHandler(socket, "Cliente " + i, dataIS, dataOS);
            Thread thread = new Thread(clientHandler);

            System.out.println("Agregando este cliente a la lista de clientes activos...");
            clients.add(clientHandler);

            thread.start();
            i++;
        }
    }
}

// Clase ClientHandler
class ClientHandler implements Runnable {
    Scanner reader = new Scanner(System.in);
    String name;
    DataInputStream dataIS;
    DataOutputStream dataOS;
    Socket socket;
    boolean isLoggedIn;

    // Constructor
    public ClientHandler(Socket socket, String name, DataInputStream dataIS, DataOutputStream dataOS) {
        this.socket = socket;
        this.name = name;
        this.dataIS = dataIS;
        this.dataOS = dataOS;
        this.isLoggedIn = true;
    }

    @Override
    public void run() {
        String received;
        while (true) {
            try {
                received = dataIS.readUTF();
                System.out.println(received);
                if (received.equals("logout")) {
                    this.isLoggedIn = false;
                    this.socket.close();
                    break;
                }
                // Separar la cadena en mensaje y cliente
                StringTokenizer stringToken = new StringTokenizer(received, "/");
                String messageToSend = stringToken.nextToken();
                String client = stringToken.nextToken();
                // Buscar el cliente en la lista de dispositivos conectados
                for (ClientHandler toSearch : ServidorClientesHIlos.clients) {
                    if (toSearch.name.equals(client) && toSearch.isLoggedIn) {
                        toSearch.dataOS.writeUTF(this.name + " : " + messageToSend);
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            this.dataIS.close();
            this.dataOS.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
