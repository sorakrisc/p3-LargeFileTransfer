import com.turn.ttorrent.common.Torrent;
import com.turn.ttorrent.tracker.TrackedTorrent;
import com.turn.ttorrent.tracker.Tracker;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.NoSuchAlgorithmException;


public class ttorrentTracker {
    public void track(){
        try{
            // First, instantiate a Tracker object with the port you want it to listen on.
            // The default tracker port recommended by the BitTorrent protocol is 6969.
            Tracker tracker = new Tracker(new InetSocketAddress(6969));

            // Then, for each torrent you wish to announce on this tracker, simply created
            // a TrackedTorrent object and pass it to the tracker.announce() method:
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".torrent");
                }
            };
            for (File f : new File("/path/to/torrent/files").listFiles(filter)) {
                tracker.announce(TrackedTorrent.load(f));
            }

            // Once done, you just have to start the tracker's main operation loop:
            tracker.start();

            // You can stop the tracker when you're done with:
            tracker.stop();
        }catch (IOException ex) {
//            Logger.getLogger(DiscoveryThread.class.getName()).log(Level.SEVERE, null, ex);
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    //512*1024
    //destination of the file, number, url(interface), string(name)
    public static void createTorrentFile(){
        // File parent = new File("d:/echo-insurance.backup");
        String sharedFile = "d:/echo-insurance.backup";

        try {
            Tracker tracker = new Tracker( InetAddress.getLocalHost() );
            tracker.start();
            System.out.println("Tracker running.");

            System.out.println( "create new .torrent metainfo file..." );
            Torrent torrent = Torrent.create(new File(sharedFile), tracker.getAnnounceUrl().toURI(), "createdByDarren");

            System.out.println("save .torrent to file...");

            FileOutputStream fos = new FileOutputStream("d:/seed.torrent");
            torrent.save( fos );
            fos.close();

        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}
