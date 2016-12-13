import java.io.*;
import java.net.Socket;

public class WorkerRunnable implements Runnable{

    protected Socket clientSocket = null;
    protected String serverText   = null;

    public WorkerRunnable(Socket clientSocket, String serverText) {
        this.clientSocket = clientSocket;
        this.serverText   = serverText;
    }
    public String portRemover(String ipWithPort){
        return ipWithPort.substring(0,ipWithPort.lastIndexOf(":"));
    }
    public void run() {
        try {
            InputStream input  = clientSocket.getInputStream();
            BufferedReader inFromClient =
                    new BufferedReader(new InputStreamReader(input));
            String clientSentence = inFromClient.readLine();
            String senderIp=clientSocket.getRemoteSocketAddress().toString();
            System.out.println(portRemover(senderIp)+": "+clientSentence);
            input.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}