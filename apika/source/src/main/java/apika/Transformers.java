package apika;

import apika.statistics.DexStatistics;
import soot.*;
import soot.jimple.AssignStmt;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
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

    // must be thread safe
    // mainly exam classes information


    public static class ComponentTransformer extends BodyTransformer {
        protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
            for (Unit unit : b.getUnits()) {

                InvokeExpr call = getInvokeExpr(unit);
                if (call == null)
                    continue;
                SootMethod func = call.getMethod();


                if (Config.sensorManagerListener.contains(func.getSignature())) {
//                        System.out.println("\n"+unit);
//                        System.out.println(b.getMethod()); // method where the invoke locates
//                        System.out.println(func.getName()); // invoke which
//                        System.out.println(func.getDeclaration()); // func
//                        System.out.println(func.getDeclaringClass());
                    CallSite c = new CallSite(func.getSignature(),
                            b.getMethod().getDeclaringClass().toString(),
                            b.getMethod().toString());
                    DexStatistics.callSites.add(c);
                    addAllSuperClass(b, c);
                    printAllSuperClass(func, b);

                } else if (Config.sensorMangerGetSensor.containsKey(func.getSignature())) {
                    // sensor type is in argument
                    Value v = call.getArg(Config.sensorMangerGetSensor.get(func.getSignature()));

                    CallSite c;
                    printAllSuperClass(func, b);
                    if (v instanceof IntConstant) {
                        IntConstant intV = (IntConstant) v;
                        c = new CallSite(func.getSignature(),
                                b.getMethod().getDeclaringClass().toString(),
                                b.getMethod().toString(),
                                intV.value);
                        DexStatistics.callSites.add(c);
                    } else {
                        c = new CallSite(func.getSignature(),
                                b.getMethod().getDeclaringClass().toString(),
                                b.getMethod().toString());
                        DexStatistics.callSites.add(c);
                    }
                    addAllSuperClass(b, c);
                }

            }// for each unit
        }


        private static InvokeExpr getInvokeExpr(Unit unit) {
            if (unit instanceof InvokeStmt) {
                return ((InvokeStmt) unit).getInvokeExpr();
            } else if (unit instanceof AssignStmt) {
                if (((AssignStmt) unit).containsInvokeExpr()) {
                    return ((AssignStmt) unit).getInvokeExpr();
                }
            }
            return null;
        }
    }

    private static void printAllSuperClass(SootMethod func, Body b) {
        System.out.println(func.getSignature());
        SootClass callerClass = b.getMethod().getDeclaringClass();
        System.out.println("Class :" + callerClass.getName());
        SootClass superClass = callerClass;
        while (superClass.hasSuperclass()) {
            superClass = superClass.getSuperclass();
            System.out.println("      -> " + superClass.getName());
        }
        System.out.println("");
    }

    private static void addAllSuperClass(Body b, CallSite c) {
        SootClass callerClass = b.getMethod().getDeclaringClass();
        SootClass superClass = callerClass;
        while (superClass.hasSuperclass()) {
            superClass = superClass.getSuperclass();
            c.addSupperClasses(superClass.toString());
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

    public static class CallGraphDump extends SceneTransformer
    {

        @Override
        protected void internalTransform(String s, Map<String, String> map) {
            CallGraph cg = Scene.v().getCallGraph();
            cg.forEach(System.out::println);
            System.out.println("call graph size = " + cg.size());
        }
    }

    public static class DumpCallerParents extends SceneTransformer
    {
        @Override
        protected void internalTransform(String s, Map<String, String> map) {
        }
    }

    public static class CollectDetailedMethodUsage extends SceneTransformer
    {
        @Override
        protected void internalTransform(String s, Map<String, String> map) {
            CallGraph cg = Scene.v().getCallGraph();

            System.out.println("edges count: " + cg.size());
            for (Iterator<Edge> i = cg.iterator(); i.hasNext(); ) {
                System.out.println(i.next());
            }

//            Edge e=null;
//            Iterator<Edge> it=null;
//            SootMethod caller=null;
//            it=cg.iterator();
//            while(it.hasNext())
//            {
//                System.out.println(it.next().toString());
//
//            }
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