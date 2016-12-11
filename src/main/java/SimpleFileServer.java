import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleFileServer implements Runnable {
    static Integer PORT;
    static String FILE_TO_SEND;  // you may change this
    public SimpleFileServer(String port,String FTS){
        Integer p = Integer.valueOf(port);
        this.PORT = p;
        this.FILE_TO_SEND=FTS;
    }
    public void run() {
//        System.out.println("Creating tracker");
//        ttorrentTracker t = new ttorrentTracker(intfaceaddr, shareFileName);
//        t.track();
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        ServerSocket servsock = null;
        Socket sock = null;
        try {
            servsock = new ServerSocket(PORT);
            while (true) {

                try {
                    sock = servsock.accept();
                    System.out.println("Accepted connection : " + sock);
                    // send file
                    File myFile = new File (FILE_TO_SEND);
                    byte [] mybytearray  = new byte [(int)myFile.length()];
                    fis = new FileInputStream(myFile);
                    bis = new BufferedInputStream(fis);
                    bis.read(mybytearray,0,mybytearray.length);
                    os = sock.getOutputStream();
                    System.out.println("Sending " + FILE_TO_SEND + "(" + mybytearray.length + " bytes)");
                    os.write(mybytearray,0,mybytearray.length);
                    os.flush();
                    System.out.println("Done.");
                }
                finally {
                    if (bis != null) bis.close();
                    if (os != null) os.close();
                    if (sock!=null) sock.close();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


}