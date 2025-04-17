package org.abror.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class WorkingHoursTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static WorkingHours getWorkingHoursSample1() {
        return new WorkingHours().id(1L);
    }

    public static WorkingHours getWorkingHoursSample2() {
        return new WorkingHours().id(2L);
    }

    public static WorkingHours getWorkingHoursRandomSampleGenerator() {
        return new WorkingHours().id(longCount.incrementAndGet());
    }
}
