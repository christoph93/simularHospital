package simulahospital;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author chris
 */
public class HospPush implements Runnable {

    String hospCode;
    ArrayList<Integer> intervals;

    public HospPush(String hospCode, ArrayList<Integer> intervals) throws FileNotFoundException {
        this.hospCode = hospCode;
        this.intervals = intervals;
    }

    @Override
    public void run() {
        long tempo = 0;

        //Colocar usuário na fila
        for (Integer i : intervals) {

            tempo = i * 1000;

            List<NameValuePair> jsonData = new ArrayList<>();
            jsonData.add(new BasicNameValuePair("hospitalCode", hospCode));

            //cria um post                
            Post p = new Post("http://tcc-si.herokuapp.com/api/queue/push", jsonData);
            ArrayList<String> response = null;

            try {
                response = p.sendRequest();

                /*
                 response.forEach((s) -> {
                    System.out.println(s);
                });
                 */
                if (response.get(0).contains("200")) {
                    System.out.println("Usuário inserido com sucesso em " + response.get(1));
                } else {
                    System.out.println("Falha ao inserir usuário em " + response.get(1));
                }

                System.out.print("\n//Esperando " + tempo + "ms\n");
                Thread.sleep(tempo);

            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(HospPush.class.getName()).log(Level.SEVERE, null, ex);
                //System.out.println(ex);
            } 
            finally {
                System.out.println(hospCode + " push. Waiting " + tempo);
                try {
                    Thread.sleep(tempo);
                } catch (InterruptedException ex) {
                    Logger.getLogger(HospPush.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

    }

    public String getHospCode() {
        return hospCode;
    }

    public void setHospCode(String hospCode) {
        this.hospCode = hospCode;
    }

}
