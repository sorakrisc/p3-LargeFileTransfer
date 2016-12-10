import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiscoveryThread implements Runnable {
    DatagramSocket socket;
    ConcurrentSkipListSet<String> lanIP = new ConcurrentSkipListSet<>();

    @Override
    public void run() {
        try {
            //Keep a socket open to listen to all the UDP trafic that is destined for this port
            socket = new DatagramSocket(6789, InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);
            while (true) {
                System.out.println(getClass().getName() + ": Ready to receive broadcast packets!");

                //Receive a packet
                byte[] recvBuf = new byte[15000];
                DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
                socket.receive(packet);
                

                //Packet received
                System.out.println(getClass().getName() + ": Discovery packet received from: " + packet.getAddress().getHostAddress());
                System.out.println(getClass().getName() + ": Packet received; data: " + new String(packet.getData()).trim());

                //Add ip to concurrentskiplistset 10.x.x.x
                String ip = packet.getAddress().getHostAddress();
                if (ip.substring(0,3).equals("10.")) {
                    lanIP.add(packet.getAddress().getHostAddress());
                    System.out.println(getClass().getName() + ": List of IP in your LAN: " + lanIP);
                }
                //See if the packet holds the right command (message)
                String message = new String(packet.getData()).trim();
                if (message.equals("DISCOVER_SERVER_REQUEST")) {
                    byte[] sendData = "DISCOVER_SERVER_RESPONSE".getBytes();

                    //Send a response
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
                    socket.send(sendPacket);

                    System.out.println(getClass().getName() + ": Sent packet to: " + sendPacket.getAddress().getHostAddress());
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(DiscoveryThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}