package apika.transformer;

import apika.data.CallSite;
import apika.statistics.DexStatistics;
import soot.Body;
import soot.BodyTransformer;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.InvokeExpr;

import java.util.Map;

import static apika.transformer.Helper.addAllSuperClass;
import static apika.transformer.Helper.getInvokeExpr;

/**
 * Created by guoxing on 26/5/2017.
 */

/**
 * this transformer collect info of callsite which are in some specific package
 */

public class CollectCallSite extends BodyTransformer {
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        for (Unit unit : b.getUnits()) {

            InvokeExpr call = getInvokeExpr(unit);
            if (call == null) continue;

            SootMethod func = call.getMethod();

            if (shouldCollect(func)) {
                CallSite c = new CallSite(func.getSignature(),
                        b.getMethod().getDeclaringClass().toString(),
                        b.getMethod().toString());
                DexStatistics.callSites.add(c);
                addAllSuperClass(b, c);
            }
        }// for each unit
    }

    boolean shouldCollect(SootMethod f) {
        String packageName = f.getDeclaringClass().getName();
         // android.hardware.SensorManager$WakeLock, not SensorManager.WakeLock
        return packageName.startsWith("android.hardware.SensorManager")
                || packageName.startsWith("android.os.PowerManager")
                || packageName.startsWith("android.hardware.Sensor")
            || f.getSignature().contains("startActivity");
    }

}
