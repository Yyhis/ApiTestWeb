package net.yyhis.apitester.util;

public class ThreadUtil {
    public static void startThread(Runnable runnable) throws InterruptedException {
        Thread thread = Thread.ofVirtual()
                .name("Running Thread")
                .start(() -> System.out.println("Hello"));

        thread.join();
    }
}
