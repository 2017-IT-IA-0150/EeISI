package it.infocert.eigor.converter.cen2ubl;

import it.infocert.eigor.api.CustomMapping;
import it.infocert.eigor.api.conversion.JavaLocalDateToStringConverter;
import it.infocert.eigor.api.conversion.Untdid4461PaymentMeansCodeToString;
import it.infocert.eigor.model.core.enums.Untdid4461PaymentMeansCode;
import it.infocert.eigor.model.core.model.*;
import org.jdom2.Document;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PaymentMeansConverter implements CustomMapping<Document> {
    private static final Logger log = LoggerFactory.getLogger(PaymentMeansConverter.class);

    @Override
    public void map(BG0000Invoice cenInvoice, Document document, List errors) {

        Element root = document.getRootElement();
        if (root != null) {
            if (!cenInvoice.getBG0016PaymentInstructions().isEmpty()) {
                BG0016PaymentInstructions bg0016 = cenInvoice.getBG0016PaymentInstructions(0);

                Untdid4461PaymentMeansCodeToString paymentMeansCodeToStr = new Untdid4461PaymentMeansCodeToString();

                Element paymentMeans = root.getChild("PaymentMeans");
                if (paymentMeans == null) {
                    paymentMeans = new Element("PaymentMeans");
                    root.addContent(paymentMeans);
                }

                if (!bg0016.getBT0081PaymentMeansTypeCode().isEmpty()) {
                    BT0081PaymentMeansTypeCode bt0081 = bg0016.getBT0081PaymentMeansTypeCode(0);
                    Element paymentMeansCode = new Element("PaymentMeansCode");
                    paymentMeansCode.setText(paymentMeansCodeToStr.convert(bt0081.getValue()));
                    paymentMeans.addContent(paymentMeansCode);
                }

                if (!bg0016.getBT0083RemittanceInformation().isEmpty()) {
                    BT0083RemittanceInformation bt0083 = bg0016.getBT0083RemittanceInformation(0);
                    Element paymentID = new Element("PaymentID");
                    paymentID.setText(bt0083.getValue());
                    paymentMeans.addContent(paymentID);
                }
            }
        }
    }
}