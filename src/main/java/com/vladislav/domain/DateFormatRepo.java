package com.vladislav.domain;

import java.text.SimpleDateFormat;

public record DateFormatRepo() {

    static SimpleDateFormat primaryDateFormat = new SimpleDateFormat("HH:mm dd.MM.yyyy");
    static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
}
