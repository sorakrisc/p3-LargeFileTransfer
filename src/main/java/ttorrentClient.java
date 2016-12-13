import com.turn.ttorrent.client.Client;
import com.turn.ttorrent.client.SharedTorrent;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ttorrentClient implements Runnable{
    InetAddress InetAddr;
    String dirTorFileName;
    InetAddress masterInetAddr;

    public ttorrentClient(InetAddress ip, InetAddress mastInetAddr){
        InetAddr = ip;
        dirTorFileName = System.getProperty("user.dir")+"/seed.torrent";
        masterInetAddr = mastInetAddr;
    }

    public static Client client;
    public void connect() {
        try {
            // First, instantiate the Client object.
//            Client client = null;
            try {
                this.client = new Client(
                        // This is the interface the client will listen on (you might need something
                        // else than localhost here).
                        InetAddr,
                        // Load the torrent from the torrent file and use the given
                        // output directory. Partials downloads are automatically recovered.
                        SharedTorrent.fromFile(
                                new File(dirTorFileName),
                                new File(System.getProperty("user.dir"))));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            // You can optionally set download/upload rate limits
            // in kB/second. Setting a limit to 0.0 disables rate
            // limits.
            this.client.setMaxDownloadRate(0.0);
            this.client.setMaxUploadRate(0.0);

            // At this point, can you either call download() to download the torrent and
            // stop immediately after...
            // client.download();
            this.client.addObserver(new Observer() {
                @Override
                public void update(Observable observable, Object data) {
                    Client client = (Client) observable;
                    float progress = client.getTorrent().getCompletion();
                    // send progress to the master with ip runDiscovery.ip
                    System.out.println(progress);
                }
            });


            //progress
            ExecutorService pool = Executors.newFixedThreadPool(1);
            pool.submit(new Thd(InetAddr, masterInetAddr));
            runDiscovery.clientStatus = true;
            // Or call client.share(...) with a seed time in seconds:
            this.client.share();
            // Which would seed the torrent for an hour after the download is complete.

            // Downloading and seeding is done in background threads.
            // To wait for this process to finish, call:
            this.client.waitForCompletion();

            // At any time you can call client.stop() to interrupt the download.
        } catch (IOException ex) {
            System.out.println("There is an error..");
            //Logger.getLogger(DiscoveryThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        boolean switches = true;
        while (switches) {
            if (runDiscovery.trackerStatus) {
                switches =false;
                connect();
            } else {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Thd implements Runnable{
        InetAddress InetAddr;
        InetAddress masterInetAddr;
        public Thd(InetAddress itfaddr, InetAddress mstInetAddr) {
            InetAddr = itfaddr;
            masterInetAddr = mstInetAddr;
        }
        @Override
        public void run() {
            if (!masterInetAddr.equals(null)) {
                while (true) {
                    float progress = ttorrentClient.client.getTorrent().getCompletion();
                    String state = ttorrentClient.client.getState().toString();
                    System.out.println("State: " + state + " Progress: " + progress);
                    Thread ptc = new Thread(new ProgTrackClient(masterInetAddr, Float.toString(progress)));
                    ptc.start();
                    //System.out.println(ttorrentClient.client.getPeers());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
