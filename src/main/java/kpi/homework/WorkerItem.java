package kpi.homework;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorkerItem<T, R> {
    private MyFunction<T, R> myFunction;
    private T arg;
    private FutureResult<R> future;
}
