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
        // prefer android APK files // -src-prec apk
//        Options.v().set_src_prec(Options.src_prec_apk);

//       Scene.v().addBasicClass("java.io.PrintStream", SootClass.SIGNATURES);
//       Scene.v().addBasicClass("java.lang.System", SootClass.SIGNATURES);
//        Options.v().set_android_jars(ANDROID_JAR);


        // scenetransformer in whole-program packs
        //bodytransformer in  jimple packs 'jtp', 'jop', 'jap'

        PackManager.v().getPack("jtp").add(
                new Transform("jtp.myTransform", new BodyTransformer() {
                    protected void internalTransform(Body body, String s, Map<String, String> map) {
                        System.out.println(body.getMethod());
                    }
                })
        );


//        Options.v().

        Options.v().set_output_format(Options.output_format_n);

        soot.Main.main(args);
    }
}
