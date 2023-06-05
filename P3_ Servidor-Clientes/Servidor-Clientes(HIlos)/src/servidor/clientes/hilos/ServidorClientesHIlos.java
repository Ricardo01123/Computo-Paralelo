package servidor.clientes.hilos;

import java.io.*;
import java.util.*;
import java.net.*;

//Server class
public class ServidorClientesHIlos {
    //Vector to store active clients
    static Vector<ClientHandler> clients = new Vector<>();
    //Counter for clients
    static int i = 0;

    public static void main(String [] args) throws IOException{
        int port = 6666;
        ServerSocket serverSocket = new ServerSocket(port);
        Socket socket;

        while(true){
            socket = serverSocket.accept();
            System.out.println("New client request received: " + socket);

            //Obtain input and output streams
            DataInputStream dataIS = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOS = new DataOutputStream(socket.getOutputStream());

            System.out.println("Creando un hilo para este cliente...");
            ClientHandler match = new ClientHandler(socket,"client " + i, dataIS,dataOS);
            Thread thread = new Thread(match);

            System.out.println("Añadiendo este cliente activo a la lista...");
            clients.add(match);

            thread.start();
            i++;
        }
    }
}

//ClientHandler class
class ClientHandler implements Runnable{
    Scanner reader = new Scanner(System.in);
    String name;
    DataInputStream dataIS;
    DataOutputStream dataOS;
    Socket socket;
    boolean isLoggedIn;

    //constructor
    public ClientHandler(Socket socket, String name, DataInputStream dataIS, DataOutputStream dataOS){
        this.dataIS = dataIS;
        this.dataOS = dataOS;
        this.socket = socket;
        this.name = name;
        this.isLoggedIn = true;
    }

    @Override
    public void run(){
        String received;
        while(true){
            try{
                received = dataIS.readUTF();
                System.out.println(received);
                if(received.equals("logout")){
                    this.isLoggedIn = false;
                    this.socket.close();
                    System.out.println("Cliente desconectado: " + this.name);
                    break;
                }

                //Break the string into message and client part
                StringTokenizer stringToken = new StringTokenizer(received,"/");
                if (stringToken.countTokens() != 2) {
                    try {
                        dataOS.writeUTF("Sintaxis incorrecta");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    continue;
                }

                String messageToSend = stringToken.nextToken();
                String client = stringToken.nextToken();

                //search for the client in the connected devices list
                for(ClientHandler toSearch : ServidorClientesHIlos.clients){
                    if(toSearch.name.equals(client) && toSearch.isLoggedIn == true){
                        try {
                            toSearch.dataOS.writeUTF(this.name + " : " + messageToSend);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }catch(IOException e){
                e.printStackTrace();
                try {
                    dataOS.writeUTF("Sintaxis incorrecta");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }

        try{
            this.dataIS.close();
            this.dataOS.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
