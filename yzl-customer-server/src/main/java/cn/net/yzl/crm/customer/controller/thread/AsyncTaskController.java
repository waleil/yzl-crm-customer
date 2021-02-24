package cn.net.yzl.crm.customer.controller.thread;


import cn.net.yzl.crm.customer.service.thread.IAsyncService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


@Slf4j
@Api(value="customer线程池",tags = {"customer线程池"})
@RestController
@RequestMapping("asyncTask")
public class AsyncTaskController {


    @Autowired
    IAsyncService executeAsync;

    @GetMapping("func")
    public void testFunctionalExecutor() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        for (int i = 0; i < 3; i++) {
            executeAsync.executeAsync(() -> {
                int random = (int) (2 + Math.random() * 8);
                log.info("线程睡{}秒", random);
                Thread.sleep(random * 1000);
                latch.countDown();
                log.info("子线程睡{}秒,执行完毕", random);
            });
        }
        latch.await();
        log.info("主线程执行完毕");



    }

    @GetMapping("func2")
    public void AsyncTaskTest() throws InterruptedException, ExecutionException {
        Future<String> task1 = executeAsync.doTask1();
        Future<String> task2 = executeAsync.doTask2();

        while(true) {
            if(task1.isDone() && task2.isDone()) {
                log.info("Task1 result: {}", task1.get());
                log.info("Task2 result: {}", task2.get());
                break;
            }
            Thread.sleep(1000);
        }

        log.info("All tasks finished.");
    }
}
