package it.infocert.eigor.converter.cen2cii;

import it.infocert.eigor.api.*;
import it.infocert.eigor.api.conversion.ConversionFailedException;
import it.infocert.eigor.api.conversion.DoubleToStringConverter;
import it.infocert.eigor.api.conversion.JavaLocalDateToStringConverter;
import it.infocert.eigor.api.conversion.TypeConverter;
import it.infocert.eigor.api.errors.ErrorCode;
import it.infocert.eigor.model.core.model.*;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.joda.time.LocalDate;

import java.util.List;

/**
 * The Payment Terms Custom Converter
 */
public class PaymentTermsConverter extends CustomConverterUtils implements CustomMapping<Document> {

    @Override
    public void map(BG0000Invoice cenInvoice, Document document, List<IConversionIssue> errors, ErrorCode.Location callingLocation) {
        TypeConverter<LocalDate, String> dateStrConverter = JavaLocalDateToStringConverter.newConverter("yyyyMMdd");
        TypeConverter<Double, String> dblStrConverter = DoubleToStringConverter.newConverter("0.00");

        Element rootElement = document.getRootElement();
        List<Namespace> namespacesInScope = rootElement.getNamespacesIntroduced();
        Namespace rsmNs = rootElement.getNamespace("rsm");
        Namespace ramNs = rootElement.getNamespace("ram");
        Namespace udtNs = rootElement.getNamespace("udt");

        Element supplyChainTradeTransaction = findNamespaceChild(rootElement, namespacesInScope, "SupplyChainTradeTransaction");
        if (supplyChainTradeTransaction == null) {
            supplyChainTradeTransaction = new Element("SupplyChainTradeTransaction", rsmNs);
            rootElement.addContent(supplyChainTradeTransaction);
        }

        Element applicableHeaderTradeAgreement = findNamespaceChild(supplyChainTradeTransaction, namespacesInScope, "ApplicableHeaderTradeAgreement");
        if (applicableHeaderTradeAgreement == null) {
            applicableHeaderTradeAgreement = new Element("ApplicableHeaderTradeAgreement", ramNs);
            supplyChainTradeTransaction.addContent(applicableHeaderTradeAgreement);
        }

        Element specifiedTradePaymentTerms = findNamespaceChild(applicableHeaderTradeAgreement, namespacesInScope, "SpecifiedTradePaymentTerms");
        if (specifiedTradePaymentTerms == null) {
            specifiedTradePaymentTerms = new Element("SpecifiedTradePaymentTerms", ramNs);
            applicableHeaderTradeAgreement.addContent(specifiedTradePaymentTerms);
        }

        if (!cenInvoice.getBT0009PaymentDueDate().isEmpty()) {
            LocalDate bt0009 = cenInvoice.getBT0009PaymentDueDate(0).getValue();
            Element dueDateDateTime = new Element("DueDateDateTime", ramNs);
            Element dateTimeString = new Element("DateTimeString", udtNs);
            dateTimeString.setAttribute("format", "102");
            try {
                dateTimeString.setText(dateStrConverter.convert(bt0009));
                dueDateDateTime.addContent(dateTimeString);
                specifiedTradePaymentTerms.addContent(dueDateDateTime);
            } catch (IllegalArgumentException | ConversionFailedException e) {
                errors.add(ConversionIssue.newError(new EigorRuntimeException(
                        e.getMessage(),
                        callingLocation,
                        ErrorCode.Action.HARDCODED_MAP,
                        ErrorCode.Error.INVALID,
                        e
                )));
            }
        }

        if (!cenInvoice.getBT0020PaymentTerms().isEmpty()) {
            String bt0020 = cenInvoice.getBT0020PaymentTerms(0).getValue();
            Element description = new Element("Description", ramNs);
            description.setText(bt0020);
            specifiedTradePaymentTerms.addContent(description);
        }

        List<BG0016PaymentInstructions> bg0016s = cenInvoice.getBG0016PaymentInstructions();
        if (!bg0016s.isEmpty()) {
            List<BG0019DirectDebit> bg0019s = bg0016s.get(0).getBG0019DirectDebit();
            if (!bg0019s.isEmpty()) {
                List<BT0089MandateReferenceIdentifier> bt0089s = bg0019s.get(0).getBT0089MandateReferenceIdentifier();
                if (!bt0089s.isEmpty()) {
                    BT0089MandateReferenceIdentifier bt0089 = bt0089s.get(0);
                    Element directDebitMandateID = new Element("DirectDebitMandateID", ramNs);
                    directDebitMandateID.setText(bt0089.getValue());
                    specifiedTradePaymentTerms.addContent(directDebitMandateID);
                }
            }
        }

        if (!cenInvoice.getBG0022DocumentTotals().isEmpty()) {
            BG0022DocumentTotals bg0022 = cenInvoice.getBG0022DocumentTotals(0);

            Element specifiedTradeSettlementHeaderMonetarySummation = findNamespaceChild(applicableHeaderTradeAgreement, namespacesInScope, "SpecifiedTradeSettlementHeaderMonetarySummation");
            if (specifiedTradeSettlementHeaderMonetarySummation == null) {
                specifiedTradeSettlementHeaderMonetarySummation = new Element("SpecifiedTradeSettlementHeaderMonetarySummation", ramNs);
                applicableHeaderTradeAgreement.addContent(specifiedTradeSettlementHeaderMonetarySummation);
            }

            if (!bg0022.getBT0106SumOfInvoiceLineNetAmount().isEmpty()) {
                Double bt0106 = bg0022.getBT0106SumOfInvoiceLineNetAmount(0).getValue();
                Element lineTotalAmount = new Element("LineTotalAmount", ramNs);
                try {
                    lineTotalAmount.setText(dblStrConverter.convert(bt0106));
                    specifiedTradeSettlementHeaderMonetarySummation.addContent(lineTotalAmount);
                } catch (ConversionFailedException e) {
                    errors.add(ConversionIssue.newError(new EigorRuntimeException(
                            e.getMessage(),
                            callingLocation,
                            ErrorCode.Action.HARDCODED_MAP,
                            ErrorCode.Error.INVALID,
                            e
                    )));
                }
            }

            if (!bg0022.getBT0107SumOfAllowancesOnDocumentLevel().isEmpty()) {
                Double bt0107 = bg0022.getBT0107SumOfAllowancesOnDocumentLevel(0).getValue();
                Element allowanceTotalAmount = new Element("AllowanceTotalAmount", ramNs);
                try {
                    allowanceTotalAmount.setText(dblStrConverter.convert(bt0107));
                    specifiedTradeSettlementHeaderMonetarySummation.addContent(allowanceTotalAmount);
                } catch (ConversionFailedException e) {
                    errors.add(ConversionIssue.newError(new EigorRuntimeException(
                            e.getMessage(),
                            callingLocation,
                            ErrorCode.Action.HARDCODED_MAP,
                            ErrorCode.Error.INVALID,
                            e
                    )));
                }
            }

            if (!bg0022.getBT0108SumOfChargesOnDocumentLevel().isEmpty()) {
                Double bt0108 = bg0022.getBT0108SumOfChargesOnDocumentLevel(0).getValue();
                Element chargeTotalAmount = new Element("ChargeTotalAmount", ramNs);
                try {
                    chargeTotalAmount.setText(dblStrConverter.convert(bt0108));
                    specifiedTradeSettlementHeaderMonetarySummation.addContent(chargeTotalAmount);
                } catch (ConversionFailedException e) {
                    errors.add(ConversionIssue.newError(new EigorRuntimeException(
                            e.getMessage(),
                            callingLocation,
                            ErrorCode.Action.HARDCODED_MAP,
                            ErrorCode.Error.INVALID,
                            e
                    )));
                }
            }

            if (!bg0022.getBT0109InvoiceTotalAmountWithoutVat().isEmpty()) {
                Double bt0109 = bg0022.getBT0109InvoiceTotalAmountWithoutVat(0).getValue();
                Element taxBasisTotalAmount = new Element("TaxBasisTotalAmount", ramNs);
                try {
                    taxBasisTotalAmount.setText(dblStrConverter.convert(bt0109));
                    specifiedTradeSettlementHeaderMonetarySummation.addContent(taxBasisTotalAmount);
                } catch (ConversionFailedException e) {
                    errors.add(ConversionIssue.newError(new EigorRuntimeException(
                            e.getMessage(),
                            callingLocation,
                            ErrorCode.Action.HARDCODED_MAP,
                            ErrorCode.Error.INVALID,
                            e
                    )));
                }
            }

            if (!bg0022.getBT0110InvoiceTotalVatAmount().isEmpty()) {
                Double bt0110 = bg0022.getBT0110InvoiceTotalVatAmount(0).getValue();
                Element taxTotalAmount = new Element("TaxTotalAmount", ramNs);
                try {
                    taxTotalAmount.setText(dblStrConverter.convert(bt0110));
                    specifiedTradeSettlementHeaderMonetarySummation.addContent(taxTotalAmount);
                } catch (ConversionFailedException e) {
                    errors.add(ConversionIssue.newError(new EigorRuntimeException(
                            e.getMessage(),
                            callingLocation,
                            ErrorCode.Action.HARDCODED_MAP,
                            ErrorCode.Error.INVALID,
                            e
                    )));
                }
            }

            if (!bg0022.getBT0112InvoiceTotalAmountWithVat().isEmpty()) {
                Double bt0112 = bg0022.getBT0112InvoiceTotalAmountWithVat(0).getValue();
                Element grandTotalAmount = new Element("GrandTotalAmount", ramNs);
                try {
                    grandTotalAmount.setText(dblStrConverter.convert(bt0112));
                    specifiedTradeSettlementHeaderMonetarySummation.addContent(grandTotalAmount);
                } catch (ConversionFailedException e) {
                    errors.add(ConversionIssue.newError(new EigorRuntimeException(
                            e.getMessage(),
                            callingLocation,
                            ErrorCode.Action.HARDCODED_MAP,
                            ErrorCode.Error.INVALID,
                            e
                    )));
                }
            }

            if (!bg0022.getBT0113PaidAmount().isEmpty()) {
                Double bt0113 = bg0022.getBT0113PaidAmount(0).getValue();
                Element totalPrepaidAmount = new Element("TotalPrepaidAmount", ramNs);
                try {
                    totalPrepaidAmount.setText(dblStrConverter.convert(bt0113));
                    specifiedTradeSettlementHeaderMonetarySummation.addContent(totalPrepaidAmount);
                } catch (ConversionFailedException e) {
                    errors.add(ConversionIssue.newError(new EigorRuntimeException(
                            e.getMessage(),
                            callingLocation,
                            ErrorCode.Action.HARDCODED_MAP,
                            ErrorCode.Error.INVALID,
                            e
                    )));
                }
            }

            if (!bg0022.getBT0114RoundingAmount().isEmpty()) {
                Double bt0114 = bg0022.getBT0114RoundingAmount(0).getValue();
                Element roundingAmount = new Element("RoundingAmount", ramNs);
                try {
                    roundingAmount.setText(dblStrConverter.convert(bt0114));
                    specifiedTradeSettlementHeaderMonetarySummation.addContent(roundingAmount);
                } catch (ConversionFailedException e) {
                    errors.add(ConversionIssue.newError(new EigorRuntimeException(
                            e.getMessage(),
                            callingLocation,
                            ErrorCode.Action.HARDCODED_MAP,
                            ErrorCode.Error.INVALID,
                            e
                    )));
                }
            }

            if (!bg0022.getBT0115AmountDueForPayment().isEmpty()) {
                Double bt0115 = bg0022.getBT0115AmountDueForPayment(0).getValue();
                Element duePayableAmount = new Element("DuePayableAmount", ramNs);
                try {
                    duePayableAmount.setText(dblStrConverter.convert(bt0115));
                    specifiedTradeSettlementHeaderMonetarySummation.addContent(duePayableAmount);
                } catch (ConversionFailedException e) {
                    errors.add(ConversionIssue.newError(new EigorRuntimeException(
                            e.getMessage(),
                            callingLocation,
                            ErrorCode.Action.HARDCODED_MAP,
                            ErrorCode.Error.INVALID,
                            e
                    )));
                }
            }
        }
    }
}

