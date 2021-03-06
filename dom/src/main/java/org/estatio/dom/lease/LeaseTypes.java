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

import java.util.List;

import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Programmatic;

import org.estatio.dom.EstatioDomainService;

public class LeaseTypes extends EstatioDomainService<LeaseType> {

    public LeaseTypes() {
        super(LeaseTypes.class, LeaseType.class);
    }

    // //////////////////////////////////////

    @ActionSemantics(Of.SAFE)
    @MemberOrder(name = "Other", sequence = "lease.leaseTypes.1")
    public List<LeaseType> allLeaseTypes() {
        return allInstances();
    }

    @ActionSemantics(Of.NON_IDEMPOTENT)
    @MemberOrder(name = "Other", sequence = "lease.leaseTypes.2")
    public LeaseType newLeaseType(
            final @Named("Reference") String reference,
            final @Named("Name") @Optional String name) {
        final LeaseType leaseType = newTransientInstance();
        leaseType.setReference(reference);
        leaseType.setName(name);
        persistIfNotAlready(leaseType);
        return leaseType;
    }

    // //////////////////////////////////////

    @Programmatic
    public LeaseType findByReference(final String reference) {
        return firstMatch("findByReference", "reference", reference);
    }

    // //////////////////////////////////////

    @Programmatic
    public LeaseType findOrCreate(final String reference, final String name) {
        LeaseType leaseType = findByReference(reference);
        if (leaseType == null) {
            leaseType = newLeaseType(reference, name == null ? reference : name);
        }
        return leaseType;
    }

}
