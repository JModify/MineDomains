package net.minefury.domains.data.mongo;

import com.modify.fundamentum.text.PlugLogger;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import net.minefury.domains.MineDomains;
import net.minefury.domains.data.flatfile.ConfigFile;
import net.minefury.domains.exceptions.MongoCredentialException;
import org.bson.Document;

/**
 * Represents the mongo collection which the plugin will work through.
 * Holds reference to the mongo collection, mongo database and mongo client
 */
public class MongoConnection {

    /** A MongoDB client with internal connection pooling **/
    @Getter private MongoClient mongoClient;

    /** The MongoDatabase interface, used for database management **/
    @Getter private MongoDatabase mongoDatabase;

    /** Collection which the plugin will work in **/
    @Getter private MongoCollection<Document> mongoCollection;

    /**
     * Connects to the mongo database using credentials given in configuration file.
     * If URI link is present and is valid, the variables; host, port, username, password and auth
     * will be ignored - only the uri, database and collection variables will be used
     *
     * If URI link is not present or is invalid, all variables in config will be used
     * except the missing/invalid uri string.
     */
    public void connect() {
        try {
            String host = getCredential("host");
            String port = getCredential("port");
            String database = getCredential("database");
            String collection = getCredential("collection");
            String username = getCredential("username");
            String password = getCredential("password");
            String configUri = getCredential("uri");
            String auth = getCredential("auth");

            String authParams = username + ":" + password + "@";
            String authSource = "/?authSource=" + auth;
            String uri = isUriValid(configUri) ? configUri : "mongodb://" + authParams + host + ":" + port + authSource;

            this.mongoClient = MongoClients.create(uri);
            this.mongoDatabase = mongoClient.getDatabase(database);
            this.mongoCollection = mongoDatabase.getCollection(collection);
            PlugLogger.logInfo("Successfully connected to mongodb");
        } catch (MongoCredentialException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the given URI link is valid.
     * This consists of a null check, empty value check and a default value check.
     *
     * @param uri uri string to check.
     * @return true if the uri is valid, else false.
     */
    private boolean isUriValid(String uri) {
        //TODO: Improve URI validity check to prevent general mongo exceptions.
        if (uri.equals("uri") || uri.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * Retrieves the credential value from config.yml file.
     *
     * @param credential credentials to retrieve value for
     * @return string of the credential's value.
     *
     * @throws MongoCredentialException if a given credential is not present.
     */
    private String getCredential(String credential) throws MongoCredentialException {
        ConfigFile config = MineDomains.getInstance().getDataManager().getConfig();

        String result = config.getString("mongodb." + credential);
        if (result == null || result.isEmpty()) {
            throw new MongoCredentialException("Failed to retrieve mongo credential \"" + credential + "\". Corrupt config.yml?");
        }
        return result;
    }
}
