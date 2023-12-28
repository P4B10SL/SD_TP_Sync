package Berkeley;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
    public static void main(String[] args) {
List<Integer> timeDifferences = new ArrayList<>();
    ServerSocket serverSocket = null;
    String ipDestino = null; // Declare ipDestino here

    try {
        // Create a ServerSocket
        serverSocket = new ServerSocket(732); // Use any available port

        // Wait for a client to connect
        Socket clientSocket = serverSocket.accept();

        // Get the client's IP address
        ipDestino = clientSocket.getInetAddress().getHostAddress();

        // Create streams for sending and receiving data
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        LocalDateTime now = LocalDateTime.now();
        // Formatea el timestamp en formato de fecha y hora
        String timestampFormateado = now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        System.out.println(timestampFormateado);

        // Send the timestamp to the client
        out.println(timestampFormateado);

        // Receive timestamps from the client
        for (String line = in.readLine(); line != null; line = in.readLine()) {
            LocalDateTime timeReceivedFormated = LocalDateTime.parse(line, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
            Duration duration = Duration.between(now, timeReceivedFormated);
            long difference = duration.toNanos() / 1_000_000;
            timeDifferences.add((int) difference);
        }
    } catch (IOException e) {
        System.out.print("Error al ingresar el mensaje");
        System.out.print(e);
    } finally {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.out.println("Error al cerrar el ServerSocket");
                System.out.print(e);
            }
        }
    }
    // Calculate average time difference
    int sum = 0;
    for (int diff : timeDifferences) {
        sum += diff;
    }
    int averageDifference = sum / timeDifferences.size();

    // Send the average time difference to all computers
    if (ipDestino != null) { // Check if ipDestino is not null
        try (Socket clientSocket = new Socket(ipDestino, 1234);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            out.println(averageDifference);
        } catch (IOException e) {
            System.out.println("Error al enviar la diferencia de tiempo promedio");
            System.out.print(e);
        }
    }
    }
}
