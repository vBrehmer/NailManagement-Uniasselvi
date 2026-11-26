package com.godev.nailManagement.domain.client.queryDtos;

import java.time.LocalTime;

public record OftenWeekdaysAndTime(
        Integer weekday,
        LocalTime hour,
        Long total
) {
    public Integer weekday() {
        return weekday == 0 ? 7 : weekday;
    }
}
