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
package org.estatio.services.audit;

import java.util.List;

import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.services.audit.AuditingService;

import org.estatio.dom.EstatioDomainService;


@Named("Audit")
@Hidden
public class AuditingServiceForEstatio extends EstatioDomainService<AuditEntryForEstatio> implements AuditingService {

    public AuditingServiceForEstatio() {
        super(AuditingServiceForEstatio.class, AuditEntryForEstatio.class);
    }
    
    public List<AuditEntryForEstatio> list() {
        return allInstances();
    }

    @Override
    @Programmatic
    public void audit(
            final String user, 
            final long currentTimestampEpoch, 
            final String objectType, final String objectIdentifier, 
            final String preValue, final String postValue) {
        AuditEntryForEstatio auditEntry = newTransientInstance();
        auditEntry.setTimestampEpoch(currentTimestampEpoch);
        auditEntry.setUser(user);
        auditEntry.setObjectType(objectType);
        auditEntry.setIdentifier(objectIdentifier);
        auditEntry.setPreValue(preValue);
        auditEntry.setPostValue(postValue);
        persist(auditEntry);
    }
}
