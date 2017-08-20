import java.util.concurrent.ThreadLocalRandom;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SimulaHospital {

    public static void main(String[] args) throws InterruptedException {

        //args[0] - Número de ciclos
        //args[1] - tempo min (seg)
        //args[2] - tempo max (seg)
        long tempo = 0;

        String output;

        try {

            URL url = new URL("http://tcc-si.herokuapp.com/api");
            
            for (int i = 0; i <= Integer.valueOf(args[0]); i++) {
            
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            //con.setRequestProperty("Accept", "application/json");

            

                tempo = ThreadLocalRandom.current().nextLong(Integer.valueOf(args[1]) * 1000, Integer.valueOf(args[2]) * 1000);

                if (con.getResponseCode() != 200) {
                    throw new RuntimeException("Erro : "
                            + con.getResponseCode());
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (con.getInputStream())));

                System.out.println("Recebi: \n");
                while ((output = br.readLine()) != null) {
                    System.out.println(output);
                }
                con.disconnect();

                System.out.println("\n//Esperando " + tempo + "ms\n");
                Thread.sleep(tempo);
                
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }

    }

}
