package org.abror.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EmployeeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Employee getEmployeeSample1() {
        return new Employee().id(1L).name("name1").specialization("specialization1");
    }

    public static Employee getEmployeeSample2() {
        return new Employee().id(2L).name("name2").specialization("specialization2");
    }

    public static Employee getEmployeeRandomSampleGenerator() {
        return new Employee()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .specialization(UUID.randomUUID().toString());
    }
}
