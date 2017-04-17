package apika;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import soot.options.Options;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by guoxing on 17/4/2017.
 */
public class Statistics {
    // must be thread safe
    static Set<String> classes  = Collections.synchronizedSet(new HashSet<String>());
    static Set<String> activities = Collections.synchronizedSet(new HashSet<String>());


    static void createOutput() {
        JSONObject obj = new JSONObject();
        List<String> apkNames= Options.v().process_dir();
        String apkName = apkNames.get(0);
        apkName = apkName.replace('/','.');  // error : '/' in file name
        apkName = apkName.replaceAll("^\\.+", ""); // remove leading '.' in file name

        obj.put("apk", apkName);
        JSONArray array = null;

        array = new JSONArray();
        array.addAll(classes);
        obj.put("Class List", array);

        array = new JSONArray();
        array.addAll(activities);
        obj.put("Activity List", array);


        String OutputFileName = "output/" + apkName + ".json";
        try (FileWriter file = new FileWriter(OutputFileName)) {
            System.out.println(obj.toJSONString());
            file.write(obj.toJSONString());
            System.out.println("Result write to File : " + OutputFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
