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
package org.estatio.integration.tests.lease;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;
import java.util.SortedSet;

import org.hamcrest.core.Is;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import org.estatio.dom.lease.Lease;
import org.estatio.dom.lease.LeaseItem;
import org.estatio.dom.lease.LeaseItemType;
import org.estatio.dom.lease.LeaseTerm;
import org.estatio.dom.lease.LeaseTermForIndexableRent;
import org.estatio.dom.lease.LeaseTermForServiceCharge;
import org.estatio.dom.lease.LeaseTermStatus;
import org.estatio.dom.lease.LeaseTerms;
import org.estatio.dom.lease.Leases;
import org.estatio.dom.lease.Leases.InvoiceRunType;
import org.estatio.fixture.EstatioTransactionalObjectsFixture;
import org.estatio.integration.tests.EstatioIntegrationTest;
import org.estatio.services.settings.EstatioSettingsService;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LeaseTermTest_verify_and_InvoiceItems_calculate extends EstatioIntegrationTest {

    @BeforeClass
    public static void setupTransactionalData() {
        scenarioExecution().install(new EstatioTransactionalObjectsFixture());
    }

    private Leases leases;
    private LeaseTerms leaseTerms;
    private EstatioSettingsService estatioSettingsService;

    private Lease leaseTopModel;
    private LeaseItem leaseTopModelRentItem;
    private LeaseItem leaseTopModelServiceChargeItem;

    @Before
    public void setup() {
        leases = service(Leases.class);
        leaseTerms = service(LeaseTerms.class);
        estatioSettingsService = service(EstatioSettingsService.class);

        leaseTopModel = leases.findLeaseByReference("OXF-TOPMODEL-001");
        assertThat(leaseTopModel.getItems().size(), is(3));

        leaseTopModelRentItem = leaseTopModel.findItem(LeaseItemType.RENT, new LocalDate(2010, 7, 15), BigInteger.valueOf(1));
        leaseTopModelServiceChargeItem = leaseTopModel.findItem(LeaseItemType.SERVICE_CHARGE, new LocalDate(2010, 7, 15), BigInteger.valueOf(1));

        Assert.assertNotNull(leaseTopModelRentItem);
        Assert.assertNotNull(leaseTopModelServiceChargeItem);
    }

    @Test
    public void t08_lease_findItem_whenRent_and_leaseItem_findTerm() throws Exception {

        final SortedSet<LeaseTerm> terms = leaseTopModelRentItem.getTerms();
        Assert.assertThat(terms.size(), is(1));
        final LeaseTerm term0 = terms.first();

        LeaseTermForIndexableRent leaseTopModelRentTerm = (LeaseTermForIndexableRent) leaseTopModelRentItem.findTerm(new LocalDate(2010, 7, 15));
        Assert.assertNotNull(leaseTopModelRentTerm);

        List<LeaseTerm> allLeaseTerms = leaseTerms.allLeaseTerms();
        LeaseTerm term = (LeaseTerm) allLeaseTerms.get(0);

        assertThat(leaseTopModelRentTerm, is(term));
        assertThat(leaseTopModelRentTerm, is(term0));

        // given the first leaseTerm has non-null frequency
        Assert.assertNotNull(term.getFrequency());
        Assert.assertNotNull(term.getFrequency().nextDate(new LocalDate(2012, 1, 1)));

        BigDecimal baseValue = leaseTopModelRentTerm.getBaseValue();
        Assert.assertEquals(new BigDecimal("20000.00"), baseValue);
    }

    @Test
    public void t09_lease_findItem_whenServiceCharge_and_leaseItem_findTerm() throws Exception {

        final SortedSet<LeaseTerm> terms = leaseTopModelServiceChargeItem.getTerms();
        Assert.assertThat(terms.size(), Is.is(1));
        final LeaseTerm term0 = terms.first();

        LeaseTermForServiceCharge leaseTopModelServiceChargeTerm = (LeaseTermForServiceCharge) leaseTopModelServiceChargeItem.findTerm(new LocalDate(2010, 7, 15));

        assertThat(leaseTopModelServiceChargeTerm, is(term0));

        Assert.assertThat(leaseTopModelServiceChargeTerm.getBudgetedValue(), Is.is(new BigDecimal("6000.00")));
    }

    @Test
    public void t10_leaseTermRent_verify() throws Exception {
        // given
        LeaseTermForIndexableRent leaseTopModelRentTerm1 = (LeaseTermForIndexableRent) leaseTopModelRentItem.getTerms().first();

        // when
        leaseTopModelRentTerm1.verifyUntil(new LocalDate(2014, 1, 1));

        // then
        assertThat(leaseTopModelRentTerm1.getBaseIndexValue(), is(BigDecimal.valueOf(137.6).setScale(4)));
        assertThat(leaseTopModelRentTerm1.getNextIndexValue(), is(BigDecimal.valueOf(101.2).setScale(4)));
        assertThat(leaseTopModelRentTerm1.getIndexationPercentage(), is(BigDecimal.valueOf(1).setScale(1)));
        assertThat(leaseTopModelRentTerm1.getIndexedValue(), is(BigDecimal.valueOf(20200).setScale(2)));
    }

    @Test
    public void t10_leaseTermServiceCharge_verify() throws Exception {
        // given
        assertThat(leaseTopModelServiceChargeItem.getTerms().size(), is(1));

        // when
        leaseTopModelServiceChargeItem.getTerms().first().verifyUntil(new LocalDate(2014, 1, 1));

        // then
        SortedSet<LeaseTerm> terms = leaseTopModelServiceChargeItem.getTerms();
        assertNotNull(terms.toString(), leaseTopModelServiceChargeItem.findTerm(new LocalDate(2012, 7, 15)));
    }

    @Test
    public void t11_lease_verify() throws Exception {

        // when
        leaseTopModel.verifyUntil(new LocalDate(2014, 1, 1));

        // then
        assertNotNull(leaseTopModelRentItem.findTerm(new LocalDate(2012, 7, 15)));

        // and when
        leaseTopModelServiceChargeItem.verify();

        // then
        assertNotNull(leaseTopModelServiceChargeItem.findTerm(new LocalDate(2012, 7, 15)));
    }

    @Test
    public void t13_leaseTerm_lock() throws Exception {
        // given
        LeaseTerm term = (LeaseTerm) leaseTopModelRentItem.getTerms().toArray()[0];

        // when
        term.approve();

        // then
        assertThat(term.getStatus(), is(LeaseTermStatus.APPROVED));
        assertThat(term.getApprovedValue(), is(BigDecimal.valueOf(20200).setScale(2)));
    }

    @Test
    public void t141a_leaseTerm_verify_and_calculate() throws Exception {
        // given
        LeaseTerm leaseTopModelRentTerm = leaseTopModelRentItem.findTerm(new LocalDate(2010, 7, 15));

        // when
        leaseTopModelRentTerm.verifyUntil(new LocalDate(2014, 1, 1));
        // and when
        leaseTopModelRentTerm.calculate(new LocalDate(2010, 7, 2), new LocalDate(2010, 7, 1));

        // then
        assertThat(leaseTopModelRentTerm.getInvoiceItems().size(), is(1));
    }

    // scenario: invoiceItemsForRentCreated
    @Test
    public void t14b_invoiceItemsForRentCreated() throws Exception {

        estatioSettingsService.updateEpochDate(null);

        // unapproved doesn't work
        LeaseTerm leaseTopModelRentTerm0 = (LeaseTerm) leaseTopModelRentItem.getTerms().first();
        leaseTopModelRentTerm0.calculate(new LocalDate(2010, 7, 1), new LocalDate(2010, 7, 1));
        Assert.assertNull(leaseTopModelRentTerm0.findUnapprovedInvoiceItemFor(new LocalDate(2010, 7, 1), new LocalDate(2010, 6, 1)));

        // let's approve
        leaseTopModelRentTerm0.approve();
        // partial period
        leaseTopModelRentTerm0.calculate(new LocalDate(2010, 7, 1), new LocalDate(2010, 7, 1));
        assertThat(leaseTopModelRentTerm0.findUnapprovedInvoiceItemFor(new LocalDate(2010, 7, 1), new LocalDate(2010, 7, 1)).getNetAmount(), is(new BigDecimal(4239.13).setScale(2, RoundingMode.HALF_UP)));
        // full term
        leaseTopModelRentTerm0.calculate(new LocalDate(2010, 10, 1), new LocalDate(2010, 10, 1));
        assertThat(leaseTopModelRentTerm0.findUnapprovedInvoiceItemFor(new LocalDate(2010, 10, 1), new LocalDate(2010, 10, 1)).getNetAmount(), is(new BigDecimal(5000.00).setScale(2, RoundingMode.HALF_UP)));
        // invoice after effective date
        leaseTopModelRentTerm0.calculate(new LocalDate(2010, 10, 1), new LocalDate(2011, 4, 1));
        assertThat(leaseTopModelRentTerm0.findUnapprovedInvoiceItemFor(new LocalDate(2010, 10, 1), new LocalDate(2011, 4, 1)).getNetAmount(), is(new BigDecimal(5050.00).setScale(2, RoundingMode.HALF_UP)));
        // invoice after effective date with mock
        estatioSettingsService.updateEpochDate(new LocalDate(2011, 1, 1));
        leaseTopModelRentTerm0.calculate(new LocalDate(2010, 10, 1), new LocalDate(2011, 4, 1));
        assertThat(leaseTopModelRentTerm0.findUnapprovedInvoiceItemFor(new LocalDate(2010, 10, 1), new LocalDate(2011, 4, 1)).getNetAmount(), is(new BigDecimal(50.00).setScale(2, RoundingMode.HALF_UP)));

        // remove
        leaseTopModelRentTerm0.removeUnapprovedInvoiceItemsForDate(new LocalDate(2010, 10, 1), new LocalDate(2010, 10, 1));
        estatioSettingsService.updateEpochDate(null);
        assertThat(leaseTopModelRentTerm0.getInvoiceItems().size(), is(2));
    }

    // scenario: invoiceItemsForServiceChargeCreated
    @Test
    public void t15_invoiceItemsForServiceChargeCreated() throws Exception {

        estatioSettingsService.updateEpochDate(null);

        LeaseTermForServiceCharge leaseTopModelServiceChargeTerm0 = (LeaseTermForServiceCharge) leaseTopModelServiceChargeItem.getTerms().first();
        leaseTopModelServiceChargeTerm0.approve();
        // partial period
        leaseTopModelServiceChargeTerm0.calculate(new LocalDate(2010, 7, 1), new LocalDate(2010, 7, 1));
        assertThat(leaseTopModelServiceChargeTerm0.findUnapprovedInvoiceItemFor(new LocalDate(2010, 7, 1), new LocalDate(2010, 7, 1)).getNetAmount(), is(new BigDecimal(1271.74).setScale(2, RoundingMode.HALF_UP)));
        // full period
        leaseTopModelServiceChargeTerm0.calculate(new LocalDate(2010, 10, 1), new LocalDate(2010, 10, 1));
        assertThat(leaseTopModelServiceChargeTerm0.findUnapprovedInvoiceItemFor(new LocalDate(2010, 10, 1), new LocalDate(2010, 10, 1)).getNetAmount(), is(new BigDecimal(1500.00).setScale(2, RoundingMode.HALF_UP)));
        // reconcile without mock
        leaseTopModelServiceChargeTerm0.calculate(new LocalDate(2010, 10, 1), new LocalDate(2011, 10, 1));
        assertThat(leaseTopModelServiceChargeTerm0.findUnapprovedInvoiceItemFor(new LocalDate(2010, 10, 1), new LocalDate(2011, 10, 1)).getNetAmount(), is(new BigDecimal(1650.00).setScale(2, RoundingMode.HALF_UP)));
        // reconcile with mock date
        estatioSettingsService.updateEpochDate(new LocalDate(2011, 10, 1));
        leaseTopModelServiceChargeTerm0.calculate(new LocalDate(2010, 10, 1), new LocalDate(2011, 10, 1));
        assertThat(leaseTopModelServiceChargeTerm0.findUnapprovedInvoiceItemFor(new LocalDate(2010, 10, 1), new LocalDate(2011, 10, 1)).getNetAmount(), is(new BigDecimal(150.00).setScale(2, RoundingMode.HALF_UP)));
        estatioSettingsService.updateEpochDate(null);
    }

    @Ignore
    @Test
    public void t16_bulkLeaseCalculate() throws Exception {
        leaseTopModelServiceChargeItem = leaseTopModel.findItem(LeaseItemType.SERVICE_CHARGE, new LocalDate(2010, 7, 15), BigInteger.valueOf(1));
        LeaseTermForServiceCharge leaseTopModelServiceChargeTerm0 = (LeaseTermForServiceCharge) leaseTopModelServiceChargeItem.getTerms().first();
        // call calculate on leaseTopModel
        leaseTopModel.calculate(new LocalDate(2010, 10, 1), null, new LocalDate(2010, 10, 1), InvoiceRunType.NORMAL_RUN);
        assertThat(leaseTopModelServiceChargeTerm0.getInvoiceItems().size(), is(2)); // the
                                                                                     // previous
                                                                                     // test
        // already supplied
        // one
    }

}
