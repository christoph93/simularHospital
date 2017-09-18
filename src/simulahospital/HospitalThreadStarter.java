package simulahospital;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HospitalThreadStarter {

    private FileReader fr;
    String fileName;
    ArrayList<Integer> intervals;

    public HospitalThreadStarter(String file) {

        fileName = file;
    }

    public void start() {

        try {
            fr = new FileReader(fileName);

            intervals = new ArrayList<>();

            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();

            String[] hospCodes = line.split(";");
            System.out.println("Hospitais: " + line + "\n");

            //coloca todos os números em uma lista
            while (line != null) {
                line = br.readLine();
                if (line != null) {
                    intervals.add(Integer.parseInt(line));
                }
            }

            br.close();
            fr.close();

            ExecutorService executor = Executors.newFixedThreadPool(hospCodes.length * 2);
            
            for (String s : hospCodes) {
                HospPush hospPush = new HospPush(s, intervals);                
                System.out.println("Starting push for " + hospPush.getHospCode());
                executor.execute(hospPush);
                
                HospPop hospPop = new HospPop(s, intervals);
                System.out.println("Starting pop for " + hospPop.getHospCode());
                executor.execute(hospPop);
            }
            
            executor.shutdown();
            while (!executor.isTerminated()) {
            }           
            

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
