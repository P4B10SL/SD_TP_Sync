import java.awt.image.DataBufferInt;
import java.io.DataInputStream;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        SistemaComunicaciones Coms= new SistemaComunicaciones();
        try {
            //En cliente ingresa el ip del servidor y viceversa
            DataInputStream ipConsola;
            System.out.println("Ingrese la dirección IP a la que se desea conectar:");
            ipConsola=new DataInputStream(System.in);
            String ipDestino=ipConsola.readLine();
            Coms.init(ipDestino);

            /*
            //Cliente
            String contador= "0";
            Coms.SendTCP(contador);
            while (true) {
                String recibidoTCPCliente = Coms.ReceiveTCP();
                Integer contadorRecibidoCliente = Integer.parseInt(recibidoTCPCliente);
                String contadorActualizadoCliente = Integer.toString(contadorRecibidoCliente + 1);
                Coms.SendTCP(contadorActualizadoCliente);
                System.out.println(contadorActualizadoCliente);
            }
            */
            //Servidor
            while (true) {
                System.out.println("HASTA ACA LLEGA");
                String recibidoTCPServidor = Coms.ReceiveTCP();
                System.out.println("Esto es lo que recibió: "+ recibidoTCPServidor);
                Integer contadorRecibido = Integer.parseInt(recibidoTCPServidor);
                String contadorActualizado = Integer.toString(contadorRecibido + 1);
                Coms.SendTCP(contadorActualizado);
                System.out.println(contadorActualizado);
            }
        }
        catch (IOException e)
        {
            System.out.print("Error al ingresar el mensaje");
            System.out.print(e);
        }
    }

}