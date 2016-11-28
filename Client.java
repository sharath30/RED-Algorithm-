import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
 
public class Client
{
 
    private static Socket socket;
	
 
    public static void main(String args[])
    {
        try
        {
            String host = "localhost";
            int port = 25000;
		int packets[] = new int[5];
	int delay = 0;
            InetAddress address = InetAddress.getByName(host);
            socket = new Socket(address, port);
		
		for(int i=0;i<5;i++){
		System.out.println("Enter the packet size : \n");
		Scanner s = new Scanner(System.in);
		packets[i] = s.nextInt();

 
            //Send the message to the server
            OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
 
         String number = String.valueOf(packets[i]);
		
 		if(packets[i]<=100){
		delay = 2;
		}
		else if(packets[i]>100&&packets[i]<=500){
		delay = 3;
		}
		else if(packets[i]>500){
		delay = 5;
		}
            String sendMessage = number + "\n";
            bw.write(sendMessage);
            bw.flush();
            System.out.println("Packet sent to the server in terms of bytes : "+sendMessage);
		System.out.println("Delay is : "+delay+"ms");
		delay=delay+5;
 
            //Get the return message from the server
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String message = br.readLine();
            System.out.println(message);
		}
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
        finally
        {
            //Closing the socket
            try
            {
                socket.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
