package chapter7;

/**
 * Created by simjunbo on 2018-02-26.
 */
public class ForkJoinSumCalculator extends java.util.concurrent.RecursiveTask<Long> {

    private final long[] numbers;
    private final int start;
    private final int end;
    public static final long THRESHOLD = 1000;

    public ForkJoinSumCalculator(long[] numbers) {
        this(numbers, 0, numbers.length);
    }

    private ForkJoinSumCalculator(long[] numbers, int start, int end) {
        this.numbers = numbers;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        int length = end - start;
        if (length <= THRESHOLD) {
            return computeSequentially();
        }
        ForkJoinSumCalculator leftTask =
                new ForkJoinSumCalculator(numbers, start, start + length / 2);

        leftTask.fork(); // 비동기로 왼쪽 서브테스크1

        ForkJoinSumCalculator rightTask =
                new ForkJoinSumCalculator(numbers, start + length / 2, end);

        Long rightResult = rightTask.compute(); // 동기로 오른쪽 서브테스크2
        Long leftResult = leftTask.join();      // 왼쪽 취함

        System.out.println(Thread.currentThread().getName());
        return leftResult + rightResult;
    }

    private long computeSequentially() {
        long sum = 0;
        for (int i = start; i < end; i++) {
                sum += numbers[i];
        }
        return sum;
    }
}
