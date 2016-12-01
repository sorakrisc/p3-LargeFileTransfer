import com.turn.ttorrent.common.Torrent;
import com.turn.ttorrent.tracker.TrackedTorrent;
import com.turn.ttorrent.tracker.Tracker;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


//512*1024
//destination of the file, number, http://<ip>:<port>/annouce, string(name)
public class test {
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
    public static void main(String[] args) {
        // File parent = new File("d:/echo-insurance.backup");
        String sharedFile = "/home/james/Downloads/testjpg.jpg";
        List<List<URI>> URI =buildURI("6969");
        try {
            Tracker tracker = new Tracker( InetAddress.getLocalHost() );
            tracker.start();
            System.out.println("Tracker running.");
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
