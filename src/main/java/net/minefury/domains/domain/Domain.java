package net.minefury.domains.domain;

import com.modify.fundamentum.text.PlugLogger;
import lombok.Getter;
import net.minefury.domains.MineDomains;
import net.minefury.domains.data.mongo.MongoHelper;
import net.minefury.domains.domain.properties.Role;
import net.minefury.domains.domain.properties.DomainSettings;
import net.minefury.domains.domain.properties.DomainUpgradables;
import net.minefury.domains.domain.theme.Theme;
import net.minefury.domains.domain.warps.DomainWarps;
import net.minefury.domains.exceptions.DocumentParseException;
import net.minefury.domains.exceptions.DomainRegistrationException;
import net.minefury.domains.exceptions.ThemeParseException;
import net.minefury.domains.exceptions.UUIDFormatException;
import net.minefury.domains.util.FuryUtil;
import org.bson.Document;

import java.util.*;

/**
 * Domains are private worlds which player's own, to build and expand as they please.
 * Domains consist of their domain object, mongo document, and slime world however
 * this object ties each of these together in some way.
 */
public class Domain {

    /** UUID of the id for this domain */
    @Getter private UUID id;

    @Getter private String name;

    /** UUID of user who owns the domain */
    @Getter private UUID owner;

    /** Slime world associated with this domain */
    @Getter private UUID worldId;

    /** All team members and their roles for the domain */
    @Getter private Map<UUID, Role> members;

    /** Player controlled settings for this domain */
    @Getter private DomainSettings domainSettings;

    /** Upgradable features for this domain */
    @Getter private DomainUpgradables upgradables;

    /** All player-set domain warps */
    @Getter private DomainWarps warps;

    /** Theme selected by the player for this domain */
    @Getter private Theme theme;

