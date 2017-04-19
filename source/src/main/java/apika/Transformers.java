package apika;

import soot.*;

import java.util.Map;

/**
 * Created by guoxing on 17/4/2017.
 */
public class Transformers {
    static boolean DEBUG = false;
    // must be thread safe
    // mainly exam classes information
    static Object lock = new Object();

    public static class ComponentTransformer extends BodyTransformer {
        protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
            SootClass sootClass = b.getMethod().getDeclaringClass();

            if (DEBUG) {
                synchronized (lock) {
                    System.out.println("Class name: " + sootClass.toString());
                    System.out.println("Super class: " +
                            (sootClass.hasSuperclass() ? sootClass.getSuperclass().toString() : ""));
                    SootClass superClass = sootClass.getSuperclass();
                    System.out.println("Super Super class: " +
                            (superClass.hasSuperclass() ? superClass.getSuperclass().toString() : ""));
                    System.out.println("is Lib class: " + (sootClass.isLibraryClass() ? "true" : "false"));
                    System.out.println("is Appli class: " + (sootClass.isApplicationClass() ? "true" : "false"));
                    System.out.println("is javalib class: " + (sootClass.isJavaLibraryClass() ? "true" : "false"));
                }
            }

            Statistics.addClass(sootClass);
        }
    }

    /**
     * check based on string compare, if class name includes 'Activity' etc.
     * @return is Application Compoenent class
     */
    static boolean isApplicationComponentClass(SootClass klass) {
        String className = klass.toString();
        klass.isApplicationClass();

        Scene.v().getAndroidAPIVersion();

        return className.contains("Activity");
    }





}
