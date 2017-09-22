package simulahospital;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author ccalifi
 */
public class HospTimes {

    private Set<String> hospCodes;

    public HospTimes(Set hospCodes) {
        this.hospCodes = hospCodes;
    }

    public Map<String, Integer> getTimes() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        Get g = new Get();
        Map<String, Integer> times = new HashMap<>();
        
        /*
        for (String s : hospCodes) {
            String json = g.sendRequest("http://tcc-si.herokuapp.com/api/queue/getMediumTime/" + s);
            HospitalObj hosp = mapper.readValue(json, HospitalObj.class);
            times.put(hosp.getHospCode(), hosp.getTime());
        }*/

        //System.out.println(json);        
        
        //para teste: (descomentar o for depois)
        times.put("puc", 5);
        times.put("moinhos", 5);        
        
        return times;

    }

}
