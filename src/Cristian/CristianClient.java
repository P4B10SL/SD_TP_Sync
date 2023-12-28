import java.io.*;
import java.net.*;
import java.util.Date;

public class CristianClient {

    public static void main(String[] args) {
        String serverAddress = "127.0.0.1"; // Dirección del servidor - Cambiar por la dirección IP del servidor
        int serverPort = 5000; // Puerto del servidor

        try (Socket socket = new Socket(serverAddress, serverPort);
             DataInputStream inputStream = new DataInputStream(socket.getInputStream());
             DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())) {

            // Enviar solicitud al servidor
            outputStream.writeUTF("TIME_REQUEST");

            // Recibir la respuesta del servidor
            long serverTime = inputStream.readLong();
            Date serverDate = new Date(serverTime);

            // Calcular la diferencia entre el tiempo del servidor y el tiempo local
            long timeDifference = serverTime - System.currentTimeMillis();

            // Ajustar el reloj local
            long adjustedLocalTime = System.currentTimeMillis() + timeDifference;

            // Mostrar resultados
            System.out.println("Tiempo del servidor: " + serverDate);
            System.out.println("Diferencia de tiempo: " + timeDifference + " milisegundos");
            System.out.println("Reloj local ajustado: " + new Date(adjustedLocalTime));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
