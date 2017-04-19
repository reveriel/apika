package apika;

import org.junit.Test;

import java.io.File;

/**
 * Created by guoxing on 1/4/2017.
 */
public class MainTest {

    @Test
    public void main() throws Exception {

        String sep = File.separator;

        String testApk = "src" + sep
                        + "test" + sep
                        + "resources" + sep
                        + "app-release-unsigned.apk";

        File file = new File(testApk);
        if (!file.exists()) {
            throw new RuntimeException("apk file not found");
        }

        apika.Main.main(new String[]{testApk});

//        System.out.println(File.separator);
//        System.out.println(File.separatorChar);
//        System.out.println(File.pathSeparator);
    }


}