package com.tmt.common.jwt;

import java.util.Date;

import com.tmt.common.jwt.interfaces.Clock;

final class ClockImpl implements Clock {

    ClockImpl() {}

    @Override
    public Date getToday() {
        return new Date();
    }
}
