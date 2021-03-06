package cn.net.yzl.crm.customer.service.thread;

import java.util.concurrent.Future;

public interface IAsyncService {

    /**
     * 执行异步任务
     * @param target
     */
    void executeAsync(ExecutorFunctional target);


    /**
     * 执行异步任务
     * @param target
     */
    public Future<String> executeAsync2(ExecutorFunctional target);

    public Future<String> doTask1() throws InterruptedException;

    public Future<String> doTask2() throws InterruptedException;
}
