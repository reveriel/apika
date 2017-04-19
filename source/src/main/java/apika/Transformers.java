package apika;

import soot.*;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

    public static class CollectDetailedMethodUsage extends soot.SceneTransformer
    {
        private ArrayList<String> methodsToCollect;
        public CollectDetailedMethodUsage(List<String> methods)
        {
            methodsToCollect = new ArrayList<>(methods);
        }

        public CollectDetailedMethodUsage()
        {
            methodsToCollect = new ArrayList<>();
            methodsToCollect.add("<android.hardware.SensorManager: boolean registerListener(android.hardware.SensorListener,int)>");
            methodsToCollect.add("<android.hardware.SensorManager: boolean registerListener(android.hardware.SensorListener,int,int)>");
            methodsToCollect.add("<android.hardware.SensorManager: void unregisterListener(android.hardware.SensorListener)>");
            methodsToCollect.add("<android.hardware.SensorManager: void unregisterListener(android.hardware.SensorListener,int)>");
            methodsToCollect.add("<android.hardware.SensorManager: void unregisterListener(android.hardware.SensorEventListener,android.hardware.Sensor)>");
            methodsToCollect.add("<android.hardware.SensorManager: void unregisterListener(android.hardware.SensorEventListener)>");
            methodsToCollect.add("<android.hardware.SensorManager: boolean registerListener(android.hardware.SensorEventListener,android.hardware.Sensor,int)>");
            methodsToCollect.add("<android.hardware.SensorManager: boolean registerListener(android.hardware.SensorEventListener,android.hardware.Sensor,int,int)>");
            methodsToCollect.add("<android.hardware.SensorManager: boolean registerListener(android.hardware.SensorEventListener,android.hardware.Sensor,int,android.os.Handler)>");
            methodsToCollect.add("<android.hardware.SensorManager: boolean registerListener(android.hardware.SensorEventListener,android.hardware.Sensor,int,int,android.os.Handler)>");
        }
        @Override
        protected void internalTransform(String s, Map<String, String> map) {
            CallGraph cg = Scene.v().getCallGraph();
            Edge e=null;
            Iterator<Edge> it=null;
            SootMethod caller=null;
            it=cg.iterator();
            while(it.hasNext())
            {
                System.out.println(it.next().toString());

            }
            //System.exit(0);
            /*
            for(SootClass sc : Scene.v().getClasses())
            {
                if(sc.isApplicationClass()) {
                    for(SootMethod sm : sc.getMethods())
                    {
                        it=cg.edgesOutOf(sm);
                        while(it.hasNext())
                        {
                            e=it.next();
                            for(String theMethod : methodsToCollect)
                            {
                                caller=e.getTgt().method();
                                if(caller.getName().contains(theMethod))
                                {
                                    System.out.println(caller.getName()+"---->"+theMethod);
                                }
                            }

                        }
                    }
                }
            }//*/
        }
    }

    public static class CollectMethodsUsage extends BodyTransformer
    {
        private ArrayList<String> methodsToCollect;

        //supply the method signatures you want to collect
        public CollectMethodsUsage(List<String> methods)
        {
            methodsToCollect = new ArrayList<>(methods);
        }

        //by defalut we collect SensorManager.(un)registerListener
        public CollectMethodsUsage()
        {
            methodsToCollect = new ArrayList<>();
            methodsToCollect.add("<android.hardware.SensorManager: boolean registerListener(android.hardware.SensorListener,int)>");
            methodsToCollect.add("<android.hardware.SensorManager: boolean registerListener(android.hardware.SensorListener,int,int)>");
            methodsToCollect.add("<android.hardware.SensorManager: void unregisterListener(android.hardware.SensorListener)>");
            methodsToCollect.add("<android.hardware.SensorManager: void unregisterListener(android.hardware.SensorListener,int)>");
            methodsToCollect.add("<android.hardware.SensorManager: void unregisterListener(android.hardware.SensorEventListener,android.hardware.Sensor)>");
            methodsToCollect.add("<android.hardware.SensorManager: void unregisterListener(android.hardware.SensorEventListener)>");
            methodsToCollect.add("<android.hardware.SensorManager: boolean registerListener(android.hardware.SensorEventListener,android.hardware.Sensor,int)>");
            methodsToCollect.add("<android.hardware.SensorManager: boolean registerListener(android.hardware.SensorEventListener,android.hardware.Sensor,int,int)>");
            methodsToCollect.add("<android.hardware.SensorManager: boolean registerListener(android.hardware.SensorEventListener,android.hardware.Sensor,int,android.os.Handler)>");
            methodsToCollect.add("<android.hardware.SensorManager: boolean registerListener(android.hardware.SensorEventListener,android.hardware.Sensor,int,int,android.os.Handler)>");
        }
        protected void internalTransform(Body body, String s, Map<String, String> map) {
            boolean conTains;
            for(Unit u : body.getUnits())
            {
                conTains = false;
                if(u.toString().contains("virtualinvoke"))
                {
                    for(String method : methodsToCollect)
                    {
                        if(u.toString().contains(method))
                        {
                            conTains=true;
                            break;
                        }
                    }
                    if(conTains)
                    {

                    }
                }
            }
        }
    }




}
