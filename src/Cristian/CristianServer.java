import java.io.*;
import java.net.*;
import java.util.Date;

public class CristianServer {

    public static void main(String[] args) {
        int serverPort = 5000; // Puerto del servidor

        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            System.out.println("Servidor esperando conexiones en el puerto " + serverPort);

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
                     DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream())) {

                    // Recibir solicitud del cliente
                    String request = inputStream.readUTF();

                    if (request.equals("TIME_REQUEST")) {
                        // Enviar la hora actual al cliente
                        long serverTime = System.currentTimeMillis();
                        outputStream.writeLong(serverTime);
                        System.out.println("Enviando tiempo al cliente: " + new Date(serverTime));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
