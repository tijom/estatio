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
package org.estatio.dom.numerator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.apache.isis.applib.query.Query;
import org.apache.isis.applib.services.bookmark.Bookmark;
import org.apache.isis.applib.services.bookmark.BookmarkService;
import org.apache.isis.core.commons.matchers.IsisMatchers;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2.Ignoring;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2.Mode;

import org.estatio.dom.FinderInteraction;
import org.estatio.dom.FinderInteraction.FinderMethod;
import org.estatio.dom.asset.Property;
import org.estatio.dom.invoice.Constants;

public class NumeratorsTest_finders {

    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);

    private FinderInteraction finderInteraction;

    private Numerators numerators;
    
    @Mock
    private BookmarkService mockBookmarkService;

    @Ignoring
    @Mock
    Property mockProperty;

    private Bookmark propertyBookmark;

    @Before
    public void setup() {

        propertyBookmark = new Bookmark("PROP", "123");
        
        context.checking(new Expectations() {
            {
                allowing(mockBookmarkService).bookmarkFor(mockProperty);
                will(returnValue(propertyBookmark));
            }
        });
        numerators = new Numerators() {

            @Override
            protected <T> T firstMatch(Query<T> query) {
                finderInteraction = new FinderInteraction(query, FinderMethod.FIRST_MATCH);
                return null;
            }
            @Override
            protected List<Numerator> allInstances() {
                finderInteraction = new FinderInteraction(null, FinderMethod.ALL_INSTANCES);
                return null;
            }
            @Override
            protected <T> List<T> allMatches(Query<T> query) {
                finderInteraction = new FinderInteraction(query, FinderMethod.ALL_MATCHES);
                return null;
            }
        };
        numerators.injectBookmarkService(mockBookmarkService);
    }

    @Test
    public void findNumeratorByType() {

        numerators.findScopedNumerator(Constants.INVOICE_NUMBER_NUMERATOR_NAME, mockProperty);
        
        assertThat(finderInteraction.getFinderMethod(), is(FinderMethod.FIRST_MATCH));
        assertThat(finderInteraction.getResultType(), IsisMatchers.classEqualTo(Numerator.class));
        assertThat(finderInteraction.getQueryName(), is("findByNameAndObjectTypeAndObjectIdentifier"));
        assertThat(finderInteraction.getArgumentsByParameterName().get("name"), is((Object)Constants.INVOICE_NUMBER_NUMERATOR_NAME));
        assertThat(finderInteraction.getArgumentsByParameterName().get("objectType"), is((Object)"PROP"));
        assertThat(finderInteraction.getArgumentsByParameterName().get("objectIdentifier"), is((Object)"123"));

        assertThat(finderInteraction.getArgumentsByParameterName().size(), is(3));
    }
    

    @Test
    public void allNumerators() {
        
        numerators.allNumerators();
        
        assertThat(finderInteraction.getFinderMethod(), is(FinderMethod.ALL_INSTANCES));
    }
    
}
