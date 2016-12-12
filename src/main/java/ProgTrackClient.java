import java.io.*;
import java.net.*;

public class ProgTrackClient implements Runnable{
    InetAddress masterInetAddr;
    String progress;
    public ProgTrackClient(InetAddress Inetad, String prgrs){
        masterInetAddr = Inetad;
        progress =prgrs;
    }
    public void run(){
        try {
            Socket clientSocket = new Socket(masterInetAddr.getHostAddress(), 6789);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            outToServer.writeBytes(progress);//  + '\n'
            clientSocket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// TODO: if there is more than one interface?