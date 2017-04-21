package apika;

import org.junit.Test;

import java.io.File;

/**
 * Created by guoxing on 21/4/2017.
 */
public class MainTest2 {

    String sep = File.separator;
    String testApkDir = "src" + sep
            + "test" + sep
            + "resources" + sep;

//    @Test
//    public void test1() throws Exception {
//        Main.main(new String[]{testApkDir + "app-release-unsigned.apk"});
//    }

    /**
     * Statistic information is not cleared after running
     * error occurs when processing two apk in one run;
     *
     * split in two file, also has error
     */
//    @Test
//    public void test2() throws Exception {
//        Main.main(new String[]{testApkDir + "app-debug.apk"});
//    }
}
