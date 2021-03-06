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
package org.estatio.dom.invoice.publishing;

import java.util.List;

import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.PublishedAction.PayloadFactory;
import org.apache.isis.applib.services.publish.EventPayload;

import org.estatio.dom.invoice.Invoice;

public class InvoiceEagerlyRenderedPayloadFactory implements PayloadFactory {

    @Override
    @Programmatic
    public EventPayload payloadFor(
            final Identifier actionIdentifier, 
            final Object target, final List<Object> arguments, 
            final Object result) {
        return new InvoiceEagerlyRenderedPayload(actionIdentifier, (Invoice)target, arguments, result);
    }

}
