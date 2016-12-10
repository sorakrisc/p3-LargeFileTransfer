import com.turn.ttorrent.client.Client;
import com.turn.ttorrent.client.SharedTorrent;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class ttorrentClient {
    public static List<InetAddress> getLocalHost(){
        List<InetAddress> lst1 = new ArrayList<>();
        try{
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
                    System.out.println(interfaceAddress.getAddress());
                    if (interfaceAddress.getAddress().toString().substring(1,4).equals("10.")) {
                        lst1.add(interfaceAddress.getAddress());
                    }

                }
            }
        }catch (IOException ex) {
        }
        return lst1;
    }
    public void connect() {
        try {
            // First, instantiate the Client object.
            Client client = null;
            InetAddress addr = getLocalHost().get(0);
            System.out.println(addr);
            System.out.println(InetAddress.getLocalHost());
            try {
                client = new Client(
                        // This is the interface the client will listen on (you might need something
                        // else than localhost here).
                        // InetAddress.getLocalHost(),
                        addr,
                        // Load the torrent from the torrent file and use the given
                        // output directory. Partials downloads are automatically recovered.
                        SharedTorrent.fromFile(
                                new File(System.getProperty("user.dir")+"/seed.torrent"),
                                new File(System.getProperty("user.dir"))));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            // You can optionally set download/upload rate limits
            // in kB/second. Setting a limit to 0.0 disables rate
            // limits.
            client.setMaxDownloadRate(0.0);
            client.setMaxUploadRate(0.0);

            // At this point, can you either call download() to download the torrent and
            // stop immediately after...
            // client.download();
            client.addObserver(new Observer() {
                @Override
                public void update(Observable observable, Object data) {
                    Client client = (Client) observable;
                    float progress = client.getTorrent().getCompletion();
                    // Do something with progress.
                    System.out.println(progress);
                }
            });
            // Or call client.share(...) with a seed time in seconds:
            client.share(3600);
            // Which would seed the torrent for an hour after the download is complete.

            // Downloading and seeding is done in background threads.
            // To wait for this process to finish, call:
            client.waitForCompletion();

            // At any time you can call client.stop() to interrupt the download.
        } catch (IOException ex) {
            System.out.println("There is an error..");
            //Logger.getLogger(DiscoveryThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
