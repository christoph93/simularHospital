/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulahospital;

/**
 *
 * @author ccalifi
 */
public class HospitalObj {
 
    private int time;
    private String hospCode;
    
    
    public HospitalObj(String hospCode, int time){
        this.time = time;
        this.hospCode = hospCode;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getHospCode() {
        return hospCode;
    }

    public void setHospCode(String hospCode) {
        this.hospCode = hospCode;
    }
    
    
    @Override
    public String toString(){
        return hospCode + "->" + time;
    }
    
    
}
