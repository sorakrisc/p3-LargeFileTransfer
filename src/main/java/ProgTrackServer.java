import java.io.*;
import java.net.*;

public class ProgTrackServer implements Runnable{
    public String portRemover(String ipWithPort){
        return ipWithPort.substring(0,ipWithPort.lastIndexOf(":"));
    }
    public void run(){
        String clientSentence;
        try {
            ServerSocket welcomeSocket = new ServerSocket(6789);
            while (true) {
                Socket connectionSocket = welcomeSocket.accept();
                BufferedReader inFromClient =
                        new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                clientSentence = inFromClient.readLine();
                System.out.println("Received: " + clientSentence);
                String senderIp=connectionSocket.getRemoteSocketAddress().toString();
                runDiscovery.prgtrkMap.put(portRemover(senderIp),clientSentence);
                connectionSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
