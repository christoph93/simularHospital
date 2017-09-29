package simulahospital;

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
        HospTimes hTimes = new HospTimes(HospitalStarter.getIntervals().peek().getTravelTimes().keySet());

        Map<String, Integer> queueWaitTimes = null;

        //getTimeInterval é o intervalo de ciclos para atualizar os tempos.
        int getTimeInterval = 2;

        //para atualizar na primeira vez que entrar no for
        int i = getTimeInterval;

        while (!HospitalStarter.getIntervals().isEmpty()) {
            User u = HospitalStarter.getIntervals().poll(); //pega o próximo elemento da fila
            time = u.getArrivalDelay();
            try {
                Thread.sleep(time * 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(HospPush.class.getName()).log(Level.SEVERE, null, ex);
            }

            //atualiza os tempos em um itervalo para não sobrecarregar o backend
            if (getTimeInterval == i) {
                try {
                    queueWaitTimes = hTimes.getTimes();
                    // System.out.println("Updating wait times: " + queueWaitTimes);
                } catch (IOException ex) {
                    Logger.getLogger(HospPush.class.getName()).log(Level.SEVERE, null, ex);
                }
                i = 0;
            }
            i++;

            //intervalo            
            List<NameValuePair> jsonData = new ArrayList<>();

            u.calculateTotalTimes(queueWaitTimes);
            /*
            try {
                //cria o post para a melhor escolha
                u.calculateTotalTimes(); //calcula os tempos
            } catch (IOException ex) {
                Logger.getLogger(HospPush.class.getName()).log(Level.SEVERE, null, ex);
            }
             */

            jsonData.add(new BasicNameValuePair("hospitalCode", u.bestChoice())); //atribui a melhor escolha para hospitalCode           

            Post p = new Post("http://tcc-si.herokuapp.com/api/queue/push", jsonData);
            ArrayList<String> response = new ArrayList<>();

            try {
                //envia o post e coloca a resposta no array
                response = p.sendRequest();

            } catch (IOException ex) {
                Logger.getLogger(HospPush.class.getName()).log(Level.SEVERE, null, ex);
                //System.out.println(ex);
            } finally {

                response.add(0, "200");
                response.add(1, "puc");

                if (response.get(0).contains("200")) {
                    System.out.println("-> Push to " + response.get(1) + " OK");
                    //adiciona na fila do pop correspondente                    
                    HospitalStarter.getHospitals().get(u.bestChoice()).insertInerval(u.getArrivalDelay() + u.getService());
                } else {
                    System.out.println("-> Push to " + response.get(1) + " FAIL");
                }
            }
            
            //quando terminar de fazer dodos os pushs, coloca um indicador no final de cada HospPop 
            //para sinalizar que não serão iseridos novos intervalos
            if (HospitalStarter.getIntervals().isEmpty()) {                
                for(String s : HospitalStarter.hospitals.keySet()){
                    HospitalStarter.hospitals.get(s).insertInerval(-1);
                }
                
                System.out.println("Finished pushing");
            }
        }

    }

}
