package br.com.camiloporto.scalability.tasks;

/**
 * Created by camiloporto on 9/10/17.
 */
public class CPUIntensiveTask implements Task {

    private Long upperBound;

    public CPUIntensiveTask(Long upperBound) {
        this.upperBound = upperBound;
    }

    @Override
    public byte[] execute() {
        return primeCount().toString().getBytes();
    }

    private Long primeCount() {
        Long count = 0L;
        for(long i = 1; i <= upperBound; i++) {
            if(isPrime(i)) {
                count++;
            }
        }
        return count;
    }

    private boolean isPrime(long n) {
        for(long i = 2; i < n; i++) {
            if(n % i == 0) return false;
        }
        return true;
    }
}
