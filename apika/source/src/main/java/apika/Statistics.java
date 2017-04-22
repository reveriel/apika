package apika;

import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by guoxing on 17/4/2017.
 */

public class Statistics {


    public static long startProcessTime;
    public static long endProcessTime;

    static void printJson() {
        JSONObject obj = toJson();
        try (FileWriter file = new FileWriter(Config.getJsonFileName())) {
            file.write(obj.toJSONString());
            System.out.println("Result write to File : " + Config.getJsonFileName());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static JSONObject toJson() {

        JSONObject obj = new JSONObject();

        obj.put("apk", Config.getApkName());
        obj.put("manifest", ManifestStatistics.toJson());
        obj.put("callsite", DexStatistics.toJson());
        obj.put("sootTime", endProcessTime - startProcessTime);
//        obj.put("jsonTime", endPrintingJson - startPrintingJson); // neglectable
        return obj;
    }

}
