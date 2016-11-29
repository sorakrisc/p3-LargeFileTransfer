public class runDiscovery {
    public static void main(String[] args) {
        Thread talker = new Thread(UDPTalker.getTalkInstance());;
        Thread discoveryThread = new Thread(DiscoveryThread.getInstance());
        talker.start();
        discoveryThread.start();
    }
}
