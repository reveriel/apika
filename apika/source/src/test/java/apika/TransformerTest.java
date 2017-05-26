package apika;

import org.junit.Test;
import soot.*;

import java.io.File;
import java.util.Map;

/**
 * Created by guoxing on 19/4/2017.
 */


public class TransformerTest {
    public static class DumpSignatureTransformer extends BodyTransformer
    {
        @Override
        protected void internalTransform(Body body, String s, Map<String, String> map) {
            String name = body.getMethod().getName();
            if(name.contains("register")) {
                System.out.println("methodsToCollect.add(\""+
                        body.getMethod().getSignature()+"\");");
            }
        }
    }

    public static class DumpUnitTransformer extends BodyTransformer
    {
        @Override
        protected void internalTransform(Body body, String s, Map<String, String> map) {
            for(Unit u : body.getUnits())
            {
                if(u.toString().contains("register"))
                System.out.println(u.toString());
            }
        }
    }

    //@Test
    public static void main(String[] args) throws Exception {
        System.out.println("tttttttttttttttttttttttttttttt");
        try {
            PackManager.v().getPack("jtp").add(
                    new Transform("jtp.dumpMethod", new TransformerTest.DumpUnitTransformer())
            );
            System.out.println("tttttttttttttttttttttttttttttt");
            soot.Main.main(new String[]{
                    "-cp", "/home/sirning/Desktop/test/app-debug-dex2jar.jar:" +
                    "/home/sirning/Program/Android/Sdk/platforms/data-25/data.jar",
                    "com.example.sirning.deprecatedmethod.MainActivity"
            });
            System.out.println("tttttttttttttttttttttttttttttt");
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
    }
}
