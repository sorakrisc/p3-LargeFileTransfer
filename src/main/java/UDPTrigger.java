import java.io.IOException;
import java.net.*;
import java.util.Enumeration;


public class UDPTrigger implements Runnable {

    DatagramSocket c = null;

    public void run(){
        try{
            c = new DatagramSocket();
            c.setBroadcast(true);
            byte[] sendData = "DISCOVER_SERVER_REQUEST".getBytes();
            while (true){
                try{
                    byte[] buf = new byte[256];
                    InetAddress ip = InetAddress.getByName("255.255.255.255");
                    DatagramPacket packet = new DatagramPacket(sendData,sendData.length,ip,6789);
                    c.send(packet);
                    System.out.println(getClass().getName() + ": Request packet sent to: 255.255.255.255 (DEFAULT)");

                }catch(IOException e){
                    e.printStackTrace();
                }

                // Broadcast the message over all the network interfaces
                Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
                while (interfaces.hasMoreElements()) {
                    NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();

                    if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                        continue; // Don't want to broadcast to the loopback interface
                    }

                    for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                        InetAddress broadcast = interfaceAddress.getBroadcast();
                        if (broadcast == null) {
                            continue;
                        }

                        // Send the broadcast package!
                        try {
                            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, 6789);
                            c.send(sendPacket);
                        } catch (Exception e) {
                        }

                        System.out.println(getClass().getName() + ": Request packet sent to: " + broadcast.getHostAddress() + "; Interface: " + networkInterface.getDisplayName());
                    }
                }

                System.out.println(getClass().getName() + ": Done looping over all network interfaces. Now waiting for a reply!");

                //Wait for a response
                byte[] recvBuf = new byte[15000];
                DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
                c.receive(receivePacket);

                //We have a response
                System.out.println(getClass().getName() + ": Broadcast response from server: " + receivePacket.getAddress().getHostAddress());

                //Check if the message is correct
                String message = new String(receivePacket.getData()).trim();
                if (message.equals("DISCOVER_SERVER_RESPONSE")) {
                    //DO SOMETHING WITH THE SERVER'S IP (for example, store it in your controller)
                    System.out.println("Server's IP: "+receivePacket.getAddress());
                }

                //Close the port!
//                c.close();
                try {
                    //stop talking for 1.5 sec
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException ex) {
            // Logger.getLogger(LoginWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}