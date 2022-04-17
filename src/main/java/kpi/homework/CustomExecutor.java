package kpi.homework;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class CustomExecutor<T, R> {
    private final List<WorkerThread<T, R>> workerThreads;
    private final LinkedBlockingQueue<WorkerItem<T, R>> queue;

    public CustomExecutor(int poolSize) {
        workerThreads = new ArrayList<>(poolSize);
        queue = new LinkedBlockingQueue<>();
        initWorkers(workerThreads, poolSize);
    }

    private void initWorkers(List<WorkerThread<T, R>> workerThreads, int poolSize) {
        for (int i = 0; i < poolSize; i++) {
            WorkerThread<T, R> thread = new WorkerThread<>(queue);
            workerThreads.add(thread);
        }
    }

    public FutureResult<R> execute(MyFunction<T, R> function, T arg) {
        FutureResult<R> future = new FutureResult<>();
        WorkerItem<T, R> workerItem = WorkerItem.<T, R>builder()
                .arg(arg)
                .future(future)
                .myFunction(function).build();

        queue.add(workerItem);
        executeWorker();
        return future;
    }

    public void shutdown() {
        System.out.println("Shutting down thread pool");
        for (WorkerThread<T, R> workerThread : workerThreads) {
            workerThread = null;
        }
    }

    public List<FutureResult<R>> map(MyFunction<T, R> function, List<T> args) {
        List<FutureResult<R>> futures = new ArrayList<>();
        for (T arg : args) {
            FutureResult<R> future = execute(function, arg);
            futures.add(future);
        }
        return futures;
    }

    private void executeWorker() {
        for (WorkerThread<T, R> worker : workerThreads) {
            if (!worker.isAlive()) {
                worker.start();
            }
        }
    }
}
