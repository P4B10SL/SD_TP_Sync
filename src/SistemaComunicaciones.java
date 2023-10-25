import java.io.*;
import java.net.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SistemaComunicaciones {
    String ipDestino;
    public void SistComunicaciones()
    {}
    public void init(String destino)
    {
        ipDestino=destino;
    }
    public String Send(String mensaje){
        Mensaje nuevoMensaje= new Mensaje();
        String confirmacion;
        byte pkg_byte[] = null; //buffer para la asignacion de los datos
        try { // necesario para capturar los posibles errores
            DatagramPacket paqueteUDP; //declaracion del paquete UDP
            String host = ipDestino; //Declaración de la dirección IP destino
            String port_string = "8001";    //Se indica que se conecte al puerto 8001
            Integer port= Integer.parseInt(port_string);
            InetAddress dir_remota = InetAddress.getByName(host);
            nuevoMensaje.mensajeArrayB= nuevoMensaje.ToArrayBytes(mensaje);
            byte[] buffer = nuevoMensaje.mensajeArrayB; // retorna los bytes del string
            paqueteUDP = new DatagramPacket(buffer, buffer.length, dir_remota, port);
            //crea el socket y envía el paquete
            DatagramSocket ds = new DatagramSocket();
            ds.send(paqueteUDP);
            ds.close();
            confirmacion="Enviado";
        }
        catch(Exception e) {
            System.out.println(e);
            confirmacion="ERROR";
        }
        return confirmacion;
    }
    public String Receive() {
        byte pkg_byte[] = null; //buffer para la asignacion de los datos
        Mensaje nuevoMensaje= new Mensaje();
        String respuesta;
        int MAX_LONG = 800;
        try {
            DatagramSocket so_reciv = new DatagramSocket(8001); //Crea el DatagramSocket en el localhost y lo asigna en el puerto 80
            byte buffer[] = new byte[MAX_LONG]; //buffer solo para el DatagramPacket
            //buffer solo para el DatagramPacket
            DatagramPacket data_reciv = new DatagramPacket(buffer, MAX_LONG);
            so_reciv.receive(data_reciv); //lee los datos
            pkg_byte = new byte[data_reciv.getLength()]; //array de bytes para los datos
            pkg_byte = data_reciv.getData(); // asigna los bytes de datos
            int limite=data_reciv.getLength();
            nuevoMensaje.mensajeString=nuevoMensaje.ToMessage(pkg_byte,limite);
            respuesta=nuevoMensaje.mensajeString;
            so_reciv.close();

        } catch (Exception e) {
            System.out.println(e);
            respuesta="ERROR";
        }
        return respuesta;
    }
    public String SendTCP(String mensaje){
        String confirmacion;
        // Declaracion del socket
        Socket so_check_port;
        // Declaracion del InputStream para el socket
        DataInputStream data_in_socket;
        // Declaracion del InputStream para la linea de comandos
        DataInputStream data_in_consola;
        String linea;
        //Declaracion del OutStream
        PrintStream data_out_socket;
        // Declaracion del nombre del host servidor daytime
        try{
            String host = ipDestino;
            String port_string = "732";
            Integer port= Integer.parseInt(port_string);
            //crea el socket y se intenta conectar
            so_check_port = new Socket(host,port);

            // crea el DataStream con InputStream del socket
            data_in_socket = new DataInputStream(so_check_port.getInputStream());
            // crea el PrintStream con el OutputStream del socket
            data_out_socket = new PrintStream(so_check_port.getOutputStream());
            //System.out.print("YOU: ");
            // crea el Stream de entrada
            //lee una linea desde la consola
            linea = mensaje;
            // envia la linea el Strem del Socket
            data_out_socket.println(linea);
            // lee los datos del InputSteeam del socket y
            //los envia a la salida estandar
            confirmacion="Enviado";
            //cierra la conexion
            so_check_port.close();
        } // end try
        catch (UnknownHostException e){
            // si hubo error lo envia a la salida por defecto
            System.out.println(e);
            confirmacion="ERROR";
        } // end catch

        catch (IOException e) {
            // si hubo error lo envia a la salida por defecto
            System.out.println(e);
            confirmacion="ERROR";
        }
        return confirmacion;
    }
    public String ReceiveTCP(){
        ServerSocket serverSocket;
        PrintStream data_out_conex;
        DataInputStream data_in_consola;
        DataInputStream texto_console;
        String confirmacion;
        try  {
            String portString = "732";
            Integer port= Integer.parseInt(portString);
            serverSocket = new ServerSocket(port);
            Socket clientSocket = serverSocket.accept();
            System.out.println("EN EL RECEIVE");
            // Creacion de los Input y Output Streams del cliente
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            confirmacion=input.readLine();
            // Cierre del socket cliente
            clientSocket.close();
            serverSocket.close();
            System.out.println("Cliente desconectado");
            //}
        } catch (IOException e) {
            System.out.println(e);
            confirmacion="ERROR"+e.toString();
        }
        return confirmacion;
    }

    public void LamportServidor()
    {
        SistemaComunicaciones Coms= new SistemaComunicaciones();
        try {
            //En cliente ingresa el ip del servidor y viceversa
            DataInputStream ipConsola;
            System.out.println("Ingrese la dirección IP a la que se desea conectar:");
            ipConsola=new DataInputStream(System.in);
            String ipDestino=ipConsola.readLine();
            Coms.init(ipDestino);
            //Servidor
            while (true) {
                String recibidoTCPServidor = Coms.ReceiveTCP();
                System.out.println("Timestamp recibido: "+ recibidoTCPServidor);
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
    public void LamportCliente()
    {
        SistemaComunicaciones Coms= new SistemaComunicaciones();
        try {
            //En cliente ingresa el ip del servidor y viceversa
            DataInputStream ipConsola;
            System.out.println("Ingrese la dirección IP a la que se desea conectar:");
            ipConsola=new DataInputStream(System.in);
            String ipDestino=ipConsola.readLine();
            Coms.init(ipDestino);
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
        }
        catch (IOException e)
        {
            System.out.print("Error al ingresar el mensaje");
            System.out.print(e);
        }

    }
    public void BerkeleyCliente()
    {
        SistemaComunicaciones Coms= new SistemaComunicaciones();
        try {
            //En cliente ingresa el ip del servidor y viceversa
            DataInputStream ipConsola;
            System.out.println("Ingrese la dirección IP a la que se desea conectar:");
            ipConsola=new DataInputStream(System.in);
            String ipDestino=ipConsola.readLine();
            LocalDateTime now = LocalDateTime.now();
            // Formatea el timestamp en formato de fecha y hora
            String timestampFormateado = now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
            System.out.println(timestampFormateado);
            Coms.init(ipDestino);
            //Cliente
            Coms.SendTCP(timestampFormateado);
            while (true) {
                String diferenciaRecibida = Coms.ReceiveTCP();
                Integer diferenciaFormateada= Integer.parseInt(diferenciaRecibida);
                String timestampFormateadoCorregido;
               if (diferenciaFormateada >=0) {
                   // Suma el parsedDateTime a now
                   LocalDateTime corregido= now.plusNanos(diferenciaFormateada * 1_000_000);
                    timestampFormateadoCorregido = corregido.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
               } else{
                   LocalDateTime corregido= now.minusNanos(diferenciaFormateada * 1_000_000);
                    timestampFormateadoCorregido = corregido.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
               }
                Coms.SendTCP(timestampFormateadoCorregido);
                System.out.println(timestampFormateadoCorregido);
            }
        }
        catch (IOException e)
        {
            System.out.print("Error al ingresar el mensaje");
            System.out.print(e);
        }

    }
    public void BerkeleyServidor()
    {
        SistemaComunicaciones Coms= new SistemaComunicaciones();
        try {
            //En cliente ingresa el ip del servidor y viceversa
            DataInputStream ipConsola;
            System.out.println("Ingrese la dirección IP a la que se desea conectar:");
            ipConsola=new DataInputStream(System.in);
            String ipDestino=ipConsola.readLine();
            LocalDateTime now = LocalDateTime.now();
            // Formatea el timestamp en formato de fecha y hora
            String timestampFormateado = now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
            System.out.println(timestampFormateado);
            Coms.init(ipDestino);
            //Cliente
            Coms.SendTCP(timestampFormateado);
            while (true) {
                String diferenciaRecibida = Coms.ReceiveTCP();
                Integer diferenciaFormateada= Integer.parseInt(diferenciaRecibida);
                String timestampFormateadoCorregido;
                if (diferenciaFormateada >=0) {
                    // Suma el parsedDateTime a now
                    LocalDateTime corregido= now.plusNanos(diferenciaFormateada * 1_000_000);
                    timestampFormateadoCorregido = corregido.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
                } else{
                    LocalDateTime corregido= now.minusNanos(diferenciaFormateada * 1_000_000);
                    timestampFormateadoCorregido = corregido.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
                }
                Coms.SendTCP(timestampFormateadoCorregido);
                System.out.println(timestampFormateadoCorregido);
            }
        }
        catch (IOException e)
        {
            System.out.print("Error al ingresar el mensaje");
            System.out.print(e);
        }
    }
    public String ReceiveTCP_Berkeley(){
        ServerSocket serverSocket;
        PrintStream data_out_conex;
        DataInputStream data_in_consola;
        DataInputStream texto_console;
        String confirmacion;
        try  {
            String portString = "732";
            Integer port= Integer.parseInt(portString);
            serverSocket = new ServerSocket(port);
            Socket clientSocket = serverSocket.accept();
            System.out.println("EN EL RECEIVE");
            // Creacion de los Input y Output Streams del cliente
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            //confirmacion=input.readLine();
            confirmacion= clientSocket.getInetAddress().toString();
            // Cierre del socket cliente
            clientSocket.close();
            serverSocket.close();
            System.out.println("Cliente desconectado");
            //}
        } catch (IOException e) {
            System.out.println(e);
            confirmacion="ERROR"+e.toString();
        }
        return confirmacion;
    }


    public static void CristianCliente() {
        SistemaComunicaciones Coms = new SistemaComunicaciones();
        try {
            //En cliente ingresa el ip del servidor y viceversa
            DataInputStream ipConsola;
            System.out.println("Ingrese LA dirección IP a la que se desea conectar:");
            ipConsola = new DataInputStream(System.in);
            String ipDestino = ipConsola.readLine();
            Coms.init(ipDestino);
            String mensaje = "Sincronizar";
            //Acá dice usa el SendTCP, para mi sería un receiveTCP pero así decía en Bard
            String respuesta = Coms.SendTCP(mensaje);

            // 2. El servidor responde con su tiempo actual.
            if (respuesta.equals("Enviado")) {
                // 3. El cliente calcula la diferencia entre su tiempo actual y el tiempo del servidor.
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime tiempoActual = LocalDateTime.now();
                LocalDateTime tiempoServidor = LocalDateTime.parse(respuesta, formatter);
                Duration diferencia = Duration.between(tiempoActual, tiempoServidor);

                // 4. El cliente ajusta su reloj en consecuencia.
                tiempoActual = tiempoActual.plus(diferencia);
                System.out.println("Tiempo actualizado: " + tiempoActual);
            } else {
                System.out.println("Error al sincronizar con el servidor.");
            }
        }
        catch (IOException e)
        {
            System.out.print("Error al ingresar el mensaje");
            System.out.print(e);
        }

    }

    public static void CristianServidor()
    {
        try{
            // 1. El servidor escucha las solicitudes de sincronización de los clientes.
            ServerSocket serverSocket = new ServerSocket(732);

            // 2. El servidor responde con su tiempo actual.
            while (true) {
                //Acá creo que hay q usar el receive pq le puse el send, sacqué el socket.
                Socket socket = serverSocket.accept();
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String mensaje = input.readLine();

                if (mensaje.equals("Sincronizar")) {
                    // Obtener el tiempo actual del servidor.
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime tiempoActual = LocalDateTime.now();
                    String respuesta = tiempoActual.format(formatter);

                    // Enviar la respuesta al cliente.
                    //No se usaría el sendTCP? o al ser socket es distinto y ya lo recive
                    PrintStream output = new PrintStream(socket.getOutputStream());
                    output.println(respuesta);
                }

                socket.close();
            }
        }
        catch (IOException e)
        {
            System.out.print("Error al ingresar el mensaje");
            System.out.print(e);
        }
    }

}
