package simulahospital;

public class SimulaHospital {

    public static void main(String[] args)  {

        
        HospitalThreadStarter hosp1 = new HospitalThreadStarter(args[0]);
        hosp1.start();        

    }

}
