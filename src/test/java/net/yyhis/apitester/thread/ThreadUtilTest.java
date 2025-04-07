package net.yyhis.apitester.thread;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static net.yyhis.apitester.util.ThreadUtil.startThread;;

@SpringBootTest
public class ThreadUtilTest {

    @Test
    public void testThread() throws InterruptedException {
        startThread(null);
    }
}
