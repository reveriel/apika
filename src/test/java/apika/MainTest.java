package apika;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by guoxing on 1/4/2017.
 */
public class MainTest {
    @Test
    public void main() throws Exception {


        String[] args = new String[10];

 //      args[0] = ""

        File resourcesDir = new File("src/test/resources");


        final String JAVA_HOME = System.getenv("JAVA_HOME");
        final String CP = JAVA_HOME + "/jre/lib/rt.jar:" + JAVA_HOME + "/jre/lib/jsse.jar:.";


        Main.main(new String[] {
                "-cp",
                CP,
                "-w",
                "-x",
                "android",
                "-allow-phantom-refs",
                "-process-dir",
                "src/test/resources/app-release-unsigned.apk"
        });
    }
}