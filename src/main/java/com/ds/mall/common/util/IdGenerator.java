package com.ds.mall.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public final class IdGenerator {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    private IdGenerator() {
    }

    public static String orderNo() {
        return "DS" + LocalDateTime.now().format(FORMATTER) + ThreadLocalRandom.current().nextInt(1000, 9999);
    }
}
