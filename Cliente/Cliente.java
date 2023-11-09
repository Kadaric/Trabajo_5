import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Cliente extends Thread
{
    final int port = 3000;
    final String host = "localhost";
    private boolean exit = false;
    private boolean chatInit = false;
    private boolean socketConnected = false;
    private boolean miTurno = false;
    private Socket socketCliente;
    private DataInputStream bufferDeEntrada = null;
    private DataOutputStream bufferDeSalida = null;

    @Override
	public void run()
    {
        try
        {
            
            while (!exit) 
            {
                if(chatInit)
                {
                    System.out.println("::: Ingresando al chat");
                    this.socketCliente = new Socket(host, port);
                    bufferDeEntrada = new DataInputStream(socketCliente.getInputStream());
                    bufferDeSalida = new DataOutputStream(socketCliente.getOutputStream());
                    bufferDeSalida.flush();
                    System.out.println("::: Conectado al chat");
                    this.socketConnected=true;
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
