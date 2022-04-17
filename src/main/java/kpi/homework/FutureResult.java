package kpi.homework;

import lombok.Data;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Data
public class FutureResult<R> {
    private boolean hasResult;
    private R result;
    private Lock lock;
    private Condition stackEmptyCondition;
    private Condition stackFullCondition;

    public FutureResult() {
        this.lock = new ReentrantLock();
        stackEmptyCondition = lock.newCondition();
        stackFullCondition = lock.newCondition();
    }

    public void setResult(R result) {
        lock.lock();
        try {
            this.hasResult = true;
            this.result = result;
        } finally {
            lock.unlock();
        }
    }

    public R result() {
        lock.lock();
        try {
            while (!hasResult) {
                stackFullCondition.await();
            }
            stackEmptyCondition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return result;
    }
}
