package com.hzwl.rental.utils;

import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

public class FileUploadDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {


        ExecutorService executorService = new ThreadPoolExecutor(8, 20, 20000,
                TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(20),
                Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        List<String> list = new ArrayList<>();
        list.add(new Date().getTime()+"1");
        list.add(new Date().getTime()+"2");
        list.add(new Date().getTime()+"3");

        List<CompletableFuture<String>> ls = new ArrayList<>();
        for (String str : list) {
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> Thread.currentThread().getName()+":"+str, executorService);
            ls.add(future);
        }
        CompletableFuture.allOf(ls.toArray(new CompletableFuture[0])).join();



       // 检查各个CompletableFuture是否已完成
        for (CompletableFuture<String> future : ls) {
            future.join();
            System.out.println("Result: " + future.get());
        }
        executorService.shutdown();

    }
}
