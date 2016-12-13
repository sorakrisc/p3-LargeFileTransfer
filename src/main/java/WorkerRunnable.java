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
            OutputStream output = clientSocket.getOutputStream();
//            long time = System.currentTimeMillis();
//            output.write(("HTTP/1.1 200 OK\n\nWorkerRunnable: " +
//                    this.serverText + " - " +
//                    time +
//                    "").getBytes());


            BufferedReader inFromClient =
                    new BufferedReader(new InputStreamReader(input));
//            DataOutputStream outToClient = new DataOutputStream(output);
            String clientSentence = inFromClient.readLine();
            String senderIp=clientSocket.getRemoteSocketAddress().toString();
            System.out.println(portRemover(senderIp)+": "+clientSentence);
//            runDiscovery.prgtrkMap.put(portRemover(senderIp),clientSentence);
//            output.close();
            input.close();

        } catch (IOException e) {
            //report exception somewhere.
            e.printStackTrace();
        }
    }
}