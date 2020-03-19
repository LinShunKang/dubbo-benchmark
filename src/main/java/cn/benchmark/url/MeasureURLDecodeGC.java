package cn.benchmark.url;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.URLStrParser;
import org.junit.Test;

/**
 * Created by LinShunkang on 2020/03/15
 * <p>
 * -Xmx2G -Xms2G -Xmn1G -javaagent:/path/to/aprof.jar
 */
public class MeasureURLDecodeGC {

    private static final long TEST_TIMES = 20000000L;

    private static String encodedURLStr = "dubbo%3A%2F%2Fadmin%3Aadmin123%40192.168.1.41%3A28113%2Forg.test.api.DemoService%24Iface%3Fanyhost%3Dtrue%26application%3Ddemo-service%26dubbo%3D2.6.1%26generic%3Dfalse%26interface%3Dorg.test.api.DemoService%24Iface%26methods%3DorbCompare%2CcheckText%2CcheckPicture%26pid%3D65557%26revision%3D1.4.17%26service.filter%3DbootMetrics%26side%3Dprovider%26status%3Dserver%26threads%3D200%26timestamp%3D1583136298859%26version%3D1.0.0";

    private static String decodedURLStr = URL.decode(encodedURLStr);

    @Test
    public void testEncodedOriginal() {
        long result = 0L;
        for (int i = 0; i < TEST_TIMES; i++) {
            result += URL.valueOf(URL.decode(encodedURLStr)).hashCode();
        }
        System.out.println("testEncodedOriginal(): TEST_TIMES=" + TEST_TIMES + ", result=" + result);
    }

    @Test
    public void testEncodedOptimize() {
        long result = 0L;
        for (int i = 0; i < TEST_TIMES; i++) {
            result += URLStrParser.parseEncodedStr(encodedURLStr).hashCode();
        }
        System.out.println("testEncodedOptimize(): TEST_TIMES=" + TEST_TIMES + ", result=" + result);
    }

    @Test
    public void testDecodedOriginal() {
        long result = 0L;
        for (int i = 0; i < TEST_TIMES; i++) {
            result += URL.valueOf(decodedURLStr).hashCode();
        }
        System.out.println("testDecodedOriginal(): TEST_TIMES=" + TEST_TIMES + ", result=" + result);
    }

    @Test
    public void testDecodedOptimize() {
        long result = 0L;
        for (int i = 0; i < TEST_TIMES; i++) {
            result += URLStrParser.parseDecodedStr(decodedURLStr).hashCode();
        }
        System.out.println("testDecodedOptimize(): TEST_TIMES=" + TEST_TIMES + ", result=" + result);
    }
}
