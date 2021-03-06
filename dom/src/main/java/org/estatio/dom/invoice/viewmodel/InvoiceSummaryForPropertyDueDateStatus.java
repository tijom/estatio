/*
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
package org.estatio.dom.invoice.viewmodel;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.List;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.InheritanceStrategy;

import com.google.common.io.BaseEncoding;

import org.jdom2.Element;
import org.joda.time.LocalDate;

import org.apache.isis.applib.AbstractViewModel;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.Immutable;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Render;
import org.apache.isis.applib.annotation.Render.Type;

import org.estatio.dom.asset.Properties;
import org.estatio.dom.asset.Property;
import org.estatio.dom.invoice.Invoice;
import org.estatio.dom.invoice.InvoiceStatus;
import org.estatio.dom.invoice.Invoices;
import org.estatio.dom.utils.Jdom2Util;

@javax.jdo.annotations.PersistenceCapable(
    identityType = IdentityType.NONDURABLE,
    table = "InvoiceSummaryForPropertyDueDateStatus",
    extensions = {
        @Extension(vendorName = "datanucleus", key = "view-definition",
            value = "CREATE VIEW \"InvoiceSummaryForPropertyDueDateStatus\" " +
                    "( " +
                    "  {this.reference}, " +
                    "  {this.dueDate}, " +
                    "  {this.status}, " +
                    "  {this.total}, " +
                    "  {this.netAmount}, " +
                    "  {this.vatAmount}, " +
                    "  {this.grossAmount} " +
                    ") AS " +
                    "SELECT " +
                    "  \"FixedAsset\".\"reference\" , " +
                    "  \"Invoice\".\"dueDate\", " +
                    "  \"Invoice\".\"status\", " +
                    "  COUNT(\"Invoice\".\"id\") AS \"total\", " +
                    "   SUM(\"InvoiceItem\".\"netAmount\") AS \"netAmount\", " +
                    "   SUM(\"InvoiceItem\".\"vatAmount\") AS \"vatAmount\", " +
                    "   SUM(\"InvoiceItem\".\"grossAmount\") AS \"grossAmount\" " +
                    "FROM \"Invoice\" " +
                    "  INNER JOIN \"Lease\"       ON \"Invoice\".\"sourceLeaseId\" = \"Lease\".\"id\" " +
                    "  INNER JOIN \"Occupancy\"   ON \"Lease\".\"id\"              = \"Occupancy\".\"leaseId\" " +
                    "  INNER JOIN \"Unit\"        ON \"Unit\".\"id\"               = \"Occupancy\".\"unitId\" " +
                    "  INNER JOIN \"Property\"    ON \"Property\".\"id\"           = \"Unit\".\"propertyId\" " +
                    "  INNER JOIN \"FixedAsset\"  ON \"FixedAsset\".\"id\"         = \"Property\".\"id\" " +
                    "  INNER JOIN \"InvoiceItem\" ON \"InvoiceItem\".\"invoiceId\" = \"Invoice\".\"id\" " +
                    "GROUP BY " +
                    "  \"FixedAsset\".\"reference\", " +
                    "  \"Invoice\".\"dueDate\", " +
                    "  \"Invoice\".\"status\"")
    })
@javax.jdo.annotations.Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@Bookmarkable
@Immutable
public class InvoiceSummaryForPropertyDueDateStatus extends AbstractViewModel {


    /**
     * {@link org.apache.isis.applib.ViewModel} implementation.
     */
    @Override
    public String viewModelMemento() {
        final String base64UrlEncode = base64UrlEncode(asSnapshotXml());
        return base64UrlEncode;
    }
    
    /**
    /**
     * {@link org.apache.isis.applib.ViewModel} implementation.
     */
    @Override
    public void viewModelInit(final String memento) {
        initFromSnapshotXml(base64UrlDecode(memento));
    }

    private String asSnapshotXml() {
        Element el = new org.jdom2.Element("memento");

        set(el, "reference", getReference());
        set(el, "dueDate", getDueDate());
        set(el, "status", getStatus());
        set(el, "netAmount", getNetAmount());
        set(el, "vatAmount", getVatAmount());
        set(el, "grossAmount", getGrossAmount());
        set(el, "total", ""+getTotal());
        
        return Jdom2Util.toString(el);
    }

    private void initFromSnapshotXml(final String str) {
        final Element el = Jdom2Util.parse(str);
        
        setReference(Jdom2Util.getChild(el, "reference", String.class));
        setDueDate(Jdom2Util.getChild(el, "dueDate", LocalDate.class));
        setStatus(Jdom2Util.getChild(el, "status", String.class));
        setNetAmount(Jdom2Util.getChild(el, "netAmount", BigDecimal.class));
        setVatAmount(Jdom2Util.getChild(el, "vatAmount", BigDecimal.class));
        setGrossAmount(Jdom2Util.getChild(el, "grossAmount", BigDecimal.class));
        setTotal(Jdom2Util.getChild(el, "total", Integer.class));
    }

    static void set(final Element memento, final String name, final Object value) {
        if(value != null) {
            memento.addContent(new Element(name).setText(value.toString()));
        }
    }

    static String get(final Element el, final String name) {
        return el.getChild(name).getText();
    }

    private static String base64UrlEncode(final String str) {
        return BaseEncoding.base64Url().encode(str.getBytes(Charset.forName("UTF-8")));
    }
    private static String base64UrlDecode(final String str) {
        return new String(BaseEncoding.base64Url().decode(str), Charset.forName("UTF-8"));
    }


    // //////////////////////////////////////

    private String reference;

    @Hidden
    public String getReference() {
        return reference;
    }

    public void setReference(final String reference) {
        this.reference = reference;
    }

    // //////////////////////////////////////

    private LocalDate dueDate;

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(final LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    // //////////////////////////////////////

    private String status;

    @Hidden
    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    // //////////////////////////////////////

    private int total;

    public int getTotal() {
        return total;
    }

    public void setTotal(final int total) {
        this.total = total;
    }
    
    // //////////////////////////////////////

    private BigDecimal vatAmount;

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(final BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
    }

    // //////////////////////////////////////

    private BigDecimal netAmount;

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(final BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    // //////////////////////////////////////

    private BigDecimal grossAmount;

    public BigDecimal getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(final BigDecimal grossAmount) {
        this.grossAmount = grossAmount;
    }

    // //////////////////////////////////////

    /**
     * Annotated as {@link javax.jdo.annotations.NotPersistent not persistent}
     * because not mapped in the <tt>view-definition</tt>.
     */
    @javax.jdo.annotations.NotPersistent
    private Property property;

    /**
     * Lazily loaded from the {@link #getReference() reference}, provides access
     * to the underlying {@link Property}.
     */
    @Optional
    public Property getProperty() {
        if (property == null) {
            setProperty(properties.findPropertyByReference(getReference()));
        }
        return property;
    }

    public void setProperty(final Property property) {
        this.property = property;
    }

    // //////////////////////////////////////

    /**
     * Annotated as {@link javax.jdo.annotations.NotPersistent not persistent}
     * because not mapped in the <tt>view-definition</tt>.
     */
    @javax.jdo.annotations.NotPersistent
    private InvoiceStatus statusType;

    @Optional
    @Named("status")
    public InvoiceStatus getStatusType() {
        if (statusType == null) {
            setStatusType(InvoiceStatus.valueOf(getStatus()));
        }
        return statusType;
    }

    public void setStatusType(final InvoiceStatus statusType) {
        this.statusType = statusType;
    }

    // //////////////////////////////////////

    @Render(Type.EAGERLY)
    public List<Invoice> getInvoices() {
        return invoicesService.findInvoices(getProperty(), getDueDate(), getStatusType());
    }

    // //////////////////////////////////////

    private Properties properties;

    final public void injectProperties(final Properties properties) {
        this.properties = properties;
    }

    private Invoices invoicesService;

    final public void injectInvoicesService(final Invoices invoicesService) {
        this.invoicesService = invoicesService;
    }

}
