import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;

public class findInterface {
    public static List<InetAddress> getInetAddrList(){
        List<InetAddress> intfaceLst = new ArrayList<>();
        try{
            Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();
                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue; // Don't want to broadcast to the loopback interface
                }

                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                    InetAddress broadcast = interfaceAddress.getBroadcast();
                    if (broadcast == null) {
                        continue;
                    }
                    intfaceLst.add(interfaceAddress.getAddress());
                }
            }
        }catch (IOException ex) {
            System.out.println("error at getinetaddrlst");
        }
        return intfaceLst;
    }
    public static InetAddress askCorrectInetAddr(){
        List<InetAddress> intfaceLst = getInetAddrList();
        Scanner scanner = new Scanner(System.in);
        int i = 0;
        boolean validData = false;
        for (InetAddress ind : intfaceLst){
            System.out.println(i+") "+ind);
        }
        do {
            System.out.println("Enter a number corresponding to the Interface");
            try {
                i = scanner.nextInt(); //tries to get data. Goes to catch if invalid data
                validData = true; //if gets data successfully, sets boolean to true
            } catch (InputMismatchException e) {
                //executes when this exception occurs
                System.out.println("Input has to be a number. ");
            }
        } while(validData==false); //loops until validData is true
        return intfaceLst.get(i);
    }
}
