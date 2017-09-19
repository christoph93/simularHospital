package simulahospital;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HospitalThreadStarter {

    private FileReader fr;
    String fileName;
    ArrayList<User> intervals;

    public HospitalThreadStarter(String file) {

        fileName = file;
    }

    public void start() {

        try {
            fr = new FileReader(fileName);

            intervals = new ArrayList<>();

            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine(); //para pular aprimeira linha do arquivo
            line = br.readLine(); //linha dos hopsitais

            String[] hospCodes = line.split(";"); //coloca os hospCodes em um array            
            
            
            System.out.println("Hospitais: ");
            for (String s : hospCodes){
                System.out.println(s);
            }
            
                     
            int arrival = 0;
            int service = 0;
            String[] lineElements;
            Map<String,Integer> waitTimes;
            
            while (line != null) {
                line = br.readLine();
                if (line != null) {
                    //converte a linha em array separando os elementos por ;
                    lineElements = line.split(";");
                    waitTimes = new HashMap<>(hospCodes.length);
                    
                    //coloca os tempos de espera correspondentes a cada hospital no map de tempos
                    for(int i = 0; i < hospCodes.length; i++){
                        waitTimes.put(hospCodes[i], Integer.parseInt(lineElements[i+2]));
                    }
                    
                    intervals.add(new User(waitTimes, arrival, service));
                }
            }
            
            br.close();
            fr.close();
            
            
            
            for(User u : intervals){                
                System.out.println(u.toString());                        
            }            

            ExecutorService executor = Executors.newFixedThreadPool(hospCodes.length * 2);
            
            /*for (String s : hospCodes) {
                HospPush hospPush = new HospPush(s, intervals);                
                System.out.println("Starting push for " + hospPush.getHospCode());
                executor.execute(hospPush);
                
                HospPop hospPop = new HospPop(s, intervals);
                System.out.println("Starting pop for " + hospPop.getHospCode());
                executor.execute(hospPop);
            }*/
            
            executor.shutdown();
            while (!executor.isTerminated()) {
            }           
            

            HospTimes times = new HospTimes();
            System.out.println(times.getTimes());
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HospitalThreadStarter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HospitalThreadStarter.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    public FileReader getFr() {
        return fr;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
