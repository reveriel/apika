package apika.transformer;

import apika.Config;
import apika.data.CallSite;
import apika.statistics.DexStatistics;
import soot.*;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;

import java.util.Map;

import static apika.transformer.Helper.addAllSuperClass;
import static apika.transformer.Helper.getInvokeExpr;
import static apika.transformer.Helper.printAllSuperClass;

/**
 * Created by guoxing on 26/5/2017.
 */

/**
 * this transformer collect callsite information and
 * sensor type
 */
public class ComponentTransformer extends BodyTransformer {
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
}
