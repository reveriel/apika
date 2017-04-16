package apika;

import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Created by guoxing on 1/4/2017.
 */
public class MainTest {

    @Test
    public void main() throws Exception {
//        Main.main(new String[]{"-process-dir", "src/test/resources/app-release-unsigned.apk"});
        Main.main(new String[]{"src/test/resources/app-release-unsigned.apk"});
    }


}