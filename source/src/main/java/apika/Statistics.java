package apika;

import apika.android.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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

        String apkName = Config.apkName;

        apkName = apkName.replace(File.separator.charAt(0),'.');  // error : '/' in file name
        apkName = apkName.replaceAll("^\\.+", ""); // remove leading '.' in file name
        obj.put("apk", apkName);

        addArray(obj, usedActivities, "activites");
        addArray(obj, usedServices, "services");
        addArray(obj, usedReceivers, "receivers");
        addArray(obj, usedProviders, "providers");
        addArray(obj, usedPermissions, "permissions");
        addArray(obj, usedFeatures, "features");

        String OutputFileName = "output" + File.separator + apkName + ".json";
        try (FileWriter file = new FileWriter(OutputFileName)) {
            file.write(obj.toJSONString());
            System.out.println("Result write to File : " + OutputFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addArray(JSONObject obj, Set<? extends AndroidInfoBase> set, String name) {
        JSONArray array;
        array = new JSONArray();
        List<String> stringList = new ArrayList<>();
        for (AndroidInfoBase s : set) {
            stringList.add(s.toString());
        }
        array.addAll(stringList);
        obj.put(name, array);
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
