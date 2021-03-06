package it.infocert.eigor.api.mapping;

import com.google.common.io.Resources;
import it.infocert.eigor.api.IConversionIssue;
import it.infocert.eigor.api.conversion.ConversionRegistry;
import it.infocert.eigor.api.errors.ErrorCode;
import it.infocert.eigor.api.utils.IReflections;
import it.infocert.eigor.api.utils.JavaReflections;
import it.infocert.eigor.model.core.model.*;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class GenericManyToOneTransformerTest {
    private static Logger log = LoggerFactory.getLogger(GenericManyToOneTransformerTest.class);
    private BG0000Invoice invoice;
    private ArrayList<IConversionIssue> errors;
    private IReflections reflections;
    private Document document;
    private SAXBuilder saxBuilder;
    private ConversionRegistry conversionRegistry;

    @Before
    public void setUp() throws Exception {
        invoice = new BG0000Invoice();

        saxBuilder = new SAXBuilder();
        document = new Document(new Element("FatturaElettronica"));
        errors = new ArrayList<>(0);
        reflections = new JavaReflections();
        conversionRegistry = ConversionRegistry.DEFAULT_REGISTRY;
    }

    @Test
    public void mappingToCen() throws Exception {
        URL italianInvoiceUrl = Resources.getResource("examples/ubl/UBL-Invoice-2.1-Example.xml");
        Document doc = null;
        try {

            doc = saxBuilder.build(italianInvoiceUrl.toURI().toString());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        assert doc != null;

        final List<String> xPaths = Arrays.asList("/Invoice/ID", "/Invoice/IssueDate", "/Invoice/InvoiceTypeCode");
        final String cenPath = "/BG0004/BT0033";
        final String combinationExpression = "%1-%3 %2";

        GenericManyToOneTransformer transformator = new GenericManyToOneTransformer(cenPath, combinationExpression, xPaths, "testMapping", reflections, conversionRegistry, ErrorCode.Location.FATTPA_IN);
        transformator.transformXmlToCen(doc, invoice, errors);

        assertThat(invoice.getBG0004Seller().get(0).getBT0033SellerAdditionalLegalInformation(), hasSize(1));
        assertEquals("TOSL108-380 2009-12-15", invoice.getBG0004Seller().get(0).getBT0033SellerAdditionalLegalInformation().get(0).toString());
    }

    @Test
    public void mappingToCenWithLessSourceElementsThanInExpression() throws Exception {
        final String xPathExpression = "/FatturaElettronica/FatturaElettronicaHeader/CedentePrestatore/Sede/Indirizzo";

        final List<String> cenPaths = Arrays.asList("/BG0004/BG0005/BT0035", "/BG0004/BG0005/BT0162");

        BG0004Seller seller = new BG0004Seller();
        seller.getBG0005SellerPostalAddress().add(new BG0005SellerPostalAddress());
        seller.getBG0005SellerPostalAddress(0).getBT0035SellerAddressLine1().add(new BT0035SellerAddressLine1("Grafton street"));
        seller.getBG0005SellerPostalAddress(0).getBT0162SellerAddressLine3().add(new BT0162SellerAddressLine3("3rd Floor, Room 5"));
        invoice.getBG0004Seller().add(seller);

        GenericManyToOneTransformer transformer = new GenericManyToOneTransformer(xPathExpression, "%1 %2 %3", cenPaths, "testMapping", reflections, conversionRegistry, ErrorCode.Location.FATTPA_IN);
        transformer.transformCenToXml(invoice, document, errors);
        Element item = CommonConversionModule.evaluateXpath(document, xPathExpression).get(0);
        assertEquals("Grafton street 3rd Floor, Room 5", item.getText());
    }

    @Test
    public void shouldConvertDeepBT() throws Exception {
        final String xPathExpression = "/FatturaElettronica/FatturaElettronicaHeader/CedentePrestatore/Sede/Indirizzo";

        final List<String> cenPaths = Arrays.asList("/BG0004/BG0005/BT0035", "/BG0004/BG0005/BT0036", "/BG0004/BG0005/BT0162");

        BG0004Seller seller = new BG0004Seller();
        seller.getBG0005SellerPostalAddress().add(new BG0005SellerPostalAddress());
        seller.getBG0005SellerPostalAddress(0).getBT0035SellerAddressLine1().add(new BT0035SellerAddressLine1("Grafton street"));
        seller.getBG0005SellerPostalAddress(0).getBT0036SellerAddressLine2().add(new BT0036SellerAddressLine2("Building 5"));
        seller.getBG0005SellerPostalAddress(0).getBT0162SellerAddressLine3().add(new BT0162SellerAddressLine3("3rd Floor, Room 5"));
        invoice.getBG0004Seller().add(seller);

        GenericManyToOneTransformer transformer = new GenericManyToOneTransformer(xPathExpression, "%1 %2 %3", cenPaths, "testMapping", reflections, conversionRegistry, ErrorCode.Location.FATTPA_IN);
        transformer.transformCenToXml(invoice, document, errors);
        Element item = CommonConversionModule.evaluateXpath(document, xPathExpression).get(0);
        assertEquals("Grafton street Building 5 3rd Floor, Room 5", item.getText());
    }
}
