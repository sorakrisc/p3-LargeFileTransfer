public class runDiscovery {
    public static void main(String[] args) {
        Thread talker = new Thread(new UDPTalker());
        Thread discoveryThread = new Thread(new DiscoveryThread());
        talker.start();
        discoveryThread.start();
    }
}
