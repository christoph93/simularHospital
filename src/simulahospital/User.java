/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulahospital;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ccalifi
 */
public class User {

    private Map<String, Integer> travelTimes;
    private Map<String, Integer> queueWaitTimes;
    private Map<String, Integer> totalTimes;
    private int nextArrival;
    private int service;
    private String bestChoice = "no best choice";

    public User(Map<String, Integer> travelTimes, int arrival, int service) {
        this.travelTimes = travelTimes;
        queueWaitTimes = new HashMap<>(travelTimes.size());
        this.nextArrival = arrival;
        this.service = service;
    }

    //calcula o melhor tempo fazendo a chamada em tempo real
    public HashMap<String, Integer> calculateTotalTimes() throws IOException {

        HashMap<String, Integer> totalTimes = new HashMap<>(travelTimes.keySet().size());

        HospTimes htimes = new HospTimes(travelTimes.keySet());
        queueWaitTimes = htimes.getTimes();

        queueWaitTimes.keySet().stream().forEach((s) -> {
            //calcula o tempo total para cada hospital         
            totalTimes.put(s, travelTimes.get(s) + queueWaitTimes.get(s));
        });
        this.totalTimes = totalTimes;
        return totalTimes;
    }

    //calcula o melhor tempo recebendo um Map (para teste offline)
    public HashMap<String, Integer> calculateTotalTimes(Map<String, Integer> waitTimes) {
        HashMap<String, Integer> totalTimes = new HashMap<>(travelTimes.keySet().size());
        queueWaitTimes = waitTimes;

        queueWaitTimes.keySet().stream().forEach((s) -> {
            //calcula o tempo total para cada hospital         
            totalTimes.put(s, travelTimes.get(s) + queueWaitTimes.get(s));
        });
        this.totalTimes = totalTimes;
        return totalTimes;
    }

    public String bestChoice() {

        int bestTime = 999;
        int currTotalTime = 0;

        for (String s : totalTimes.keySet()) {

            currTotalTime = queueWaitTimes.get(s) + travelTimes.get(s);
            //decide se é melhor que o melhor tempo atual
            if (bestTime > currTotalTime) {
                bestChoice = s;
                bestTime = currTotalTime;
            }
        }
        return bestChoice;
    }

    @Override
    public String toString() {
        return "Travel times: " + travelTimes + 
                " Wait times: " + queueWaitTimes + 
                " Total times: " + totalTimes +  
                " Best choice: " + bestChoice +
                "\nNext arrival: " + nextArrival +
                " Service time: " + service;

    }

    public Map<String, Integer> getTravelTimes() {
        return travelTimes;
    }

    public void setTravelTimes(Map<String, Integer> travelTimes) {
        this.travelTimes = travelTimes;
    }

    public Map<String, Integer> getWaitTimes() {
        return queueWaitTimes;
    }

    public void setWaitTimes(Map<String, Integer> waitTimes) {
        this.queueWaitTimes = waitTimes;
    }

    public int getNextArrival() {
        return nextArrival;
    }

    public void setNextArrival(int arrival) {
        this.nextArrival = arrival;
    }

    public int getService() {
        return service;
    }

    public void setService(int service) {
        this.service = service;
    }

}
