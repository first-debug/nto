package com.vladislav.domain;

import java.text.SimpleDateFormat;

public record DateFormatRepo() {

    static final SimpleDateFormat primaryDateFormat = new SimpleDateFormat("HH:mm dd.MM.yyyy");
    static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
}
