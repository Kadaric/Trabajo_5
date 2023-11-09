import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Servidor extends Thread
{
    final int port = 3000;
    final String host = "localhost";
    private boolean exit = false;
    private boolean chatInit = false;
    private boolean socketConnected = false;
    private boolean miTurno = false;
    private Socket socketCliente;
    private ServerSocket socketServidor;
    private DataInputStream bufferDeEntrada = null;
    private DataOutputStream bufferDeSalida = null;

    @Override
	public void run()
    {
        try
        {
            this.socketServidor=new ServerSocket(port);
            this.socketCliente = new Socket();
            while (!exit) 
            {
                if(chatInit)
                {
                    System.out.println("::: Esperando al otro usuario");
                    socketCliente = socketServidor.accept();
                    bufferDeEntrada = new DataInputStream(socketCliente.getInputStream());
                    bufferDeSalida = new DataOutputStream(socketCliente.getOutputStream());
                    bufferDeSalida.flush();
                    System.out.println("::: Se ha conectado un usuario");
                    this.socketConnected=true;
                    this.miTurno=true;
                    Scanner entradaChat = new Scanner (System.in); 
                    while (socketConnected)
                    { 
                        if(miTurno)
                        {
                            System.out.print("[Tu]: ");
                            String msgEnviar = entradaChat.nextLine();
                            enviarMensaje(msgEnviar);
                            miTurno=false;
                        }
                        else
                        {
                            System.out.println("::: [Usuario] está escribiendo..");
                            recibirDatos();
                            miTurno=true;
                        }
                    }
                }
            }
        }
        catch(Exception ex)
        {
            System.out.println("fallo");
        }
    }

    public boolean iniciarChat()
    {
        
        while(!socketConnected)
        {
            this.chatInit = true;
        }

        return true;
    }

    public void enviarMensaje(String sMensaje) 
    {
        try 
        {
            bufferDeSalida.writeUTF(sMensaje);
            bufferDeSalida.flush();
        } 
        catch (IOException e) 
        {
            System.out.println("Error en enviar(): " + e.getMessage());
        }
    }

    public void recibirDatos() {
        String sMsjRecibido = "";
        try 
        {
            sMsjRecibido = (String) bufferDeEntrada.readUTF();   
            System.out.println("[Usuario] : "+sMsjRecibido);
        } 
        catch (IOException e)
        {
           cerrarChat();
        }
    }

    public void cerrarChat() 
    {
        try 
        {
            bufferDeEntrada.close();
            bufferDeSalida.close();
            socketCliente.close();
            socketConnected=false;
            exit=true;
        } 
        catch (IOException e) 
        {
          System.out.println("Error cerrando chat:  " + e.getMessage());
        } 
        finally 
        {
            System.out.println("Chat finalizado");
        }
    }
}
