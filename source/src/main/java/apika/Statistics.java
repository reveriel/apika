package apika;

import apika.android.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import soot.SootClass;
import soot.SootMethod;
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

    static Set<AndroidActivity> usedActivities = new HashSet<>();
    static Set<AndroidService> usedServices = new HashSet<>();
    static Set<AndroidReceiver> usedReceivers = new HashSet<>();
    static Set<AndroidProvider> usedProviders = new HashSet<>();
    static Set<AndroidPermission> usedPermissions = new HashSet<>();
    static Set<AndroidFeature> usedFeatures = new HashSet<>();

    static void createOutput() {
        JSONObject obj = new JSONObject();
        List<String> apkNames= Options.v().process_dir();
        String apkName = apkNames.get(0);
        apkName = apkName.replace(File.separator.charAt(0),'.');  // error : '/' in file name
        apkName = apkName.replaceAll("^\\.+", ""); // remove leading '.' in file name
        obj.put("apk", apkName);
        JSONArray array = null;

        array = new JSONArray();
        obj.put("Class List", array);
        array = new JSONArray();
        obj.put("Activity List", array);

        array = new JSONArray();
        obj.put("Service List", array);


        String OutputFileName = "output" + File.separator + apkName + ".json";
        try (FileWriter file = new FileWriter(OutputFileName)) {
            file.write(obj.toJSONString());
            System.out.println("Result write to File : " + OutputFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void printOutput() {
        System.out.println(usedActivities.toString());
        System.out.println(usedServices.toString());
        System.out.println(usedReceivers.toString());
        System.out.println(usedProviders.toString());
        System.out.println(usedPermissions.toString());
        System.out.println(usedFeatures.toString());
    }


}
