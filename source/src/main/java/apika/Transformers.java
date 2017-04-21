package apika;

import org.junit.Assert;
import soot.*;
import soot.jimple.AssignStmt;
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

    static Object lock = new Object();

    public static class ComponentTransformer extends BodyTransformer {
        protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
//            SootClass sootClass = b.getMethod().getDeclaringClass();
//                for (UnitBox unitBox : b.getAllUnitBoxes()) {
//                    System.out.println(unitBox.getUnit());
//                }

            for (Unit unit : b.getUnits()) {
//                System.out.println(unit);


                if (unit instanceof InvokeStmt) {
                    InvokeStmt invokeStmt = (InvokeStmt) unit;

//                    System.out.println(unit);

//                    System.out.println(invokeStmt.getInvokeExpr().getMethod());
                    InvokeExpr call = invokeStmt.getInvokeExpr();
                    SootMethod func = call.getMethod();

                    if (Config.sensorManagerListener.contains(func.getSignature())) {
//                        System.out.println("\n"+unit);
//                        System.out.println(b.getMethod()); // method where the invoke locates
//                        System.out.println(func.getName()); // invoke which
//                        System.out.println(func.getDeclaration()); // func
//                        System.out.println(func.getDeclaringClass());
                        DexStatistics.callSites.add(new CallSite(func.getSignature(),
                                b.getMethod().getDeclaringClass().toString(),
                                b.getMethod().toString()));
                    }

                    if (Config.sensorMangerGetSensor.containsKey(func.getSignature())) {
                        System.out.println("\n"+unit);
                        Value v = call.getArg(Config.sensorMangerGetSensor.get(func.getSignature()));
                        System.out.println("argument is " + v);
                    }

                } else if (unit instanceof AssignStmt) {
                    AssignStmt assignStmt = (AssignStmt) unit;
                    if (assignStmt.containsInvokeExpr()) {
                        InvokeExpr call = assignStmt.getInvokeExpr();

                        SootMethod func = call.getMethod();

                        if (Config.sensorManagerListener.contains(func.getSignature())) {
                            DexStatistics.callSites.add(new CallSite(func.getSignature(),
                                    b.getMethod().getDeclaringClass().toString(),
                                    b.getMethod().toString()));
                        }

                        if (Config.sensorMangerGetSensor.containsKey(func.getSignature())) {
                            System.out.println("\n"+unit);
                            Value v = call.getArg(Config.sensorMangerGetSensor.get(func.getSignature()));
                            System.out.println("argument is " + v);
                        }

                    }
                }




            }// for each unit
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
