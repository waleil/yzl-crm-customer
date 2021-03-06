package cn.net.yzl.crm.customer.service.thread.impl;

import cn.net.yzl.crm.customer.service.thread.ExecutorFunctional;
import cn.net.yzl.crm.customer.service.thread.IAsyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Slf4j
@Service
public class AsyncServiceImpl implements IAsyncService {
    @Override
    @Async("asyncServiceExecutor")
    public void executeAsync(ExecutorFunctional target) {
        log.info("start executeAsync");
        try {
            target.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("end executeAsync");
    }

    @Override
    @Async("asyncServiceExecutor")
    public Future<String> executeAsync2(ExecutorFunctional target) {
        log.info("start executeAsync");
        try {
            target.execute();
        } catch (Exception e) {
            e.printStackTrace();
            return new AsyncResult<String>("error");
        }
        log.info("end executeAsync");
        return new AsyncResult<String>("success");
    }

    @Override
    @Async("mySimpleAsync")
    public Future<String> doTask1() throws InterruptedException{
        log.info("Task1 started.");
        long start = System.currentTimeMillis();
        Thread.sleep(5000);
        long end = System.currentTimeMillis();

        log.info("Task1 finished, time elapsed: {} ms.", end-start);
        return new AsyncResult<>("Task1 accomplished!");
    }

    @Override
    @Async("myAsync")
    public Future<String> doTask2() throws InterruptedException{
        log.info("Task2 started.");
        long start = System.currentTimeMillis();
        Thread.sleep(3000);
        long end = System.currentTimeMillis();

        log.info("Task2 finished, time elapsed: {} ms.", end-start);

        return new AsyncResult<>("Task2 accomplished!");
    }

}
