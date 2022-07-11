package net.minefury.domains.domain.properties;

import lombok.Getter;
import net.minefury.domains.exceptions.DocumentParseException;
import org.bson.Document;

public class DomainUpgradables {

    @Getter private DomainBorder border;

    public DomainUpgradables() {
        this.border = new DomainBorder();
    }

    public DomainUpgradables(DomainBorder border) {
        this.border = border;
    }

    //TODO: Implement this
/*    public boolean canExpand(int blocks) {
        ConfigFile file = FuryDomains.getInstance().getDataManager().getConfig();
        int maxDomainSize = file.getInteger("limits.max-domain-size", 500);
        int newRadius = worldSize + (2 * blocks);

        if (newRadius > maxDomainSize) {
            return false;
        }

        return true;
    }*/

    public Document toDocument() {
        Document upgradables = new Document();
        upgradables.append("border", border.toDocument());
        return upgradables;
    }

    public static DomainUpgradables fromDocument(Document document) throws DocumentParseException {
        DomainBorder border;
        try {
            Document upgradablesDocument = (Document) document.get("upgradables");
            border = DomainBorder.fromDocument(upgradablesDocument);
        } catch(NullPointerException e) {
            throw new DocumentParseException("Failed to retrieve domain upgradables from mongo document.");
        }
        return new DomainUpgradables(border);
    }

}
