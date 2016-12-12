import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class runDiscovery {

    static boolean trackerStatus = false;
    static boolean clientStatus =false;
    static boolean fileServerStatus = false;
    static ConcurrentHashMap<String, String> prgtrkMap= new ConcurrentHashMap<>(); //ip, progress


    public static void fileServerstatusChecker(){
        boolean switches = true;
        while(switches){
            if (fileServerStatus){
                switches = false;
                break;
            }
            else{
                try {
                    Thread.sleep(1500); // if it is not up sleep for 1.5 s and ask again
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public static void main(String[] args) {
        if (args.length ==0) {
            InetAddress intfaceaddr = findInterface.askCorrectInetAddr();
            Thread talker = new Thread(new UDPTalker());
            Thread discoveryThread = new Thread(new DiscoveryThread(intfaceaddr));
            talker.start();
            discoveryThread.start();
        }
        // someone wants to distribute a file
        // create a hosting server and to tell everyone that there is
        // a file here on that particular machine. broadcast to everyone the file name and the ip
        else {
            ExecutorService threadPool = Executors.newFixedThreadPool(6);
            String shareFileName = args[0];

            // ask ip
            InetAddress intfaceaddr = findInterface.askCorrectInetAddr();

            // create tracker and torrent file
            threadPool.submit(new ttorrentTracker(intfaceaddr, shareFileName));

            // be a seeder so other people can download
            threadPool.submit(new ttorrentClient(intfaceaddr, null));

            // tell other that u have the file and open the server
            // open the server that host the torrent file
            String portserv = "4542";
            String filetosend = System.getProperty("user.dir")+"/seed.torrent"; //torrent file that
            threadPool.submit(new SimpleFileServer(portserv,filetosend));

            // UDPtrigger and tell them to come get the file
            // but wait till the file hosting server is up
            fileServerstatusChecker();
            System.out.println("****working on trigger****");
            Thread trigger = new Thread(new UDPTrigger());
            trigger.start();

            //start progress tracking server
            System.out.println("****starting PTS****");
            threadPool.submit((new ProgTrackServer()));

            System.out.println("****starting PTS printer****");
            threadPool.submit((new ProgTrackPrint()));



        }
    }
}
// TODO: Delete the torrent file when it is done
// TODO: Check for excessive prints and useless comments