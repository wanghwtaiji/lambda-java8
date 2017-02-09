package com.example;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * Created by qiuzhanghua on 2017/2/9.
 */
public class Concurrency {
  public static void main(String[] args) {
    Runnable task = () -> {
      String threadName = Thread.currentThread().getName();
      System.out.println("所在线程 " + threadName);
    };

    task.run();  // 直接调用一下

    Thread thread = new Thread(task);
    thread.start();

    System.out.println("完成！");


//    ExecutorService executor = Executors.newSingleThreadExecutor();
   ExecutorService executor = Executors.newFixedThreadPool(4);
    executor.submit(() -> {
      String threadName = Thread.currentThread().getName();
      System.out.println("所在线程 " + threadName);
    });


    Callable<Integer> task2 = () -> {
      try {
        String threadName = Thread.currentThread().getName();
        System.out.println("所在线程 " + threadName);
        TimeUnit.SECONDS.sleep(2);
        return 123;
      } catch (InterruptedException e) {
        throw new IllegalStateException("中断了", e);
      }
    };

    Future<?> future = executor.submit(task2);

    try {
      System.out.println("Future完成? " + future.isDone());

      Integer result = null;
      // result = (Integer) future.get();
      result = (Integer) future.get(1, TimeUnit.SECONDS);
      System.out.println("Future完成? " + future.isDone());
      System.out.println("结果是: " + result);
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      // e.printStackTrace();
      System.out.println("异常: " + e.toString());
    }


    List<Callable<String>> callables = Arrays.asList(
        () -> "task1",
        () -> "task2",
        () -> "task3");

    try {
      executor.invokeAll(callables)   // invokeAny
          .stream()
          .map(future2 -> {
            try {
              return future2.get();
            } catch (Exception e) {
              throw new IllegalStateException(e);
            }
          })
          .forEach(System.out::println);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }


//    ScheduledExecutorService executor2 = Executors.newScheduledThreadPool(1);
//    Runnable task3 = () -> System.out.println("Scheduling: " + System.nanoTime());
//    ScheduledFuture<?> future3 = executor2.schedule(task3, 3, TimeUnit.SECONDS);
//    try {
//      TimeUnit.MILLISECONDS.sleep(1337);
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    }
//    long remainingDelay = future3.getDelay(TimeUnit.MILLISECONDS);
//    System.out.printf("Remaining Delay: %sms\n", remainingDelay);
//
//    int initialDelay = 0;
//    int period = 1;
//    executor2.scheduleAtFixedRate(task3, initialDelay, period, TimeUnit.SECONDS);
//    stop(executor2);


    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    IntStream.range(0, 10000)
        .forEach(i -> executor.submit(Concurrency::increment));

    stop(executor);

    System.out.println(count); // 不是10000

  }

  static int count = 0;

  // 加上synchronized或者启用lock，或者使用AtomicInteger 再看结果是否正确
  static  ReentrantLock lock = new ReentrantLock();
  static void  increment() {

    count = count + 1;

//    lock.lock();
//    try {
//      count++;
//    } finally {
//      lock.unlock();
//    }
  }


  public static void stop(ExecutorService executor) {
    try {
      System.out.println("关闭Executor");
      executor.shutdown();
      executor.awaitTermination(5, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      System.err.println("任务中断了");
    } finally {
      if (!executor.isTerminated()) {
        System.err.println("取消未完成的任务");
      }
      executor.shutdownNow();
      System.out.println("关闭Excutor完成。");
    }

  }
}


// from http://winterbe.com/posts/2015/04/07/java8-concurrency-tutorial-thread-executor-examples/
