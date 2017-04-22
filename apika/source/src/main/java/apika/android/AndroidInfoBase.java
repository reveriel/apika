package apika.android;

import org.json.simple.JSONObject;

/**
 * Created by guoxing on 19/4/2017.
 */
public class AndroidInfoBase {
    String name;
    public AndroidInfoBase(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }
}
