package apika;

/**
 * Created by guoxing on 20/4/2017.
 */

import soot.Body;
import soot.BodyTransformer;
import soot.PackManager;
import soot.Transform;

import java.util.Map;

/**
 * print all function signatures in a jar file
 *
 * useage:
 *    ./gradle jar
 *    ./dumpSig.sh  filename.jar
 */
public class Tools {
    public static void main(String[] args) {
        PackManager.v().getPack("jtp").add(
                new Transform("jtp.dumpSig", new BodyTransformer() {
                    @Override
                    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
                        System.out.println(b.getMethod().getSignature());
                    }
                }
                ));
        soot.Main.main(new String[]{
                "-allow-phantom-refs",
                "-process-dir", args[0],
                "-output-format", "n"
        });
    }
}
