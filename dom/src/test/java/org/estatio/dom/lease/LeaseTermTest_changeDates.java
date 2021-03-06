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
package org.estatio.dom.lease;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import org.estatio.dom.WithIntervalMutable;
import org.estatio.dom.contracttests.AbstractWithIntervalMutableContractTest_changeDates;

public class LeaseTermTest_changeDates extends AbstractWithIntervalMutableContractTest_changeDates<LeaseTerm> {

    private LeaseTerm leaseTerm;

    @Before
    public void setUp() throws Exception {
        leaseTerm = withIntervalMutable;
    }
    
    protected LeaseTerm doCreateWithIntervalMutable(final WithIntervalMutable.Helper<LeaseTerm> mockChangeDates) {
        return new LeaseTerm() {
            @Override
            org.estatio.dom.WithIntervalMutable.Helper<LeaseTerm> getChangeDates() {
                return mockChangeDates;
            }
            @Override
            public BigDecimal getTrialValue() {
                return null;
            }
            @Override
            public BigDecimal getApprovedValue() {
                return null;
            }
        };
    }
    
    // //////////////////////////////////////

    @Test
    public void changeDatesDelegate() {
        leaseTerm = new LeaseTerm(){
            @Override
            public BigDecimal getTrialValue() {
                return null;
            }
            @Override
            public BigDecimal getApprovedValue() {
                return null;
            }};
        assertThat(leaseTerm.getChangeDates(), is(not(nullValue())));
    }

}
