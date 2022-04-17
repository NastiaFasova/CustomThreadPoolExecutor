package kpi.homework;

import lombok.Getter;
import java.util.concurrent.LinkedBlockingQueue;

@Getter
public class WorkerThread<T, R> extends Thread {
    private final LinkedBlockingQueue<WorkerItem<T, R>> queue;

    public WorkerThread(LinkedBlockingQueue<WorkerItem<T, R>> queue) {
        this.queue = queue;
    }

    public void run() {
        WorkerItem<T, R> item;
        while ((item = queue.poll()) != null) {
            MyFunction<T, R> myFunction = item.getMyFunction();
            R res = myFunction.longRunningTask(item.getArg());
            item.getFuture().setResult(res);
        }
    }
}
