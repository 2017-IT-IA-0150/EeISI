package it.infocert.eigor.model.core.model;

import java.util.ArrayList;
import java.util.List;

import static java.util.Comparator.comparing;

public class CoreInvoice {

    private List<BG001InvoiceNote> bg001InvoiceNotes = new ArrayList<>(0);
    private List<BG002ProcessControl> bg002ProcessControls = new ArrayList<>(0);
    private List<BG011SellerTaxRepresentativeParty> bg11SellerTaxRepresentativeParties = new ArrayList<>(0);

    private List<BT001InvoiceNumber> bt001InvoiceNumbers = new ArrayList<>(0);
    private List<BT006VatAccountingCurrencyCode> bt006VatAccountingCurrencyCodes = new ArrayList<>(0);

    public List<BG001InvoiceNote> getBg001InvoiceNotes() {
        return bg001InvoiceNotes;
    }

    public void setBg001InvoiceNotes(List<BG001InvoiceNote> bg001InvoiceNotes) {
        this.bg001InvoiceNotes = bg001InvoiceNotes;
    }

    public List<BG002ProcessControl> getBg002ProcessControls() {
        return bg002ProcessControls;
    }

    public void setBg002ProcessControls(List<BG002ProcessControl> bg002ProcessControls) {
        this.bg002ProcessControls = bg002ProcessControls;
    }

    public List<BG011SellerTaxRepresentativeParty> getBg11SellerTaxRepresentativeParties() {
        return bg11SellerTaxRepresentativeParties;
    }

    public void setBg11SellerTaxRepresentativeParties(List<BG011SellerTaxRepresentativeParty> bg11SellerTaxRepresentativeParties) {
        this.bg11SellerTaxRepresentativeParties = bg11SellerTaxRepresentativeParties;
    }

    public List<BT001InvoiceNumber> getBt001InvoiceNumbers() {
        return bt001InvoiceNumbers;
    }

    public void setBt001InvoiceNumbers(List<BT001InvoiceNumber> bt001InvoiceNumbers) {
        this.bt001InvoiceNumbers = bt001InvoiceNumbers;
    }

    public List<BT006VatAccountingCurrencyCode> getBt006VatAccountingCurrencyCodes() {
        return bt006VatAccountingCurrencyCodes;
    }

    public void setBt006VatAccountingCurrencyCodes(List<BT006VatAccountingCurrencyCode> bt006VatAccountingCurrencyCodes) {
        this.bt006VatAccountingCurrencyCodes = bt006VatAccountingCurrencyCodes;
    }




    public void accept(Visitor v) {
        v.startInvoice(this);

        List<BTBG> list = new ArrayList<>();
        list.addAll(this.bg001InvoiceNotes);
        list.addAll(this.bg002ProcessControls);
        list.addAll(this.bg11SellerTaxRepresentativeParties);
        list.addAll(this.bt001InvoiceNumbers);
        list.addAll(this.bt006VatAccountingCurrencyCodes);
        list.sort( comparing( o -> o.order() ) );

        list.forEach( o -> o.accept(v) );

        v.endInvoice(this);
    }
}