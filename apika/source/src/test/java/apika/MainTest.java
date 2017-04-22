package apika;

import org.junit.Test;
import soot.*;

import java.io.File;

/**
 * Created by guoxing on 1/4/2017.
 */
public class MainTest {

    String sep = File.separator;
    String testApkDir = "src" + sep
            + "test" + sep
            + "resources" + sep;

    @Test
    public void test1() throws Exception {
        Main.main(new String[]{testApkDir + "app-release-unsigned.apk"});
    }

    /**
     * Statistic information is not cleared after running
     * error occurs when processing two apk in one run;
     */
//    @Test
//    public void test2() throws Exception {
//        Main.main(new String[]{testApkDir + "app-debug.apk"});
//    }


}