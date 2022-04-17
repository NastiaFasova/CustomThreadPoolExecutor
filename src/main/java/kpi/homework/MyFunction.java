package kpi.homework;

@FunctionalInterface
public interface MyFunction<T, R> {
    R longRunningTask(T x);
}
