package apika.statistics;

import apika.android.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by guoxing on 20/4/2017.
 */
public class ManifestStatistics {
    public static Set<AndroidActivity> usedActivities = new HashSet<>();
    public static Set<AndroidService> usedServices = new HashSet<>();
    public static Set<AndroidReceiver> usedReceivers = new HashSet<>();
    public static Set<AndroidProvider> usedProviders = new HashSet<>();
    public static Set<AndroidPermission> usedPermissions = new HashSet<>();
    public static Set<AndroidFeature> usedFeatures = new HashSet<>();


    public static JSONObject toJson() {
        JSONObject manifest = new JSONObject() ;
        manifest.put("receiver", toJson(usedActivities));
        manifest.put("activities", toJson(usedActivities));
        manifest.put("services", toJson(usedServices));
        manifest.put("receivers", toJson(usedReceivers));
        manifest.put("providers", toJson(usedProviders));
        manifest.put("permissions", toJson(usedPermissions));
        manifest.put("features", toJson(usedFeatures));
        return manifest;
    }

    private static JSONArray toJson(Set set) {
        JSONArray array = new JSONArray();
        for (Object i : set) {
            array.add(i.toString());
        }
        return array;
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
