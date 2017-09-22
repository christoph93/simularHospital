package simulahospital;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class HospPop implements Runnable {

    @Override
    public void run() {

        long time = 0;
        Post p = null;

        for (User u : HospitalStarter.getIntervals()) {

            time = (u.getNextArrival() + u.getService());

            try {
                System.out.println("Waiting " + time + "(" + u.getNextArrival() + "+" + u.getService() + ") for next pop");
                Thread.sleep(time * 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(HospPop.class.getName()).log(Level.SEVERE, null, ex);
            }

            List<NameValuePair> jsonData = new ArrayList<>();
            jsonData.add(new BasicNameValuePair("hospitalCode", u.bestChoice())); //atribui a melhor escolha para hospitalCode
            p = new Post("http://tcc-si.herokuapp.com/api/queue/push", jsonData);

            ArrayList<String> response = new ArrayList<>();

            response.add(0, "200");
            response.add(1, "hospCode filler");
            response.add(2, "name filler");
            response.add(3, "location filler");
            response.add(4, "queue filler");

            try {
                //envia o post e coloca a resposta no array
                response = p.sendRequest();

            } catch (IOException ex) {
                Logger.getLogger(HospPush.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                System.out.println("<- Popping from " + u.bestChoice());

                if (response.get(0).contains("200")) {
                    System.out.println("Successfull pop from " + response.get(1));
                } else {
                    System.out.println("Failed to pop from " + response.get(1));
                }

            }

            if (u == HospitalStarter.getIntervals().get(HospitalStarter.getIntervals().size() - 1)) {
                System.out.println("Finished popping");
                Clock.stop();
            }

        }
    }

}
