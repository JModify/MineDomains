package net.minefury.domains.data.mongo;

import com.google.common.collect.Iterators;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import net.minefury.domains.MineDomains;
import net.minefury.domains.domain.Domain;
import net.minefury.domains.exceptions.DocumentParseException;
import net.minefury.domains.exceptions.DomainRegistrationException;
import net.minefury.domains.exceptions.ThemeParseException;
import net.minefury.domains.exceptions.UUIDFormatException;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Helper class used for interaction between MongoDB and the plugin.
 */
public class MongoHelper {

    /**
     * Inserts document for domain into Mongo database
     * @param domain domain to insert/register
     */
    public static void registerDomain(Domain domain) {
        MongoConnection connection = MineDomains.getInstance().getDataManager().getConnection();
        connection.getMongoCollection().insertOne(domain.toDocument());
    }

    /**
     * Removes document from Mongo Database for domain with the given ID
     * @param domain domain to delete/unregister
     */
    public static void unregisterDomain(Domain domain) {
        MongoConnection connection = MineDomains.getInstance().getDataManager().getConnection();
        MongoCollection<Document> collection = connection.getMongoCollection();

        Document document = collection.find(Filters.eq("_id", domain.getId().toString())).first();

        if (document != null)
            collection.deleteOne(document);
    }


    public static Document getFirstDomain(UUID user) {
        MongoConnection connection = MineDomains.getInstance().getDataManager().getConnection();

        MongoCollection<Document> collection = connection.getMongoCollection();

        FindIterable<Document> document = collection.find(Filters.or(
                Filters.eq("owner", user.toString()),
                Filters.eq("members.uuid", user.toString())));

        return document.first();
    }

    /**
     * Retrieves the number of domains a user is a part of.
     * The criteria for this consists of ownership for a domain and/or being a team member.
     * @param user user to retrieve domain count for.
     * @return number of domains a user is apart of.
     */
    public static int countDomains(UUID user) {
        MongoConnection connection = MineDomains.getInstance().getDataManager().getConnection();
        MongoCollection<Document> collection = connection.getMongoCollection();

        FindIterable<Document> documents = collection.find(Filters.or(
                Filters.eq("owner", user.toString()),
                Filters.eq("members.uuid", user.toString())));

        return Iterators.size(documents.iterator());
    }

    /**
     * Retrieves the domain objects for all domains a user is a part of.
     * The criteria for this consists of ownership for a domain and/or being a team member.
     * @param user user to retrieve domain objects for
     * @return list of domain objects which a user is a part of.
     */
    public static List<Domain> getDomains(UUID user) {
        MongoConnection connection = MineDomains.getInstance().getDataManager().getConnection();

        MongoCollection<Document> collection = connection.getMongoCollection();

        FindIterable<Document> documents = collection.find(Filters.or(
                Filters.eq("owner", user.toString()),
                Filters.eq("members.uuid", user.toString())));

        List<Domain> domains = new ArrayList<>();

        MongoCursor<Document> iterator = documents.iterator();

        if (documents.first() == null) {
            return new ArrayList<>();
        } else {
            while(iterator.hasNext()) {
                Document document = iterator.next();
                try {
                    Domain domain = Domain.fromDocument(document);
                    domains.add(domain);
                } catch (ThemeParseException | UUIDFormatException | DocumentParseException e) {
                    e.printStackTrace();
                }
            }
            return domains;
        }
    }

    public static Domain getDomain(UUID id) {
        MongoConnection connection = MineDomains.getInstance().getDataManager().getConnection();

        MongoCollection<Document> collection = connection.getMongoCollection();

        Document document = collection.find(Filters.eq("_id", id.toString())).first();

        Domain domain = null;
        try {
            domain = Domain.fromDocument(document);
        } catch (ThemeParseException | UUIDFormatException | DocumentParseException e) {
            e.printStackTrace();
        }

        return domain;
    }

    public static Location getSpawnLocation(Domain domain) {
        MongoConnection connection = MineDomains.getInstance().getDataManager().getConnection();

        MongoCollection<Document> collection = connection.getMongoCollection();

        Document document = collection.find(Filters.eq("_id", domain.getId().toString())).first();

        Document settingsDocument = (Document) document.get("settings");

        int x = settingsDocument.getInteger("spawnX");
        int y = settingsDocument.getInteger("spawnY");
        int z = settingsDocument.getInteger("spawnZ");

        World world = Bukkit.getWorld(domain.getWorldId().toString());

        return new Location(world, x, y, z);
    }

    public static boolean isRegistered(Domain domain) {
        MongoConnection connection = MineDomains.getInstance().getDataManager().getConnection();
        MongoCollection<Document> collection = connection.getMongoCollection();
        Document document = collection.find(Filters.eq("_id", domain.getId().toString())).first();
        return document != null;
    }

    public static void updateDomain(Domain domain) throws DomainRegistrationException {
        MongoConnection connection = MineDomains.getInstance().getDataManager().getConnection();
        MongoCollection<Document> collection = connection.getMongoCollection();
        Document document = collection.find(Filters.eq("_id", domain.getId().toString())).first();

        if (document == null) {
            throw new DomainRegistrationException("Domain not registered in the mongo database.");
        }

        collection.replaceOne(document, domain.toDocument());
    }

}
