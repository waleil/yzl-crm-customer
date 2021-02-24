package cn.net.yzl.crm.customer.service.thread;

import java.util.concurrent.Future;

public interface IAsyncService {

    void executeAsync(ExecutorFunctional target);

    public Future<String> doTask1() throws InterruptedException;

    public Future<String> doTask2() throws InterruptedException;
}
