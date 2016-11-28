
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
 
public class Server
{
 
    private static Socket socket;
 
    public static void main(String[] args)
    {
        try
        {
 
		int packets[] = new int[5];
		int packets2[] = new int[5];
		int avg = 0,minthr = 50,maxthr = 500;
		int p,maxp = 1,count = -1,temp;	
		int k = 0;
		int sum=0;
	
            int port = 25000;
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server Started and listening to the port 25000");
 
            //Server is running always. This is done using this while(true) loop
            while(true)
            {
	
                //Reading the message from the client
                socket = serverSocket.accept();
		for(int i=0;i<5;i++){
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String number = br.readLine(); 
                System.out.println("Packet received from client in terms of bytes is "+number);
 
                String returnMessage = " ";
                try
                {
                    int size = Integer.parseInt(number);
                   packets[i] = size;
			avg = avg+packets[i];
			if(avg<=minthr){
                    returnMessage = String.valueOf(packets[i]) + " bytes of packet gets queued with queue size "+String.valueOf(avg)+"\n";
			}
			if((minthr<avg)&&(avg<=maxthr)){
	                count++;
	                temp = maxp*(avg-minthr)/(maxthr-minthr);
	                p = temp/(1-(count*temp));
	               if(p!=1){
			 returnMessage = String.valueOf(packets[i]) + " bytes of packet gets queued with queue size "+String.valueOf(avg)+"\n";
			}
			else{
			returnMessage = "Packet gets discarded due to high probability rate but it will be stored in other bucket with size  "+String.valueOf(packets[i])+"\n";
			avg = avg - packets[i];
			packets2[k] = packets[i];
			sum = sum+packets[i];
			k++; 
			}
			count = -1;
			}	
			else if(maxthr<avg){
			returnMessage = "Packet gets discarded due to congestion that happened but it will be stored in other bucket with size "+String.valueOf(packets[i])+"\n";
			avg = avg - packets[i];
			packets2[k] = packets[i];
			sum = sum+packets[i];
			k++;
			}
			if(sum>maxthr){
			returnMessage = "Stop sending packets as the channel is busy "+"\n";
			}
                }
                catch(NumberFormatException e)
                {
                    //Input was not a number. Sending proper message back to client.
                    returnMessage = "Please send a proper number\n";
                }
 
                //Sending the response back to the client.
                OutputStream os = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);
                bw.write(returnMessage);
                System.out.println(returnMessage);
                bw.flush();
		}
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                socket.close();
            }
            catch(Exception e){}
        }
    }
}
