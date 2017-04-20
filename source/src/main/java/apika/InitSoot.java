package apika;
import soot.*;
import soot.jimple.infoflow.android.SetupApplication;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.options.Options;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by guoxing on 17/4/2017.
 */
public class InitSoot {
    //static String JAVA_HOME;
    //static String SOOT_CLASS_PATH;
    static String ANDROID_JARS;

    static void checkArgs(String[] args) {
        if (args == null || args.length < 1) {
            throw new RuntimeException("ERROR: no arguments, apika.Main <apkfile>");
        }
    }


    static void initSootOptions(String[] args) {
        soot.G.reset();
//        Options.v().set_soot_classpath(SOOT_CLASS_PATH); // -cp // no need
        // Options.v().allow_phantom_refs(); // -allow-phantom-refs //doesnt' work ??????
        Options.v().set_exclude(new ArrayList<String>(Arrays.asList("android.*"))); // -x
        Options.v().set_android_jars(ANDROID_JARS);  // -android-jars
        Options.v().set_src_prec(Options.src_prec_apk); // -src-prec apk
        Options.v().set_output_format(Options.output_format_n); // -output-format n

        Options.v().set_whole_program(true);
//        Options.v().set_process_dir(Arrays.asList(args));


//        SetupApplication app = new SetupApplication(
//                getAndroidJars(), args[0]
//        );
//        SootMethod entryPoint = app.getEntryPointCreator().createDummyMain();
//        Options.v().set_main_class(entryPoint.getSignature());
//        Scene.v().setEntryPoints(Collections.singletonList(entryPoint));
//
//        SootMethod s = new SootMethod()
//        Scene.v().setEntryPoints(new ArrayList<SootMethod>(new SootMethod()[]
//                {
//                "<com.a712.collectdata.MainActivity: void onCreate(android.os.Bundle)>"}));
    }


    static void initSootOptions(String[] args, int i) {
        SetupApplication app = new SetupApplication(
                getAndroidJars(), args[0]
        );

//        SetupApplication app = new SetupApplication(
//                "/home/sirning/Program/Android/Sdk/platforms",
//                "/home/sirning/Desktop/test/app-debug.apk");
//        try {
//            app.setCallbackFile("/home/sirning/Program/soot-infoflow-android/AndroidCallbacks.txt");
//            app.calculateSourcesSinksEntrypoints(
//                    "/home/sirning/Program/soot-infoflow-android/SourcesAndSinks.txt");
//        }
//        catch (Exception e)
//        {
//            System.err.println(e.getMessage());
//            System.exit(1);
//        }
        //*/
        soot.G.reset();
        //Options.v().set_exclude(new ArrayList<String>(Arrays.asList("android.*"))); // -x
        Options.v().set_android_jars(ANDROID_JARS);  // -android-jars
        Options.v().set_process_dir(Collections.singletonList("/home/sirning/Desktop/test/app-debug.apk"));
        Options.v().set_src_prec(Options.src_prec_apk); // -src-prec apk
        Options.v().set_output_format(Options.output_format_n); // -output-format n
        Options.v().set_whole_program(true);
        Options.v().set_allow_phantom_refs(true);
        Options.v().setPhaseOption("cg.spark","on");

        Scene.v().loadNecessaryClasses();

        SootMethod entryPoint = app.getEntryPointCreator().createDummyMain();
        Options.v().set_main_class(entryPoint.getSignature());
        Scene.v().setEntryPoints(Collections.singletonList(entryPoint));
        PackManager.v().runPacks();
        CallGraph cg = Scene.v().getCallGraph();
        Iterator<Edge> it = cg.iterator();
        while(it.hasNext()) {
            Edge e = it.next();
            System.out.println("------------------------------------");
            if(e.toString().contains("registerListener")) {
                System.out.println(it.next().toString());
            }
        }

        //*/
        //Options.v().set_app(true);
//        Options.v().set_process_dir(Arrays.asList(args));
    }

    /**
     * init JAVA_HOME, SOOT_CLASS_PATH, ANDROID_JARS
     *
     * java properties file examples : http://www.mkyong.com/java/java-properties-file-examples/
     */
    static void configurationInit() {

        //JAVA_HOME = System.getenv("JAVA_HOME");
        //if (JAVA_HOME == null) {
        //    throw new RuntimeException("JAVA_HOME undefined");
        //}
        //String pathSeparator = File.pathSeparator;
        //String separator = File.separator;
        //String rtJar = JAVA_HOME + separator + "jre" + separator + "lib" + separator + "rt.jar";
        //String jsseJar = JAVA_HOME + separator + "jre" + separator + "lib" + separator + "jsse.jar";
        //SOOT_CLASS_PATH = JAVA_HOME + "/jre/lib/rt.jar" + pathSeparator
        //        + JAVA_HOME + "/jre/lib/jsse.jar" + pathSeparator + ".";
        //SOOT_CLASS_PATH = rtJar + pathSeparator + jsseJar + pathSeparator + ".";
        //SOOT_CLASS_PATH = ".";
        // read config.properties

        ANDROID_JARS = getAndroidJars();
    }

    static String getAndroidJars() {
        if (ANDROID_JARS != null)
            return ANDROID_JARS;
        String path = null;
        try {
            Properties prop = new Properties();
            InputStream input = new FileInputStream("config.properties");
            prop.load(input);
            path = prop.getProperty("ANDROID_JARS");
            if (path == null) {
                throw new RuntimeException("property 'ANDROID_JARS' undefined in file config.properties");
            }
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("config.properties, file not found");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        ANDROID_JARS = path;
        return path;
    }

    /**
     * get all Application Components an app uses
     */
    static void addComponentTransformer() {
        PackManager.v().getPack("jtp").add(
                new Transform("jtp.componentTrans",
                        new Transformers.ComponentTransformer()));

//        PackManager.v().getPack("wjtp").add(
//                new Transform("wjtp.detailedMethodUsage",
//                        new Transformers.CollectDetailedMethodUsage()));
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
