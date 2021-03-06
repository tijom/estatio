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
package org.estatio.dom.agreement;

import java.util.List;

import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.NotContributed;

import org.estatio.dom.EstatioDomainService;

@Hidden
public class AgreementRoleTypes extends EstatioDomainService<AgreementRoleType> {

    public AgreementRoleTypes() {
        super(AgreementRoleTypes.class, AgreementRoleType.class);
    }
    
    // //////////////////////////////////////


    @NotContributed
    public AgreementRoleType findByTitle(final String title) {
        return firstMatch("findByTitle", "title", title);
    }

    @NotContributed
    public List<AgreementRoleType> findApplicableTo(final AgreementType agreementType) {
        return allMatches("findByAgreementType", "agreementType", agreementType);
    }


}
