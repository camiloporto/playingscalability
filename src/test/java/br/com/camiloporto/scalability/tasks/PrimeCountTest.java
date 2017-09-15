package br.com.camiloporto.scalability.tasks;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by camiloporto on 9/10/17.
 */
public class PrimeCountTest {

    @Test
    public void shouldCountNumberOfPrimeNumbersInARange() {
        CPUIntensiveTask primeCountTask = new CPUIntensiveTask(10L);
        byte[] response = primeCountTask.execute();
        Assert.assertEquals("5", new String(response));
    }

    @Test
    public void testCPUActivityWithHugeNumber() {
        CPUIntensiveTask primeCountTask = new CPUIntensiveTask(200000L);
        long t1 = System.currentTimeMillis();
        byte[] response = primeCountTask.execute();
        long t2 = System.currentTimeMillis();
        System.err.println("elapsed: " + ((t2-t1)/1000) + "s");
    }
}
