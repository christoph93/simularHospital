package simulahospital;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
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

            ArrayList<HospPush> hospPushList = new ArrayList<>();
            ArrayList<HospPop> hospPopList = new ArrayList<>();

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

            ExecutorService es = Executors.newSingleThreadExecutor();

            for (String hosp : hospCodes) {

                System.out.println("Iniciando Push thread para " + hosp);
                es.submit(new HospPush(hosp, intervals));

                System.out.println("Iniciando Pop thread para " + hosp);
                es.submit(new HospPop(hosp, intervals));
            }
            
            
            es.shutdown();

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
