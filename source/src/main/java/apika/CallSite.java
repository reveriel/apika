package apika;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;

/**
 * Created by guoxing on 20/4/2017.
 */
public class CallSite {
    String func;
    String contextClass;
    String contextMethod;

    public CallSite(String func, String contextClass, String contextMethod) {
        this.func = func;
        this.contextClass = contextClass;
        this.contextMethod = contextMethod;
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
        return obj;
    }

}
