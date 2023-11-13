package com.hzwl.rental.utils;

import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * @Author GA666666
 * @Date 2023/9/11 16:17
 */
@Component
public class ThreadPoolUtils {
    private final ExecutorService executorService = new ThreadPoolExecutor(2, 8, 20000,
            TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(500),
            Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());


    public ExecutorService getExecutorService() {
        return executorService;
    }

}
