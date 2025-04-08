package net.yyhis.apitester.service;

import java.util.concurrent.*;

public class AsyncRequestExecutor {
    // 큐 및 스레드 풀 설정
    private static final int MAX_THREAD_POOL_SIZE = 10; // 최대 스레드 풀 크기
    private static final BlockingQueue<Runnable> requestQueue = new LinkedBlockingQueue<>();
    private static final ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREAD_POOL_SIZE);

    // 요청 제한을 위한 세마포어 설정
    private static final int MAX_CONCURRENT_REQUESTS = 10; // 최대 동시 요청 수
    private static final Semaphore semaphore = new Semaphore(MAX_CONCURRENT_REQUESTS);

    public static void submitRequest(Runnable request) {
        requestQueue.offer(request); // 요청을 큐에 추가
    }

    public static void processRequests() {
        while (!requestQueue.isEmpty()) {
            Runnable request = requestQueue.poll();
            if (request != null) {
                CompletableFuture.runAsync(request, executorService) // 비동기 실행
                        .exceptionally(ex -> {
                            System.err.println("Error processing request: " + ex.getMessage());
                            return null;
                        });
            }
        }
    }

    public static CompletableFuture<?> sendLimitedRequestAsync(Runnable runnable) {
        return CompletableFuture.supplyAsync(() -> {
            int availablePermits = semaphore.availablePermits(); // 현재 사용 가능한 세마포어 수
            int executing = Math.min(availablePermits, MAX_CONCURRENT_REQUESTS); // 실행되는 요청 수

            System.out.println("Executing: " + executing);

            try {
                semaphore.acquire(); // 요청 제한
                runnable.run(); // 요청 실행
                return null; 
            } catch (Exception e) {
                System.err.println("실행 실패(thread): " + e.getMessage());
                return null;
            } finally {
                semaphore.release(); // 처리 후 반환
            }
        }, executorService);
    }

    public static void shutdown() {
        executorService.shutdown();
        try {
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}