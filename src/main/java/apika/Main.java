package apika;


import soot.*;
import soot.options.Options;

import java.io.*;
import java.util.Map;

/**
 * Created by guoxing on 1/4/2017.
 */
public class Main {
    public static void main(String[] args) {


//        final String ANDROID_JAR = "/Users/guoxing/Library/Android/sdk/platforms";
//        prefer android APK files // -src-prec apk
//        Options.v().set_src_prec(Options.src_prec_apk);

//        Scene.v().addBasicClass("java.io.PrintStream", SootClass.SIGNATURES);
//        Scene.v().addBasicClass("java.lang.System", SootClass.SIGNATURES);
//        Options.v().set_android_jars(ANDROID_JAR);


        addComponentTransformer();
        Options.v().set_output_format(Options.output_format_n);
        soot.Main.main(args);
    }

    /**
     * get all Application Components an app uses
     */
    static void addComponentTransformer() {
        PackManager.v().getPack("jtp").add(
                new Transform("jtp.componentTrans", new BodyTransformer() {
                    @Override
                    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
                        SootClass sootClass = b.getMethod().getDeclaringClass();
                        System.out.println(sootClass);
                    }
                })

        );
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
