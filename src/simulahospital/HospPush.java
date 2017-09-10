package simulahospital;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.json.JSONObject;

/**
 *
 * @author chris
 */
public class HospPush implements Runnable {

    String hospCode;
    int numCicl, max, min;
    private FileReader fr;
    private String fileName;
    
    Logger logger = Logger.getLogger("Log");
    FileHandler fh;

    public HospPush(String hospCode, String file) throws FileNotFoundException {
        this.hospCode = hospCode;
        this.fileName = file;        
    }

    @Override
    public void run() {
        
        
        
        try {  

        // This block configure the logger with handler and formatter  
        fh = new FileHandler("C:\\Users\\chris\\Documents\\NetBeansProjects\\simulaHospital\\log.log");  
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();  
        fh.setFormatter(formatter);  

       

    } catch (SecurityException e) {  
        e.printStackTrace();  
    } catch (IOException e) {  
        e.printStackTrace();  
    }  


        
        

        try {
            fr = new FileReader(fileName);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HospPush.class.getName()).log(Level.SEVERE, null, ex);
        }

        long tempo = 0;
        String output;

        //Colocar usuário na fila
        try {

            JSONObject json;

            URL url = new URL("http://tcc-si.herokuapp.com/api/queue/push");

            BufferedReader br = new BufferedReader(fr);
            br.readLine();
            String line = br.readLine();

            while (line != null) {

                tempo = Integer.valueOf(line) * 1000;
                
                json = new JSONObject();
                json.put("hospitalCode", hospCode);
                json.put("username", "");

                //String text = json.toString().replace("\"", "");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setInstanceFollowRedirects(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");                
                conn.setRequestProperty("Charset", "UTF-8");
                conn.setUseCaches(false);              
             

                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
                writer.write(json.toString());
                writer.close();
                wr.close();

                System.out.println("URL: " + url.toString());
                System.out.println("JSON: " + json.toString());
                System.out.println("Response: " + conn.getResponseMessage());

                System.out.println("\n//Esperando " + tempo + "ms\n");
                Thread.sleep(tempo);
                line = br.readLine();
                
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        } catch (InterruptedException ex) {
            Logger.getLogger(HospPush.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getHospCode() {
        return hospCode;
    }

    public void setHospCode(String hospCode) {
        this.hospCode = hospCode;
    }

    public int getNumCicl() {
        return numCicl;
    }

    public void setNumCicl(int numCicl) {
        this.numCicl = numCicl;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public FileReader getFr() {
        return fr;
    }

    public void setFr(FileReader fr) {
        this.fr = fr;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
