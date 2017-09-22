package simulahospital;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class HospPop implements Runnable {

    private String hospCode;
    private LinkedList<Integer> popIntervals;
    private boolean running = true;

    public HospPop(String hospCode) {
        this.hospCode = hospCode;
        popIntervals = new LinkedList<>();
        popIntervals.add(0);
    }

    public void insertInerval(int interval) {
        popIntervals.addLast(interval);
    }

    @Override
    public void run() {       
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(HospPop.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        long time = 0;
        Post p = null;        
        ListIterator<Integer> it = popIntervals.listIterator();
        int i = 0;

        while(running) {
            while(!it.hasNext()){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(HospPop.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            i = it.next();
            
            System.out.println("Pop interval for " + hospCode + ": " + i);

            time = i * 1000;

            List<NameValuePair> jsonData = new ArrayList<>();
            jsonData.add(new BasicNameValuePair("hospitalCode", hospCode));
            p = new Post("http://tcc-si.herokuapp.com/api/queue/pop", jsonData);

            ArrayList<String> response = new ArrayList<>();

            response.add(0, "200");
            response.add(1, hospCode);
            response.add(2, "name filler");
            response.add(3, "location filler");
            response.add(4, "queue filler");

            try {
                //envia o post e coloca a resposta no array
                response = p.sendRequest();
            } catch (IOException ex) {
                Logger.getLogger(HospPush.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                System.out.println("<- Popping from " + hospCode);

                if (response.get(0).contains("200")) {
                    System.out.println("Successfull pop from " + response.get(1));
                } else {
                    System.out.println("Failed to pop from " + response.get(1));
                }
            }

            try {
                System.out.println("Waiting " + i + "s for next pop from " + hospCode);
                Thread.sleep(time);
            } catch (InterruptedException ex) {
                Logger.getLogger(HospPop.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
