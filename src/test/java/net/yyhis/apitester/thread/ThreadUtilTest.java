package net.yyhis.apitester.thread;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import net.yyhis.apitester.service.AsyncRequestExecutor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class ThreadUtilTest {

    @Test
    public void testThreadExecution() throws InterruptedException {
        int taskSize = 10; // 스레드 풀 크기 설정
        CountDownLatch latch = new CountDownLatch(taskSize); // 테스트 완료를 기다리기 위한 래치

        Runnable testRunnable = () -> {
            System.out.println("Executing thread: " + Thread.currentThread().getName());
            latch.countDown(); // 작업 완료 시 래치 감소
        };

        for (int i = 0; i < taskSize; i++) {
            AsyncRequestExecutor.submitRequest(testRunnable);
        }
        AsyncRequestExecutor.processRequests(); // 요청 처리

        // 일정 시간 대기 후 모든 작업이 완료되었는지 검증
        assertTrue(latch.await(3, TimeUnit.SECONDS), "모든 스레드가 실행되지 않았습니다.");
    }

    @Test
    public void testRateLimiting() throws InterruptedException {
        int requestCount = 44;
        CountDownLatch latch = new CountDownLatch(requestCount);

        for (int i = 0; i < requestCount; i++) {
            AsyncRequestExecutor.sendLimitedRequestAsync(() -> {
                System.out.println("Thread executed: " + Thread.currentThread().getName());
                latch.countDown();
            });
        }

        assertTrue(latch.await(5, TimeUnit.SECONDS), "모든 요청이 실행되지 않았습니다.");
    }
}
