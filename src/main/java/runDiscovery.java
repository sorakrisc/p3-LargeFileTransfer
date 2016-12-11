import java.net.InetAddress;

public class runDiscovery {
    public static void main(String[] args) {
        if (args.length ==0) {
            InetAddress intfaceaddr = findInterface.askCorrectInetAddr();
            Thread talker = new Thread(new UDPTalker());
            Thread discoveryThread = new Thread(new DiscoveryThread(intfaceaddr));
            talker.start();
            discoveryThread.start();
        }

        // someone is trying to create a hosting server and we have to tell everyone that there is a
        // file here on that particular machine
        // broadcast to everyone the file name and the ip
        else {
            String shareFileName = args[0];
            // ask ip
            InetAddress intfaceaddr = findInterface.askCorrectInetAddr();

            // create tracker and torrent file
            ttorrentTracker t = new ttorrentTracker(intfaceaddr, shareFileName);
            t.track();

            // be a seeder
            ttorrentClient c = new ttorrentClient(intfaceaddr);
            c.connect();

            // tell other that u have the dang file

        }
    }
}
