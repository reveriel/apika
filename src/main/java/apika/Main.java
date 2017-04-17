package apika;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import soot.options.Options;

import java.io.*;
import java.util.*;

/**
 * Created by guoxing on 1/4/2017.
 */
public class Main {

    // must be thread safe
    static Set<String> classes  = Collections.synchronizedSet(new HashSet<String>());
    static Set<String> activities = Collections.synchronizedSet(new HashSet<String>());

    public static void main(String[] args) {
        InitSoot.checkArgs(args);
        InitSoot.configurationInit();
        InitSoot.initSootOptions(args);
        InitSoot.addComponentTransformer();
        soot.Main.main(new String[]{"-allow-phantom-refs", "-process-dir", args[0]});
        createOutput();
    }


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
