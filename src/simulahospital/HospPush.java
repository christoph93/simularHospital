package simulahospital;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class HospPush implements Runnable {

    @Override
    public void run() {
        long time = 0;

        //usa o keySet (lista de hospitais)
        HospTimes hTimes = new HospTimes(HospitalStarter.getIntervals().get(0).getTravelTimes().keySet());

        Map<String, Integer> queueWaitTimes = null;

        //getTimeInterval é o intervalo de ciclos para atualizar os tempos.
        int getTimeInterval = 2;

        //para atualizar na primeira vez que entrar no for
        int i = getTimeInterval;

        for (User u : HospitalStarter.getIntervals()) {
            //atualiza os tempos em um itervalo para não sobrecarregar o backend
            if (getTimeInterval == i) {
                try {
                    queueWaitTimes = hTimes.getTimes();
                    System.out.println("Updating wait times: " + queueWaitTimes);
                } catch (IOException ex) {
                    Logger.getLogger(HospPush.class.getName()).log(Level.SEVERE, null, ex);
                }
                i = 0;
            }
            i++;

            //intervalo
            time = u.getNextArrival();
            List<NameValuePair> jsonData = new ArrayList<>();

            //cria o post para a melhor escolha
            u.calculateTotalTimes(queueWaitTimes); //calcula os tempos
            jsonData.add(new BasicNameValuePair("hospitalCode", u.bestChoice())); //atribui a melhor escolha para hospitalCode           

            Post p = new Post("http://tcc-si.herokuapp.com/api/queue/push", jsonData);
            ArrayList<String> response = new ArrayList<>();

            response.add(0, "200");
            response.add(1, u.bestChoice());
            response.add(2, "name filler");
            response.add(3, "location filler");
            response.add(4, "queue filler");

            try {
                //envia o post e coloca a resposta no array
                response = p.sendRequest();

            } catch (IOException ex) {
                Logger.getLogger(HospPush.class.getName()).log(Level.SEVERE, null, ex);
                //System.out.println(ex);
            } finally {
                System.out.println("-> Pushing to " + u.bestChoice());

                if (response.get(0).contains("200")) {
                    System.out.println("Successfull push to " + response.get(1));
                    //adiciona na fila do pop correspondente
                    System.out.println("Inserting pop interval of " + (u.getNextArrival() + u.getService()) + "s to " + response.get(1));
                    HospitalStarter.getHospitals().get(u.bestChoice()).insertInerval(u.getNextArrival() + u.getService());
                } else {
                    System.out.println("Failed push to " + response.get(1));
                }
                System.out.println("Waiting " + time + "s for next push");
                try {
                    Thread.sleep(time * 1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(HospPush.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (u == HospitalStarter.getIntervals().get(HospitalStarter.getIntervals().size() - 1)) {
                System.out.println("Finished pushing");
            }
        }

    }

}
