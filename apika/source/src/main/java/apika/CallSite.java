package apika;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by guoxing on 20/4/2017.
 */
public class CallSite {
    private String func;
    private String contextClass;
    private String contextMethod;

    private List<String> superClasses = new LinkedList<>();


    private boolean hasArg;
    private int sensorType;

    public CallSite(String func, String contextClass, String contextMethod) {
        this.func = func;
        this.contextClass = contextClass;
        this.contextMethod = contextMethod;
        this.hasArg = false;
    }

    public CallSite(String func, String contextClass, String contextMethod, int sensorType) {
        this.func = func;
        this.contextClass = contextClass;
        this.contextMethod = contextMethod;

        this.hasArg = true;
        this.sensorType = sensorType;
    }

    public void addSupperClasses(String className) {
        this.superClasses.add(className);
    }


    @Override
    public String toString() {
        return "func:" + func + ";contextClass:" + contextClass + ";contextMethod:" + contextMethod;
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("func",  func);
        obj.put("contextClass", contextClass);
        obj.put("contextMethod", contextMethod);
        if (hasArg) {
            obj.put("sensorType", sensorType);
        }

        JSONArray a = new JSONArray();
        a.addAll(superClasses);
        obj.put("superClasses", a);
        return obj;
    }

}