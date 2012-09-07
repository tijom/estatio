package com.eurocommercialproperties.estatio.dom.lease;

import java.math.BigDecimal;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.apache.isis.applib.AbstractDomainObject;
import org.apache.isis.applib.annotation.MemberOrder;
import org.joda.time.LocalDate;

@PersistenceCapable
public class LeaseTerm extends AbstractDomainObject {

    // {{ Lease (property)
    private LeaseItem leaseItem;

    @MemberOrder(sequence = "1")
    public LeaseItem getLeaseItem() {
        return leaseItem;
    }

    public void setLeaseItem(final LeaseItem leaseItem) {
        this.leaseItem = leaseItem;
    }

    // }}

    // {{ StartDate (property)
    private LocalDate startDate;

    @Persistent
    @MemberOrder(sequence = "1")
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(final LocalDate startDate) {
        this.startDate = startDate;
    }

    // }}

    // {{ EndDate (property)
    private LocalDate endDate;

    @Persistent
    @MemberOrder(sequence = "1")
    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(final LocalDate endDate) {
        this.endDate = endDate;
    }
    // }}

    // {{ Value (property)
    private BigDecimal value;

    @MemberOrder(sequence = "1")
    public BigDecimal getValue() {
        return value;
    }

    public void setValue(final BigDecimal value) {
        this.value = value;
    }
    // }}


    
}