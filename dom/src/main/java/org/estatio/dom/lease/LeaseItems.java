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

import java.math.BigInteger;
import java.util.List;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NotContributed;
import org.apache.isis.applib.annotation.Prototype;

import org.estatio.dom.EstatioDomainService;
import org.estatio.dom.charge.Charge;
import org.estatio.dom.invoice.PaymentMethod;

@Hidden
public class LeaseItems extends EstatioDomainService<LeaseItem> {

    public LeaseItems() {
        super(LeaseItems.class, LeaseItem.class);
    }

    // //////////////////////////////////////

    @ActionSemantics(Of.NON_IDEMPOTENT)
    @NotContributed
    public LeaseItem newLeaseItem(
            final Lease lease,
            final LeaseItemType type,
            final Charge charge,
            final InvoicingFrequency invoicingFrequency,
            final PaymentMethod paymentMethod) {
        LeaseItem leaseItem = newTransientInstance();
        leaseItem.setType(type);
        leaseItem.setCharge(charge);
        leaseItem.setPaymentMethod(paymentMethod);
        leaseItem.setInvoicingFrequency(invoicingFrequency);
        leaseItem.setLease(lease);
        persistIfNotAlready(leaseItem);
        return leaseItem;
    }

    // //////////////////////////////////////

    @Prototype
    @ActionSemantics(Of.SAFE)
    @MemberOrder(sequence = "99")
    public List<LeaseItem> allLeaseItems() {
        return allInstances();
    }

    // //////////////////////////////////////

    @Hidden
    @ActionSemantics(Of.SAFE)
    public LeaseItem findLeaseItem(
            final Lease lease, final LeaseItemType type, final LocalDate startDate, final BigInteger sequence) {
        return firstMatch("findByLeaseAndTypeAndStartDateAndSequence", 
                "lease", lease, 
                "type", type, 
                "startDate", startDate, 
                "sequence", sequence);
    }

}
