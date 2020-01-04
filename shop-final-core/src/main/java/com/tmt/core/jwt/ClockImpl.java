package com.tmt.core.jwt;

import java.util.Date;

import com.tmt.core.jwt.interfaces.Clock;

final class ClockImpl implements Clock {

    ClockImpl() {}

    @Override
    public Date getToday() {
        return new Date();
    }
}
