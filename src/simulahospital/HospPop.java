package simulahospital;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class HospPop implements Runnable {

    private String hospCode;
    private Queue<Integer> popIntervals;
    private boolean running = true;

    public HospPop(String hospCode) {
        this.hospCode = hospCode;
        popIntervals = new LinkedBlockingDeque<>();        
    }

    public void insertInerval(int interval) {
        popIntervals.offer(interval);
    }

    @Override
    public void run() {
        
        while (running) {
            while (popIntervals.isEmpty()) {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ex) {
                    Logger.getLogger(HospPop.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            Post p = null;

            if (!popIntervals.isEmpty()) {                
                //tira o próximo itervalo da fila
                int popInterval = popIntervals.poll();  
                
                
                if(popInterval == -1){
                    running = false;
                    break;
                } //cheogu no final da fila e não serão colocados mais elementos

                try {
                    Thread.sleep(popInterval * 1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(HospPop.class.getName()).log(Level.SEVERE, null, ex);
                }

                List<NameValuePair> jsonData = new ArrayList<>();
                jsonData.add(new BasicNameValuePair("hospitalCode", hospCode));
                p = new Post("http://tcc-si.herokuapp.com/api/queue/pop", jsonData);

                ArrayList<String> response = new ArrayList<>();

                response.add(0, "200");
                response.add(1, "puc");

                try {
                    //envia o post e coloca a resposta no array
                    response = p.sendRequest();

                } catch (IOException ex) {
                    Logger.getLogger(HospPush.class.getName()).log(Level.SEVERE, null, ex);                    
                } finally {
                    if (response.get(0).contains("200")) {
                        System.out.println("<- Pop from " + response.get(1) + " OK");
                    } else {
                        System.out.println("<- Pop from " + response.get(1) + " FAIL");
                    }
                }

            }
        }
        HospitalStarter.signalStop();
        System.out.println("Finished popping for " + hospCode);

    }
}
