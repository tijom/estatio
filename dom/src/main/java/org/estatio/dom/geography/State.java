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
package org.estatio.dom.geography;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Immutable;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.annotation.Title;

import org.estatio.dom.EstatioMutableObject;
import org.estatio.dom.JdoColumnLength;
import org.estatio.dom.WithNameUnique;
import org.estatio.dom.WithReferenceComparable;
import org.estatio.dom.WithReferenceUnique;

/**
 * Represents a geographic {@link State} {@link #getCountry() within} a {@link Country}.
 */
@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=IdGeneratorStrategy.NATIVE, 
        column="id")
@javax.jdo.annotations.Version(
        strategy = VersionStrategy.VERSION_NUMBER,
        column = "version")
@javax.jdo.annotations.Uniques({
    @javax.jdo.annotations.Unique(
            name = "Geography_reference_UNQ", members="reference"),
    @javax.jdo.annotations.Unique(
            name = "Geography_name_UNQ", members="name")
})
@javax.jdo.annotations.Queries({
    @javax.jdo.annotations.Query(
            name = "findByCountry", language = "JDOQL",
            value = "SELECT "
                    + "FROM org.estatio.dom.geography.State "
                    + "WHERE country == :country"),
    @javax.jdo.annotations.Query(
            name = "findByReference", language = "JDOQL",
            value = "SELECT "
                    + "FROM org.estatio.dom.geography.State "
                    + "WHERE reference == :reference")
})
@Immutable
public class State 
    extends EstatioMutableObject<State> 
    implements WithReferenceComparable<State>, WithReferenceUnique, WithNameUnique {

    public State() {
        super("reference");
    }
    
    // //////////////////////////////////////

    private String reference;

    /**
     * As per ISO standards for <a href=
     * "http://www.commondatahub.com/live/geography/country/iso_3166_country_codes"
     * >countries</a> and <a href=
     * "http://www.commondatahub.com/live/geography/state_province_region/iso_3166_2_state_codes"
     * >states</a>.
     */
    @javax.jdo.annotations.Column(allowsNull="false", length=JdoColumnLength.State.REFERENCE)
    @RegEx(validation = "[-/_A-Z0-9]+", caseSensitive=true)
    public String getReference() {
        return reference;
    }

    public void setReference(final String reference) {
        this.reference = reference;
    }


    // //////////////////////////////////////

    private String name;

    @javax.jdo.annotations.Column(allowsNull="false", length=JdoColumnLength.NAME)
    @Title
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
    
    // //////////////////////////////////////

    private Country country;

    @javax.jdo.annotations.Column(name="countryId", allowsNull="false")
    public Country getCountry() {
        return country;
    }

    public void setCountry(final Country country) {
        this.country = country;
    }


}