    /**
     * Constructor for a new domain. Used for retrieving domain object from
     * mongo document.
     * @param id uuid of the domain.
     * @param owner uuid of the owner of this domain.
     * @param theme theme for this domain.
     * @param domainSettings domain settings for this domain.
     * @param upgradables upgradables for this domain.
     * @param warps warps for this domain.
     * @param worldId slime world uuid associated with this domain.
     * @param members uuid of members and their current roles for this domain.
     */
    public Domain(UUID id, String name, UUID owner, Theme theme, DomainSettings domainSettings,
                  DomainUpgradables upgradables, DomainWarps warps, UUID worldId, Map<UUID, Role> members) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.theme = theme;
        this.domainSettings = domainSettings;
        this.upgradables = upgradables;
        this.worldId = worldId;
        this.members = members;
        this.warps = warps;
    }

    /**
     * Converts domain object to mongo document.
     * @return document object for this domain.
     */
    public Document toDocument() {
        MineDomains plugin = MineDomains.getInstance();

        Document document = new Document("_id", this.getId().toString());
        document.put("name", name);
        document.put("owner", owner.toString());
        document.put("worldId", worldId.toString());
        document.put("members", memberMapToDocumentList(members));
        document.put("settings", domainSettings.toDocument());
        document.put("upgradables", upgradables.toDocument());

        document.put("warps", warps.toDocumentList());
        document.put("theme", theme.getNameRaw());

        return document;
    }

    /**
     * Retrieves domain object from mongo document.
     * @param document document to retrieve domain object for.
     * @return domain object.
     */
    public static Domain fromDocument(Document document) throws ThemeParseException, UUIDFormatException, DocumentParseException {

        String domainIdRaw = document.getString("_id");
        UUID domainId = FuryUtil.parseUUID(domainIdRaw);

        String name = document.getString("name");
        if (name == null) {
            throw new DocumentParseException("Name of domain is null.", domainIdRaw);
        }

        UUID owner;
        try {
            owner = FuryUtil.parseUUID(document.getString("owner"));
        } catch (NullPointerException e) {
            throw new DocumentParseException("World ID value not found.", domainIdRaw);
        }

        UUID worldId;
        try {
            worldId = FuryUtil.parseUUID(document.getString("worldId"));
        } catch(NullPointerException e) {
            throw new DocumentParseException("World ID value not found.", domainIdRaw);
        }

        DomainUpgradables upgradables = DomainUpgradables.fromDocument(document);
        DomainSettings domainSettings = DomainSettings.fromDocument(document);

        // Retrieved document list containing all domain members may be null as documentListToMemberMap handles this.
        Map<UUID, Role> members = documentListToMemberMap(document.get("members", List.class));

        DomainWarps warps = DomainWarps.fromDocument(document);

        // Retrieve raw name of theme and parse as theme
        Theme theme = Theme.parseTheme(document.getString("theme"));

        return new Domain(domainId, name, owner, theme, domainSettings, upgradables, warps, worldId, members);
    }

    /**
     * Creates new domain object.
     * @param owner owner of domain
     * @param theme theme for domain
     * @return new domain object.
     */
    public static Domain createDomain(UUID owner, String name, Theme theme) {
        UUID id = UUID.randomUUID();

        DomainSettings domainSettings = new DomainSettings(theme);
        DomainUpgradables domainUpgradables = new DomainUpgradables();
        DomainWarps domainWarps = new DomainWarps();
        UUID worldId = UUID.randomUUID();

        Map<UUID, Role> members = new HashMap<>();

        return new Domain(id, name, owner, theme, domainSettings, domainUpgradables,
                domainWarps, worldId, members);
    }

    /**
     * Checks for equality between this domain and another.
     * Two domains are equal if any of the following conditions are true:
     * - They are the same objects in memory
     * - They have the same variable values
     *
     * @param o object to test equality
     * @return true if objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        // Check if the same objects in memory
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Domain domain = (Domain) o;
        return id.equals(domain.id) &&
                owner.equals(domain.owner) &&
                worldId.equals(domain.worldId) &&
                members.equals(domain.members) &&
                domainSettings.equals(domain.domainSettings) &&
                upgradables.equals(domain.upgradables) &&
                warps.equals(domain.warps) &&
                theme.equals(domain.theme);
    }

    /**
     * Updates this domain object with the MongoDB database.
     */
    public void updateDatabase() {
        try {
            MongoHelper.updateDomain(this);
        } catch (DomainRegistrationException e) {
            PlugLogger.logError("Failed to update domain " + id.toString());
            e.printStackTrace();
        }
    }

    /**
     * Converts a list of documents for domain members into a hashmap whereby
     * keys are the UUID of the member and values are the role for that member.
     *
     * Parameter can sometimes be null if there are no members in the domain.
     * In this case, an empty hashmap will be returned.
     *
     * @param documents list of documents which contain member information.
     * @return hashmap where a key is a member's uuid and the value is that member's domain role.
     *
     * @throws DocumentParseException exception thrown in three cases:
     *                                1. a UUID value for a member does not exist
     *                                2. a role value for a member does not exist
     *                                3. a role assigned to a member is invalid
     *
     * @throws UUIDFormatException if a member's UUID is not of the correct UUID format.
     */
    private static Map<UUID, Role> documentListToMemberMap(List<Document> documents) throws DocumentParseException, UUIDFormatException {
        Map<UUID, Role> memberMap = new HashMap<>();

        if (documents != null) {
            for (Document entry : documents) {

                String uuidRaw = entry.getString("uuid");
                String roleRaw = entry.getString("role");

                if (uuidRaw == null || roleRaw == null) {
                    throw new DocumentParseException("Data corruption for domain member list");
                }

                UUID uuid = FuryUtil.parseUUID(uuidRaw);

                Role role;
                try {
                    role = Role.valueOf(roleRaw);
                } catch (IllegalArgumentException e) {
                    throw new DocumentParseException("Role " + roleRaw + " assigned to member " + uuidRaw + " is not a valid role.");
                }

                memberMap.put(uuid, role);
            }
        }
        return memberMap;
    }

    private List<Document> memberMapToDocumentList(Map<UUID, Role> map) {
        List<Document> documents = new ArrayList<>();

        for(Map.Entry<UUID, Role> entry : map.entrySet()) {
            UUID uuid = entry.getKey();
            Role role = entry.getValue();

            Document member = new Document("uuid", uuid.toString());
            member.append("role", role.name());

            documents.add(member);
        }

        return documents;
    }


}
