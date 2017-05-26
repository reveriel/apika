package apika.transformer;

import apika.data.CallSite;
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
public class Helper{

    // must be thread safe
    // mainly exam classes information

    public static InvokeExpr getInvokeExpr(Unit unit) {
        if (unit instanceof InvokeStmt) {
            return ((InvokeStmt) unit).getInvokeExpr();
        } else if (unit instanceof AssignStmt) {
            if (((AssignStmt) unit).containsInvokeExpr()) {
                return ((AssignStmt) unit).getInvokeExpr();
            }
        }
        return null;
    }

    public static void printAllSuperClass(SootMethod func, Body b) {
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

    public static void addAllSuperClass(Body b, CallSite c) {
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
    public static boolean isApplicationComponentClass(SootClass klass) {
        String className = klass.toString();
        klass.isApplicationClass();

        Scene.v().getAndroidAPIVersion();

        return className.contains("Activity");
    }


}
