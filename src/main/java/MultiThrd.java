/**
 * Created by james on 13/12/2559.
 */
public class MultiThrd {
    public static void main(String[] args) {
        ProgTrackMultiThrdServ server = new ProgTrackMultiThrdServ(6789);
        new Thread(server).start();

        try {
            Thread.sleep(20 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Stopping Server");
        server.stop();
    }
}
