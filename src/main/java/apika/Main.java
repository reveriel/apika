package apika;


import soot.PackManager;
import soot.Scene;
import soot.SceneTransformer;
import soot.Transform;
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



        PackManager.v().getPack("wjtp").add(
                new Transform("wjtp.myTransform", new SceneTransformer() {
                    @Override
                    protected void internalTransform(String s, Map<String, String> map) {
                        System.out.println(Scene.v().getApplicationClasses());
                    }
                })
        );


        soot.Main.main(args);
    }
}
