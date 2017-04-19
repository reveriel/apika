package apika;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import soot.SootClass;
import soot.options.Options;

import java.io.File;
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
    private static Set<String> classes  = Collections.synchronizedSet(new HashSet<String>());
    private static Set<String> activities = Collections.synchronizedSet(new HashSet<String>());
    private static Set<String> services = Collections.synchronizedSet(new HashSet<String>());

    static void createOutput() {
        JSONObject obj = new JSONObject();
        List<String> apkNames= Options.v().process_dir();
        String apkName = apkNames.get(0);
        apkName = apkName.replace(File.separator.charAt(0),'.');  // error : '/' in file name
        apkName = apkName.replaceAll("^\\.+", ""); // remove leading '.' in file name
        obj.put("apk", apkName);
        JSONArray array = null;

        array = new JSONArray();
        array.addAll(classes);
        obj.put("Class List", array);

        array = new JSONArray();
        array.addAll(activities);
        obj.put("Activity List", array);

        array = new JSONArray();
        array.addAll(services);
        obj.put("Service List", array);


        String OutputFileName = "output" + File.separator + apkName + ".json";
        try (FileWriter file = new FileWriter(OutputFileName)) {
            System.out.println(obj.toJSONString());
            file.write(obj.toJSONString());
            System.out.println("Result write to File : " + OutputFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void addClass(SootClass sootClass) {

        // Inner classes has name like OuterClass$InnerClass or OuterClass$1  (anonymous classes)
        String outerClassName = (sootClass.hasOuterClass() ? sootClass.getOuterClass() : sootClass).toString();

        /**
         *  check based on string compare
         *  class name ended with 'Activity' is activity class
         */
        if (outerClassName.endsWith("Activity")) {
            Statistics.activities.add(outerClassName);
        } else if (outerClassName.endsWith("Service")) {
            Statistics.services.add(outerClassName);
        }

        Statistics.classes.add(outerClassName);
    }

}
