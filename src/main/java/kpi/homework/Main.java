package kpi.homework;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

        CustomExecutor<Integer, Integer> exec = new CustomExecutor<>(2);
        MyFunction<Integer, Integer> myFunction = x -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return x * 2;
        };

        List<FutureResult<Integer>> futures = exec.map(myFunction, List.of(1, 2, 3, 4));
        for (FutureResult<Integer> future : futures) {
            TimeUnit.SECONDS.sleep(2);
            System.out.println(LocalTime.now().format(dtf) + " - " + future.result());
            System.out.println(future.result());
        }
        exec.shutdown();
        for (FutureResult<Integer> future : futures) {
            TimeUnit.SECONDS.sleep(2);
            System.out.println(LocalTime.now().format(dtf) + " - " + future.result());
            System.out.println(future.result());
        }
    }
}
