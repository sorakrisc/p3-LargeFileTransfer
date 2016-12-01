import com.turn.ttorrent.client.Client;
import com.turn.ttorrent.client.SharedTorrent;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;
import java.util.Observable;
import java.util.Observer;

public class ttorrentClient {
    public void connect() {
        try {
            // First, instantiate the Client object.
            Client client = null;
            try {
                client = new Client(
                        // This is the interface the client will listen on (you might need something
                        // else than localhost here).
                        InetAddress.getLocalHost(),

                        // Load the torrent from the torrent file and use the given
                        // output directory. Partials downloads are automatically recovered.
                        SharedTorrent.fromFile(
                                new File("/home/james/Downloads/punch.torrent"),
                                new File("/home/james/Downloads/")));
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
