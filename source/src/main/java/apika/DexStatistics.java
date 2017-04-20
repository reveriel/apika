package apika;

import org.json.simple.JSONArray;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by guoxing on 20/4/2017.
 */
public class DexStatistics {
    static Set<CallSite> callSites = Collections.synchronizedSet(new HashSet<CallSite>());


    public static JSONArray toJson() {
        JSONArray array = new JSONArray();
        for (CallSite s : DexStatistics.callSites) { array.add(s.toJson()); }
        return array;
    }
}
