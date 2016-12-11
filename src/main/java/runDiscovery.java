import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
            ExecutorService threadPool = Executors.newFixedThreadPool(3);

            String shareFileName = args[0];
            // ask ip
            InetAddress intfaceaddr = findInterface.askCorrectInetAddr();

            // create tracker and torrent file
//            ttorrentTracker t = new ttorrentTracker(intfaceaddr, shareFileName);
//            t.track();
            threadPool.submit(new ttorrentTracker(intfaceaddr, shareFileName));

            // be a seeder
//            ttorrentClient c = new ttorrentClient(intfaceaddr);
//            c.connect();
            threadPool.submit(new ttorrentClient(intfaceaddr));

            // tell other that u have the dang file and open the server
            // open the server that host the torrent file
            String portserv = "4542";
            String filetosend = System.getProperty("user.dir")+"/seed.torrent"; //torrent file that
            threadPool.submit(new SimpleFileServer(portserv,filetosend));
//            Thread serv = new Thread(new SimpleFileServer(portserv,filetosend));
//            serv.run();

            // UDPtrigger and tell them to come get the file
            Thread trigger = new Thread(new UDPTrigger());
            trigger.start();
            System.out.println("STARTING TRIGGER");


        }
    }
}
// TODO: Delete the torrent file when it is done