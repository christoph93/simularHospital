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

    ArrayList<User> intervals;

    public HospPush(ArrayList<User> intervals) throws FileNotFoundException {
        this.intervals = intervals;
    }

    @Override
    public void run() {
        long tempo = 0;

        //usa o keySet (lista de hospitais)
        HospTimes hTimes = new HospTimes(intervals.get(0).getTravelTimes().keySet());

        Map<String, Integer> queueWaitTimes = null;

        //getTimeInterval é o intervalo de ciclos para atualizar os tempos.
        int getTimeInterval = 2;

        //para atualizar na primeira vez que entrar no for
        int i = getTimeInterval;

        for (User u : intervals) {
            //atualiza os tempos em um itervalo para não sobrecarregar o backend
            if (getTimeInterval == i) {
                try {
                    System.out.println("Updating wait times");
                    queueWaitTimes = hTimes.getTimes();
                } catch (IOException ex) {
                    Logger.getLogger(HospPush.class.getName()).log(Level.SEVERE, null, ex);
                }
                i = 0;
            }
            i++;

            //intervalo
            tempo = u.getNextArrival() * 1000;
            List<NameValuePair> jsonData = new ArrayList<>();

            //cria o post para a melhor escolha
            u.calculateTotalTimes(queueWaitTimes); //calcula os tempos
            jsonData.add(new BasicNameValuePair("hospitalCode", u.bestChoice())); //atribui a melhor escolha para hospitalCode

            System.out.println(u.toString());

            Post p = new Post("http://tcc-si.herokuapp.com/api/queue/push", jsonData);
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
                //System.out.println(ex);
            } finally {
                System.out.println("Pushing to " + u.bestChoice());

                if (response.get(0).contains("200")) {
                    System.out.println("Usuário inserido com sucesso em " + response.get(1));
                } else {
                    System.out.println("Falha ao inserir usuário em " + response.get(1));
                }
                System.out.print("\n//Esperando " + tempo + "ms\n");
                try {
                    Thread.sleep(tempo);
                } catch (InterruptedException ex) {
                    Logger.getLogger(HospPush.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

    }

}
