package net.minefury.domains.domain.warps;

import net.minefury.domains.exceptions.DocumentParseException;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the object of all domain warps a player may have in their domain.
 */
public class DomainWarps {

    /** List of warps for the domain */
    private List<Warp> warps;

    /**
     * Constructs a new domain warps object and creates empty list of these warps.
     */
    public DomainWarps() {
        this.warps = new ArrayList<>();
    }

    /**
     * Constructs a new domain warps object using a given list of warps.
     * @param warps
     */
    public DomainWarps(List<Warp> warps) {
        this.warps = warps;
    }

    public static DomainWarps fromDocument(Document document) throws DocumentParseException {
        List<Document> warpsDocument = document.get("warps", List.class);
        List<Warp> warpLst = new ArrayList<>();

        if (warpsDocument != null) {
            for (Document entry : warpsDocument) {
                warpLst.add(Warp.fromDocument(entry));
            }
        }
        return new DomainWarps(warpLst);
    }


    /**
     * Converts domain warps object into a list of documents
     * whereby each document in the list represents an individual warp.
     * @return document to use for mongodb interactions.
     */
    public List<Document> toDocumentList() {
        List<Document> documentList = new ArrayList<>();
        for (Warp entry : warps) {
            documentList.add(entry.toDocument());
        }
        return documentList;
    }




}
