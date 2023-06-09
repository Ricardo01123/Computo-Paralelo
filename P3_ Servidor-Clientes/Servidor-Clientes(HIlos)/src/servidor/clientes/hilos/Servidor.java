package servidor.clientes.hilos;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Servidor {
    private static final int PORT = 6666;
    protected static final Logger logger = Logger.getLogger(Servidor.class.getName());
    private static CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        configureLogger();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            logger.info("Servidor iniciado. Esperando clientes...");

            while (true) {
                Socket socket = serverSocket.accept();
                logger.info("Nuevo cliente conectado: " + socket);

                DataInputStream dataIS = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOS = new DataOutputStream(socket.getOutputStream());

                ClientHandler clientHandler = new ClientHandler(socket, "client " + clients.size(), dataIS, dataOS);
                clients.add(clientHandler);

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error en el servidor", e);
        }
    }

    public static void broadcastMessage(String message) {
        logger.info("Enviando mensaje a todos los clientes: " + message);
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    public static void removeClient(ClientHandler client) {
        clients.remove(client);
        logger.info("Cliente removido: " + client.getName());
    }

    private static void configureLogger() {
        try {
            FileHandler fileHandler = new FileHandler("server_logs.log", true);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error al configurar el archivo de registro", e);
        }
    }
}

class ClientHandler implements Runnable {
    private static final String LOGOUT_COMMAND = "logout";

    private final Socket socket;
    private final String name;
    private final DataInputStream dataIS;
    private final DataOutputStream dataOS;
    private boolean isLoggedIn;

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
        try {
            while (isLoggedIn) {
                received = dataIS.readUTF();
                Servidor.logger.info("Mensaje recibido de " + name + ": " + received);

                if (received.equals(LOGOUT_COMMAND)) {
                    isLoggedIn = false;
                    socket.close();
                    Servidor.removeClient(this);
                    Servidor.logger.info("Cliente desconectado: " + name);
                } else {
                    // Procesar el mensaje recibido usando el algoritmo de fallas bizantinas
                    String processedMessage = processMessage(received);
                    Servidor.broadcastMessage(name + ": " + processedMessage);
                }
            }

            dataIS.close();
            dataOS.close();
        } catch (IOException e) {
            Servidor.logger.log(Level.SEVERE, "Error en el cliente " + name, e);
        }
    }

    public void sendMessage(String message) {
        try {
            dataOS.writeUTF(message);
            dataOS.flush();
        } catch (IOException e) {
            Servidor.logger.log(Level.SEVERE, "Error al enviar mensaje a " + name, e);
        }
    }

    public String getName() {
        return name;
    }

    private String processMessage(String message) {
        // Aquí puedes implementar tu lógica para el algoritmo de fallas bizantinas
        // Puedes simular la introducción de fallas en la comunicación o el procesamiento de mensajes
        // y retornar el mensaje procesado o modificado según la lógica que desees implementar.

        // Por simplicidad, en este ejemplo, simplemente retornamos el mensaje original sin modificar.
        return message;
    }
}
