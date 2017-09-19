/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulahospital;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ccalifi
 */
public class User {

    private Map<String, Integer> travelTimes;
    private Map<String, Integer> waitTimes;
    private int arrival;
    private int service;

    public User(Map<String,Integer> travelTimes, int arrival, int service) {
        this.travelTimes = travelTimes;
        waitTimes = new HashMap<>(travelTimes.size());
        this.arrival = arrival;
        this.service = service;
    }

    public String bestChoice() throws IOException {

        int bestTime = 999;
        int currTotalTime = 0;
        String bestChoice = "Nenhuma melhor escolha";
        
        
        //retorna os atuais tempos de espera para cada hospital
        HospTimes hTimes = new HospTimes();        
        waitTimes = hTimes.getTimes();
        
        //itera por todos os hospitais        
        for (String s : travelTimes.keySet()) {
            //calcula o tempo total para o hospital
            currTotalTime = travelTimes.get(s) + waitTimes.get(s);

            //decide se é melhor que o melhor tempo atual
            if (bestTime > currTotalTime) {
                bestChoice = s;
                bestTime = currTotalTime;
            }
        }
        return bestChoice;
    }
    
    
    @Override
    public String toString(){        
        try {
            String s = "Travel times: ";
            for (String k : travelTimes.keySet()){
                s += k + "->" + travelTimes.get(k).toString() + " ";                
            }
            System.out.println(s);
            s += "\nWait times: ";
            for(String k : waitTimes.keySet()){
                s += k + "->" + waitTimes.get(k).toString() + " ";
            }
            System.out.println(s);
            return s += "\nBest choice: " + bestChoice();
        } catch (IOException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            return "Failed at User.toString()";
        }
    }

    public Map<String, Integer> getTravelTimes() {
        return travelTimes;
    }

    public void setTravelTimes(Map<String, Integer> travelTimes) {
        this.travelTimes = travelTimes;
    }

    public Map<String, Integer> getWaitTimes() {
        return waitTimes;
    }

    public void setWaitTimes(Map<String, Integer> waitTimes) {
        this.waitTimes = waitTimes;
    }

}
