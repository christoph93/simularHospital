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
    private String bestChoice = "Nenhuma melhor escolha";

    public User(Map<String,Integer> travelTimes, int arrival, int service) {
        this.travelTimes = travelTimes;
        waitTimes = new HashMap<>(travelTimes.size());
        this.arrival = arrival;
        this.service = service;
    }

    //calcula o melhor tempo fazendo a chamada em tempo real
    public String bestChoice() throws IOException {

        int bestTime = 999;
        int currTotalTime = 0;        
        
        Get g = new Get();                
        HospTimes htimes = new HospTimes(travelTimes.keySet());
        waitTimes = htimes.getTimes();        
        
        for (String s : waitTimes.keySet()) {
            
            //calcula o tempo total para cada hospital
            currTotalTime = travelTimes.get(s) + waitTimes.get(s);

            //decide se é melhor que o melhor tempo atual
            if (bestTime > currTotalTime) {
                bestChoice = s;
                bestTime = currTotalTime;
            }
        }
        return bestChoice;
    }
    
    
    //calcula o melhor tempo recebendo um Map
    public String bestChoice(Map<String,Integer> waitTimes){
        
        int bestTime = 999;
        int currTotalTime = 0;
        bestChoice = "Nenhuma melhor escolha";  
        
        for (String s : waitTimes.keySet()) {
            
            //calcula o tempo total para cada hospital
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
        return s += "\nBest choice: " + bestChoice;
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

    public int getArrival() {
        return arrival;
    }

    public void setArrival(int arrival) {
        this.arrival = arrival;
    }

    public int getService() {
        return service;
    }

    public void setService(int service) {
        this.service = service;
    }
    
    

}
