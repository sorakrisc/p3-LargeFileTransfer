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


public class ttorrentTracker implements Runnable{
    InetAddress InetAddr;
    String dirTorFileName;

    @Override
    public void run() {
        track();
    }

    String dirShareFileName;
    public ttorrentTracker(InetAddress ip, String fn){
        InetAddr = ip;
        dirTorFileName = System.getProperty("user.dir")+"/seed.torrent";
        dirShareFileName = System.getProperty("user.dir")+"/"+fn;
    }
    public void track(){
        try{
            List<List<URI>> URI = buildURI();
            createTorrentFile(URI);
            // First, instantiate a Tracker object with the port you want it to listen on.
            // The default tracker port recommended by the BitTorrent protocol is 6969.
            Tracker tracker = new Tracker(new InetSocketAddress(InetAddr,6969));
            tracker.start();

            // Then, for each torrent you wish to announce on this tracker, simply created
            // a TrackedTorrent object and pass it to the tracker.announce() method:
            tracker.announce(TrackedTorrent.load(new File(dirTorFileName)));
            runDiscovery.trackerStatus = true;
            // Once done, you just have to start the tracker's main operation loop:// tracker.start();// You can stop the tracker when you're done with:// tracker.stop();

        }catch (IOException ex) {
//            Logger.getLogger(DiscoveryThread.class.getName()).log(Level.SEVERE, null, ex);
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    public List<List<URI>> buildURI(){
        List<URI> lst1 = new ArrayList<>();
        List<List<URI>> lst2 = new ArrayList<>();
        try{
            lst1.add(new URL("http:/" +InetAddr.toString()+ ":" + "6969" + "/announce").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        lst2.add(lst1);
        return lst2;
    }


    public void createTorrentFile(List<List<URI>> URI) {
        try {
            Torrent torrent = Torrent.create(new File(dirShareFileName), 512*1024, URI, "createdByJamesTle");

            FileOutputStream fos = new FileOutputStream(dirTorFileName);
            torrent.save(fos);
            fos.close();
            System.out.println( ".torrent file created" );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

}
