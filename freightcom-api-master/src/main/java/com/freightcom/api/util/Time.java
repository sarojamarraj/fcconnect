package com.freightcom.api.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Time
{
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd-HH-mm")
                .withZone(ZoneId.of("UTC"));

    public static String timestamp()
    {
        return formatter.format(Instant.now());
    }
}
