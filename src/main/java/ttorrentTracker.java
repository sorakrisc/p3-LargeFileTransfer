import com.turn.ttorrent.common.Torrent;
import com.turn.ttorrent.tracker.TrackedTorrent;
import com.turn.ttorrent.tracker.Tracker;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


public class ttorrentTracker {
    public void track(){
        try{
            // First, instantiate a Tracker object with the port you want it to listen on.
            // The default tracker port recommended by the BitTorrent protocol is 6969.
            Tracker tracker = new Tracker(new InetSocketAddress(6969));
            createTorrentFile();
            // Then, for each torrent you wish to announce on this tracker, simply created
            // a TrackedTorrent object and pass it to the tracker.announce() method:


            tracker.announce(TrackedTorrent.load(new File("/home/james/Downloads/seed.torrent")));
            // Once done, you just have to start the tracker's main operation loop:
            tracker.start();

            // You can stop the tracker when you're done with:
            //tracker.stop();
        }catch (IOException ex) {
//            Logger.getLogger(DiscoveryThread.class.getName()).log(Level.SEVERE, null, ex);
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    public static List<List<URI>> buildURI(String port){
        List<URI> lst1 = new ArrayList<>();
        List<List<URI>> lst2 = new ArrayList<>();
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
                    try{
                        lst1.add(new URL("http:/"+interfaceAddress.getAddress().toString()+":"+port+"/annouce").toURI());
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        }catch (IOException ex) {
        }
        lst2.add(lst1);
        return lst2;
    }
    public static void createTorrentFile() {
        // File parent = new File("d:/echo-insurance.backup");
        String sharedFile = "/home/james/Downloads/testjpg.jpg";
        List<List<URI>> URI =buildURI("6969");
        try {
            System.out.println( "create new .torrent metainfo file..." );
            Torrent torrent = Torrent.create(new File(sharedFile), 512*1024, URI, "createdByJamesTle");

            System.out.println("HII");
            System.out.println("save .torrent to file...");

            FileOutputStream fos = new FileOutputStream("/home/james/Downloads/seed.torrent");
            torrent.save(fos);
            fos.close();
            System.out.println(".torrent file is saved");

        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

}
