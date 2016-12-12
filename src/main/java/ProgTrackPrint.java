import java.util.HashSet;
import java.util.Set;

public class ProgTrackPrint implements Runnable{
    public void run(){
        runDiscovery.fileServerstatusChecker();
        boolean switches = true;
        Set<String> setValue = new HashSet<>();
        while(switches){
            for (String echk : runDiscovery.prgtrkMap.keySet()){
                String value =runDiscovery.prgtrkMap.get(echk);
                System.out.println(echk+":  "+value);
                setValue.add(value);
            }
            if (setValue.size() ==1 && setValue.contains("100.0")){
                switches = false;
            }
            System.out.println("|||||||||||||||||||||||");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
