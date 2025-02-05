package org.example;

import java.util.Random;
import java.util.concurrent.*;

public class MultiThreadedArraySum {
    private static final int ARRAY_SIZE = 1_000_000;
    private static final int NUM_THREADS = 4;

    public static void main(String[] args) {
        int[] array = generateRandomArray(ARRAY_SIZE);
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

        int chunkSize = ARRAY_SIZE / NUM_THREADS;
        Future<Integer>[] results = new Future[NUM_THREADS];

        for (int i = 0; i < NUM_THREADS; i++) {
            int start = i * chunkSize;
            int end = (i == NUM_THREADS - 1) ? ARRAY_SIZE : start + chunkSize;
            results[i] = executor.submit(new SumTask(array, start, end));
        }

        int totalSum = 0;
        try {
            for (Future<Integer> result : results) {
                totalSum += result.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        executor.shutdown();

        System.out.println("Total Sum: " + totalSum);
    }

    private static int[] generateRandomArray(int size) {
        Random random = new Random();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(100); // Генеруємо числа від 0 до 99
        }
        return array;
    }

    static class SumTask implements Callable<Integer> {
        private final int[] array;
        private final int start;
        private final int end;

        SumTask(int[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        public Integer call() {
            int sum = 0;
            for (int i = start; i < end; i++) {
                sum += array[i];
            }
            return sum;
        }
    }
}
