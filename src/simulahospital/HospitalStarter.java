package simulahospital;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HospitalStarter {

    private FileReader fr;
    String fileName;
    ArrayList<User> intervals;

    public HospitalStarter(String file) {
        fileName = file;
    }
    
    
    /*
    
    Lê os a lista de chegadas global e decide para qual hospital o usuário será enviado
    
    */

    public void start() {

        try {
            fr = new FileReader(fileName);

            intervals = new ArrayList<>();

            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine(); //para pular aprimeira linha do arquivo
            line = br.readLine(); //linha dos hopsitais

            String[] hospCodes = line.split(";"); //coloca os hospCodes em um array            
            
            String aux = "";
            for (String s : hospCodes){
                aux += s + "; ";
            }            
            System.out.println(aux);
                 
            String[] lineElements;
            
            //aramzena os tempos de viagem para cada hospital por linha
            Map<String,Integer> travelTimes;
            
            //começa leitura do aquivo
            while (line != null) {
                line = br.readLine();                
                if (line != null) {
                    //converte a linha em array separando os elementos por ;                   
                    lineElements = line.split(";");    
                    travelTimes = new HashMap<>();
                                       
                    //primeiro elemento: intervalo para o próximo push
                    //segundo elemento: tempo de atendimento (tempo para o próximo pop)
                    //demais elementos: tempos de viagem para cada hospital, na ordem que estão na 2ª linha do arquivo
                    
                    //coloca os tempos de viagem correspondentes a cada hospital no map de tempos (começando pelo 3º elemento
                    for(int i = 2; i < hospCodes.length+2; i++){
                        travelTimes.put(hospCodes[i-2], Integer.parseInt(lineElements[i]));                       
                    }   
                    //coloca todos os usuários com os tempos de viagem, intervalo e espera na lista de intervalos                    
                    intervals.add(new User(travelTimes, Integer.parseInt(lineElements[0]), Integer.parseInt(lineElements[1])));
                }
            }
            
            br.close();
            fr.close();  
           
            ExecutorService executor = Executors.newFixedThreadPool(2);            
            
                HospPush hospPush = new HospPush(intervals);                
                System.out.println("Starting push thread");
                executor.execute(hospPush);
                
               /* HospPop hospPop = new HospPop(intervals);
                System.out.println("Starting pop thread");
                executor.execute(hospPop);*/
            
        
            executor.shutdown();
            while (!executor.isTerminated()) {
            }           
            

            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HospitalStarter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HospitalStarter.class.getName()).log(Level.SEVERE, null, ex);

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
