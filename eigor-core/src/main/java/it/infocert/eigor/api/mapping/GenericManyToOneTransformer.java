package it.infocert.eigor.api.mapping;

import it.infocert.eigor.api.ConversionIssue;
import it.infocert.eigor.api.EigorRuntimeException;
import it.infocert.eigor.api.IConversionIssue;
import it.infocert.eigor.api.SyntaxErrorInInvoiceFormatException;
import it.infocert.eigor.api.conversion.ConversionRegistry;
import it.infocert.eigor.api.errors.ErrorCode;
import it.infocert.eigor.api.utils.IReflections;
import it.infocert.eigor.model.core.model.BG0000Invoice;
import it.infocert.eigor.model.core.model.BTBG;
import org.jdom2.Document;
import org.jdom2.Element;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Generic class to transform both cen objects in XML elements and viceversa,
 * based on a n-1 configurable mapping
 * Use string concat expression
 */
public class GenericManyToOneTransformer extends GenericTransformer {
    private final String combinationExpression;
    private final String targetPath;
    private final List<String> sourcePaths;
    private final String mappingId;
    private final ErrorCode.Location callingLocation;


    /**
     * Instantiates a new Generic many to one transformation.
     *
     * @param targetPath the CEN invoice path
     */
    public GenericManyToOneTransformer(String targetPath, String combinationExpression, List<String> sourcePaths, String mappingId, IReflections reflections, ConversionRegistry conversionRegistry, ErrorCode.Location callingLocation) {
        super(reflections, conversionRegistry, callingLocation);
        this.targetPath = targetPath;
        this.combinationExpression = combinationExpression;
        this.sourcePaths = sourcePaths;
        this.mappingId = mappingId;
        this.callingLocation = callingLocation;
        log = LoggerFactory.getLogger(GenericManyToOneTransformer.class);
    }


    @Override
    public void transformXmlToCen(Document document, BG0000Invoice invoice, List<IConversionIssue> errors) {
        final String logPrefix = "(" + sourcePaths + " - " + targetPath + ") ";
        log.trace(logPrefix + "resolving");

        String finalValue = combinationExpression;
        for (int i = 0; i < sourcePaths.size(); i++) {

            String xPathText = getNodeTextFromXPath(document, sourcePaths.get(i));
            if (xPathText != null) {
                finalValue = finalValue.replace("%" + (i + 1), xPathText);
            }
        }

        if (finalValue.contains("%")) {
            finalValue = removePlaceHoldersFromExpression(finalValue);
            if (finalValue.contains("%")) {
                String elem = finalValue.substring(finalValue.indexOf("%"), 3);
                errors.add(ConversionIssue.newWarning(new EigorRuntimeException(
                        String.format("Source element %s, missing to complete many to one mapping %s with expression : %s; Result: %s", elem, mappingId, combinationExpression, finalValue),
                        callingLocation,
                        ErrorCode.Action.CONFIGURED_MAP,
                        ErrorCode.Error.INVALID
                )));
            }
        } else {
            addNewCenObjectFromStringValueToInvoice(targetPath, invoice, finalValue, errors);
        }
    }

    @Override
    public void transformCenToXml(BG0000Invoice invoice, Document document, List<IConversionIssue> errors) throws SyntaxErrorInInvoiceFormatException {
        final String logPrefix = "(" + sourcePaths + " - " + targetPath + ") ";
        log.trace(logPrefix + "resolving");

        String finalValue = combinationExpression;
        for (int idx = 0; idx < sourcePaths.size(); idx++) {

            List<BTBG> bts = getAllBTs(sourcePaths.get(idx), invoice, errors);
            if (bts == null || bts.size() == 0) {
                log.warn("No BT found for {} when trying to map to {}", sourcePaths.get(idx), targetPath);
                return;
            }
            if (bts.size() > 1) {
                errors.add(ConversionIssue.newError(new EigorRuntimeException(
                        "More than one BT for " + sourcePaths.get(idx) + ": " + bts,
                        callingLocation,
                        ErrorCode.Action.CONFIGURED_MAP,
                        ErrorCode.Error.INVALID
                )));
                return;
            }
            BTBG btbg = bts.get(0);
            Object value = getBtValue(btbg, errors);
            if (value != null) {
                Class<?> aClass = value.getClass();
                String converted = conversionRegistry.convert(aClass, String.class, value);
                log.info("CEN '{}' with value '{}' mapped to XML element '{}' with value '{}'.",
                        btbg.denomination(), String.valueOf(value), targetPath, converted);
                finalValue = finalValue.replace("%" + (idx + 1), converted);
            }
        }

        if (finalValue.contains("%")) {

            finalValue = removePlaceHoldersFromExpression(finalValue);
            if (finalValue.contains("%")) {
                String elem = finalValue.substring(finalValue.indexOf("%"), 3);
                errors.add(ConversionIssue.newWarning(new EigorRuntimeException(
                        String.format("Source element %s, missing to complete many to one mapping %s with expression : %s; Result: %s", elem, mappingId, combinationExpression, finalValue),
                        callingLocation,
                        ErrorCode.Action.CONFIGURED_MAP,
                        ErrorCode.Error.INVALID
                )));
            }
        }

        List<Element> elements = getAllXmlElements(targetPath, document, 1, sourcePaths.toString(), errors);
        if (elements == null || elements.size() == 0) return;
        if (elements.size() > 1) {
            errors.add(ConversionIssue.newError(new EigorRuntimeException(
                    "More than one element for " + targetPath + ": " + elements,
                    callingLocation,
                    ErrorCode.Action.CONFIGURED_MAP,
                    ErrorCode.Error.INVALID
            )));
            return;
        }
        elements.get(0).setText(finalValue);
    }

    private String removePlaceHoldersFromExpression(String finalValue) {
        while (finalValue.contains("%")) {
            int idxPlaceholder = finalValue.indexOf("%");
            String toReplace = "%";
            while (idxPlaceholder + 1 < finalValue.length()
                    && finalValue.charAt(idxPlaceholder + 1) >= '0'
                    && finalValue.charAt(idxPlaceholder + 1) <= '9') {
                toReplace += finalValue.charAt(idxPlaceholder + 1);
                idxPlaceholder++;
            }
            finalValue = finalValue.replace(toReplace, "");
        }
        return finalValue.trim();
    }
}
