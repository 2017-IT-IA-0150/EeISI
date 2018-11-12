package it.infocert.eigor.converter.xmlcen2cen;

import it.infocert.eigor.api.ConversionResult;
import it.infocert.eigor.api.CustomMapping;
import it.infocert.eigor.api.IConversionIssue;
import it.infocert.eigor.api.errors.ErrorCode;
import it.infocert.eigor.model.core.datatypes.Identifier;
import it.infocert.eigor.model.core.model.BG0000Invoice;
import it.infocert.eigor.model.core.model.BG0004Seller;
import it.infocert.eigor.model.core.model.BT0029SellerIdentifierAndSchemeIdentifier;
import org.jdom2.Document;
import org.jdom2.Element;

import java.util.List;
import java.util.Objects;

public class SellerConverter implements CustomMapping<Document> {

    public ConversionResult<BG0000Invoice> toBG0004(Document document, BG0000Invoice invoice, List<IConversionIssue> errors) {

        Element rootElement = document.getRootElement();
        Element bg4 = rootElement.getChild("BG-4");

        if(Objects.nonNull(bg4)) {
            if (invoice.getBG0004Seller().isEmpty()) {
                invoice.getBG0004Seller().add(new BG0004Seller());
            }
            final BG0004Seller seller = invoice.getBG0004Seller().get(0);
            
            final List<Element> bt29s = rootElement.getChild("BG-4").getChildren("BT-29");
            bt29s.forEach(bt29 -> {
                final BT0029SellerIdentifierAndSchemeIdentifier sellerIdentifierAndSchemeIdentifier;
                if(Objects.nonNull(bt29.getAttribute("scheme"))) {
                    final String scheme = bt29.getAttribute("scheme").getValue();
                    sellerIdentifierAndSchemeIdentifier = new BT0029SellerIdentifierAndSchemeIdentifier(new Identifier(scheme, bt29.getText()));
                } else {
                    sellerIdentifierAndSchemeIdentifier = new BT0029SellerIdentifierAndSchemeIdentifier(new Identifier(bt29.getText()));
                }
                seller.getBT0029SellerIdentifierAndSchemeIdentifier().add(sellerIdentifierAndSchemeIdentifier);
            });

        }
        /*
        if (fatturaElettronicaHeader != null) {
            Element cedentePrestatore = fatturaElettronicaHeader.getChild("CedentePrestatore");
            if (cedentePrestatore != null) {
                if (invoice.getBG0004Seller().isEmpty()) {
                    invoice.getBG0004Seller().add(new BG0004Seller());
                }
                final BG0004Seller seller = invoice.getBG0004Seller(0);

                String nazioneStr = "";
                Element sede = cedentePrestatore.getChild("Sede");
                if (sede != null) {
                    Element nazione = sede.getChild("Nazione");
                    if (nazione != null) {
                        nazioneStr = nazione.getText();
                    }
                }

                Element datiAnagrafici = cedentePrestatore.getChild("DatiAnagrafici");
                Element numeroIscrizioneAlbo = null;
                if (datiAnagrafici != null) {
                    numeroIscrizioneAlbo = datiAnagrafici.getChild("NumeroIscrizioneAlbo");

                    Element codiceFiscale = datiAnagrafici.getChild("CodiceFiscale");
                    Element anagrafica = datiAnagrafici.getChild("Anagrafica");
                    Element codEORI = null;
                    if (anagrafica != null) {
                        codEORI = anagrafica.getChild("CodEORI");
                    }
                    Element alboProfessionale = datiAnagrafici.getChild("AlboProfessionale");
                    BT0029SellerIdentifierAndSchemeIdentifier sellerIdentifierAndSchemeIdentifier;
                    if (codiceFiscale != null) {
                        if (nazioneStr.equals("IT")) {
                            sellerIdentifierAndSchemeIdentifier = new BT0029SellerIdentifierAndSchemeIdentifier(new Identifier(cf, codiceFiscale.getText()));
                        } else {
                            sellerIdentifierAndSchemeIdentifier = new BT0029SellerIdentifierAndSchemeIdentifier(new Identifier(codiceFiscale.getText()));
                        }

                        seller.getBT0029SellerIdentifierAndSchemeIdentifier().add(sellerIdentifierAndSchemeIdentifier);
                    } else if (anagrafica != null && codEORI != null) {
                        if (nazioneStr.equals("IT")) {
                            sellerIdentifierAndSchemeIdentifier = new BT0029SellerIdentifierAndSchemeIdentifier(new Identifier(eori, codEORI.getText()));
                        } else {
                            sellerIdentifierAndSchemeIdentifier = new BT0029SellerIdentifierAndSchemeIdentifier(new Identifier(codEORI.getText()));
                        }

                        seller.getBT0029SellerIdentifierAndSchemeIdentifier().add(sellerIdentifierAndSchemeIdentifier);
                    } else if (alboProfessionale != null && numeroIscrizioneAlbo != null) {
                        if (nazioneStr.equals("IT")) {
                            sellerIdentifierAndSchemeIdentifier = new BT0029SellerIdentifierAndSchemeIdentifier(new Identifier(albo, alboProfessionale.getText() + ":" + numeroIscrizioneAlbo.getText()));
                        } else {
                            sellerIdentifierAndSchemeIdentifier = new BT0029SellerIdentifierAndSchemeIdentifier(new Identifier(alboProfessionale.getText() + ":" + numeroIscrizioneAlbo.getText()));
                        }

                        seller.getBT0029SellerIdentifierAndSchemeIdentifier().add(sellerIdentifierAndSchemeIdentifier);
                    }

                }

                Element iscrizioneREA = cedentePrestatore.getChild("IscrizioneREA");
                if (iscrizioneREA != null) {
                    Element ufficio = iscrizioneREA.getChild("Ufficio");
                    Element numeroREA = iscrizioneREA.getChild("NumeroREA");

                    if (nazioneStr.equals("IT")) {
                        if (ufficio != null && numeroREA != null) {
                            BT0030SellerLegalRegistrationIdentifierAndSchemeIdentifier sellerLegalRegistrationIdentifierAndSchemeIdentifier =
                                    new BT0030SellerLegalRegistrationIdentifierAndSchemeIdentifier(new Identifier(rea, ufficio.getText() + ":" + numeroREA.getText()));
                            if (invoice.getBG0004Seller().isEmpty()) {
                                invoice.getBG0004Seller().add(new BG0004Seller());
                            }
                            seller.getBT0030SellerLegalRegistrationIdentifierAndSchemeIdentifier().add(sellerLegalRegistrationIdentifierAndSchemeIdentifier);
                        }
                    } else {

                        if (ufficio != null) {
                            BT0030SellerLegalRegistrationIdentifierAndSchemeIdentifier sellerLegalRegistrationIdentifierAndSchemeIdentifier =
                                    new BT0030SellerLegalRegistrationIdentifierAndSchemeIdentifier(new Identifier(ufficio.getText()));
                            if (invoice.getBG0004Seller().isEmpty()) {
                                invoice.getBG0004Seller().add(new BG0004Seller());
                            }
                            seller.getBT0030SellerLegalRegistrationIdentifierAndSchemeIdentifier().add(sellerLegalRegistrationIdentifierAndSchemeIdentifier);
                        } else if (numeroREA != null) {
                            BT0030SellerLegalRegistrationIdentifierAndSchemeIdentifier sellerLegalRegistrationIdentifierAndSchemeIdentifier =
                                    new BT0030SellerLegalRegistrationIdentifierAndSchemeIdentifier(new Identifier(numeroREA.getText()));
                            if (invoice.getBG0004Seller().isEmpty()) {
                                invoice.getBG0004Seller().add(new BG0004Seller());
                            }
                            seller.getBT0030SellerLegalRegistrationIdentifierAndSchemeIdentifier().add(sellerLegalRegistrationIdentifierAndSchemeIdentifier);
                        }
                    }
                } else if (numeroIscrizioneAlbo != null) {
                    BT0030SellerLegalRegistrationIdentifierAndSchemeIdentifier sellerLegalRegistrationIdentifierAndSchemeIdentifier =
                            new BT0030SellerLegalRegistrationIdentifierAndSchemeIdentifier(new Identifier(numeroIscrizioneAlbo.getText()));
                    if (invoice.getBG0004Seller().isEmpty()) {
                        invoice.getBG0004Seller().add(new BG0004Seller());
                    }
                    seller.getBT0030SellerLegalRegistrationIdentifierAndSchemeIdentifier().add(sellerLegalRegistrationIdentifierAndSchemeIdentifier);
                }
            }
        }*/
        return new ConversionResult<>(errors, invoice);
    }

    @Override
    public void map(BG0000Invoice cenInvoice, Document document, List<IConversionIssue> errors, ErrorCode.Location callingLocation) {
        toBG0004(document, cenInvoice, errors);
    }
}
