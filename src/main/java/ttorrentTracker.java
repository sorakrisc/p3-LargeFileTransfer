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
            List<List<URI>> URI = buildURI();
            System.out.println(getcorrectInetAddr().get(0));
            createTorrentFile(URI);
            // First, instantiate a Tracker object with the port you want it to listen on.
            // The default tracker port recommended by the BitTorrent protocol is 6969.
            Tracker tracker = new Tracker(new InetSocketAddress(getcorrectInetAddr().get(0),6969));
            tracker.start();

            // Then, for each torrent you wish to announce on this tracker, simply created
            // a TrackedTorrent object and pass it to the tracker.announce() method:
            System.out.println(System.getProperty("user.dir"));
            tracker.announce(TrackedTorrent.load(new File(System.getProperty("user.dir")+"/seed.torrent")));
            // Once done, you just have to start the tracker's main operation loop:
//            tracker.start();

            // You can stop the tracker when you're done with:
            //tracker.stop();
        }catch (IOException ex) {
//            Logger.getLogger(DiscoveryThread.class.getName()).log(Level.SEVERE, null, ex);
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    public static List<InetAddress> getcorrectInetAddr(){
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
                    if (interfaceAddress.getAddress().toString().substring(1,4).equals("192")) {
                        lst1.add(interfaceAddress.getAddress());
                    }

                }
            }
        }catch (IOException ex) {
            System.out.println("error at getcorrectInetAddr");
        }
        return lst1;
    }
    public static List<List<URI>> buildURI(){
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
                        if (interfaceAddress.getAddress().toString().substring(1,4).equals("192")) {
                            lst1.add(new URL("http:/" + interfaceAddress.getAddress().toString() + ":" + "6969" + "/announce").toURI());
                        }
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
    public static void createTorrentFile(List<List<URI>> URI) {
        // File parent = new File("d:/echo-insurance.backup");
        String sharedFile = System.getProperty("user.dir")+"/testjpg.jpg";
//        List<List<URI>> URI =buildURI("6969");

        try {
            System.out.println( "create new .torrent metainfo file..." );
            Torrent torrent = Torrent.create(new File(sharedFile), 512*1024, URI, "createdByJamesTle");

            System.out.println("HII");
            System.out.println("save .torrent to file...");

            FileOutputStream fos = new FileOutputStream(System.getProperty("user.dir")+"/seed.torrent");
            torrent.save(fos);
            fos.close();
            System.out.println(".torrent file is saved");

        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

}
