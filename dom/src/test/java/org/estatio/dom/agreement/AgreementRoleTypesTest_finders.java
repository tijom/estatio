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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.query.Query;
import org.apache.isis.core.commons.matchers.IsisMatchers;

import org.estatio.dom.FinderInteraction;
import org.estatio.dom.FinderInteraction.FinderMethod;

public class AgreementRoleTypesTest_finders {

    private FinderInteraction finderInteraction;

    private AgreementRoleTypes agreementRoleTypes;

    private AgreementType agreementType;

    @Before
    public void setup() {
        agreementType = new AgreementType();
        
        agreementRoleTypes = new AgreementRoleTypes() {

            @Override
            protected <T> T firstMatch(Query<T> query) {
                finderInteraction = new FinderInteraction(query, FinderMethod.FIRST_MATCH);
                return null;
            }
            @Override
            protected List<AgreementRoleType> allInstances() {
                finderInteraction = new FinderInteraction(null, FinderMethod.ALL_INSTANCES);
                return null;
            }

            @Override
            protected <T> List<T> allMatches(Query<T> query) {
                finderInteraction = new FinderInteraction(query, FinderMethod.ALL_MATCHES);
                return null;
            }
        };
    }

    
    @Test
    public void findApplicableTo() {

        agreementRoleTypes.findApplicableTo(agreementType);

        // then
        assertThat(finderInteraction.getFinderMethod(), is(FinderMethod.ALL_MATCHES));
        assertThat(finderInteraction.getResultType(), IsisMatchers.classEqualTo(AgreementRoleType.class));
        assertThat(finderInteraction.getQueryName(), is("findByAgreementType"));
        assertThat(finderInteraction.getArgumentsByParameterName().get("agreementType"), is((Object)agreementType));
        assertThat(finderInteraction.getArgumentsByParameterName().size(), is(1));
    }

    @Test
    public void findByTitle() {

        agreementRoleTypes.findByTitle("someTitle");
        
        assertThat(finderInteraction.getFinderMethod(), is(FinderMethod.FIRST_MATCH));
        assertThat(finderInteraction.getResultType(), IsisMatchers.classEqualTo(AgreementRoleType.class));
        assertThat(finderInteraction.getQueryName(), is("findByTitle"));
        assertThat(finderInteraction.getArgumentsByParameterName().get("title"), is((Object)"someTitle"));
        assertThat(finderInteraction.getArgumentsByParameterName().size(), is(1));
    }

}
