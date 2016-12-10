public class runDiscovery {
    public static void main(String[] args) {
//        if (args.length ==0) {
//            Thread talker = new Thread(new UDPTalker());
//            Thread discoveryThread = new Thread(new DiscoveryThread());
//            talker.start();
//            discoveryThread.start();
//        }
//        else {
            //someone is trying to create a server and we have to tell everyone that there is a
            //file here on that particular machine
        System.out.println(System.getProperty("user.dir"));
        ttorrentTracker t = new ttorrentTracker();
        t.track();

        ttorrentClient c = new ttorrentClient();
        c.connect();


//        }
    }
}
