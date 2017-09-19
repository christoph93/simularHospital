/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulahospital;

import java.util.List;
import org.apache.http.NameValuePair;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ccalifi
 */
public class HospTimes {

    private ObjectMapper mapper;

    public HospTimes() {
        mapper = new ObjectMapper();
    }

    public Map<String,Integer> getTimes() throws IOException {

        Get g = new Get();
        String json = g.sendRequest("http://tcc-si.herokuapp.com/api/");

        Map<String,Integer> times = new HashMap<>();
        
        //System.out.println(json);
        HospitalObj hosp = mapper.readValue(json, HospitalObj.class);
        
        //times.put(hosp.getHospCode(), hosp.getTime());
        //para teste:
        
        times.put("puc", 34);
        times.put("moinhos", 26);
        return times;

    }

}
