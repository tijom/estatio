/*
 *
 *  Copyright 2012-2013 Eurocommercial Properties NV
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.estatio.services.clock;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.clock.Clock;

@Hidden
public class ClockService {

    private static final int MONTHS_IN_QUARTER = 3;

    public LocalDate now() {
        return Clock.getTimeAsLocalDate();
    }

    // //////////////////////////////////////

    public LocalDate beginningOfMonth() {
        return beginningOfMonth(now());
    }

    static LocalDate beginningOfMonth(final LocalDate date) {
        final int dayOfMonth = date.getDayOfMonth();
        return date.minusDays(dayOfMonth-1);
    }

    // //////////////////////////////////////

    public LocalDate beginningOfQuarter() {
        final LocalDate date = now();
        return beginningOfQuarter(date);
    }

    static LocalDate beginningOfQuarter(final LocalDate date) {
        final LocalDate beginningOfMonth = beginningOfMonth(date);
        final int monthOfYear = beginningOfMonth.getMonthOfYear();
        final int quarter = (monthOfYear-1)/MONTHS_IN_QUARTER; // 0, 1, 2, 3
        final int monthStartOfQuarter = quarter*MONTHS_IN_QUARTER+1;
        return beginningOfMonth.minusMonths(monthOfYear-monthStartOfQuarter);
    }

}
