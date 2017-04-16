package apika;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import soot.*;
import soot.options.Options;

import java.io.*;
import java.util.*;

/**
 * Created by guoxing on 1/4/2017.
 */
public class Main {

    static String JAVA_HOME;
    static String SOOT_CLASS_PATH;
    static String ANDROID_JARS;

    // must be thread safe
    static Set<String> classes  = Collections.synchronizedSet(new HashSet<String>());

    public static void main(String[] args) {
        checkArgs(args);
        configurationInit();
        initSootOptions(args);
        addComponentTransformer();
        soot.Main.main(new String[]{"-allow-phantom-refs", "-process-dir", args[0]});
        createOutput();
    }


    static void checkArgs(String[] args) {
        if (args == null || args.length < 1) {
            throw new RuntimeException("ERROR: no arguments, apika.Main <apkfile>");
        }
    }

    static void initSootOptions(String[] args) {
//        Options.v().set_soot_classpath(SOOT_CLASS_PATH); // -cp // no need
        // Options.v().allow_phantom_refs(); // -allow-phantom-refs //doesnt' work ??????
        Options.v().set_exclude(new ArrayList<String>(Arrays.asList("android.*"))); // -x
        Options.v().set_android_jars(ANDROID_JARS);  // -android-jars
        Options.v().set_src_prec(Options.src_prec_apk); // -src-prec apk
        Options.v().set_output_format(Options.output_format_n); // -output-format n
//        Options.v().set_process_dir(Arrays.asList(args));
    }

    /**
     * init JAVA_HOME, SOOT_CLASS_PATH, ANDROID_JARS
     *
     * java properties file examples : http://www.mkyong.com/java/java-properties-file-examples/
     */
    static void configurationInit() {

        JAVA_HOME = System.getenv("JAVA_HOME");
        if (JAVA_HOME == null) {
            throw new RuntimeException("JAVA_HOME undefined");
        }

        SOOT_CLASS_PATH = JAVA_HOME + "/jre/lib/rt.jar:" + JAVA_HOME + "/jre/lib/jsse.jar:.";

        // read config.properties
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream("config.properties");

            prop.load(input);
            ANDROID_JARS = prop.getProperty("ANDROID_JARS");
//            ANDROID_JARS = "/Users/guoxing/Library/Android/sdk/platforms";
            if (ANDROID_JARS == null) {
                System.err.println("property 'ANDROID_JARS' undefined in file config.properties");
            }
        } catch (FileNotFoundException ex) {
            System.err.println("config.properties, file not found");
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * get all Application Components an app uses
     */
    static void addComponentTransformer() {
        PackManager.v().getPack("jtp").add(
                new Transform("jtp.componentTrans", new ComponentTransformer()));
    }

    // must be thread safe
    static class ComponentTransformer extends BodyTransformer {
        protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
            SootClass sootClass = b.getMethod().getDeclaringClass();
            classes.add(sootClass.toString());
        }
    }

    static void createOutput() {
        JSONObject obj = new JSONObject();
        List<String> apkNames= Options.v().process_dir();
        String apkName = apkNames.get(0);
        apkName = apkName.replace('/','.');  // error : '/' in file name
        apkName = apkName.replaceAll("^\\.+", ""); // remove leading '.' in file name

        obj.put("apk", apkName);
        JSONArray classesArray = new JSONArray();
        classesArray.addAll(classes);
        obj.put("Class List", classesArray);

        String OutputFileName = "output/" + apkName + ".json";
        try (FileWriter file = new FileWriter(OutputFileName)) {
            System.out.println(obj.toJSONString());
            file.write(obj.toJSONString());
            System.out.println("Result write to File : " + OutputFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * examples about how to add transformers
     */
    static void addExampleTransformer() {
        // scenetransformer in whole-program packs,  '-w' is needed to enable 'wjtp'
        PackManager.v().getPack("wjtp").add(
                new Transform("wjtp.myTransform", new SceneTransformer() {
                    protected void internalTransform(String phaseName,
                                                     Map options) {
                        System.err.println(Scene.v().getApplicationClasses());
                    }
                }));

        //bodytransformer in  jimple packs 'jtp', 'jop', 'jap'
        PackManager.v().getPack("jtp").add(
                new Transform("jtp.myTransform", new BodyTransformer() {
                    protected void internalTransform(Body body, String s, Map<String, String> map) {
                        System.out.println(body.getMethod());
                    }
                })
        );
    }
}
