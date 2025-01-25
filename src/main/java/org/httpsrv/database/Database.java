package org.httpsrv.database;

import static dev.morphia.query.experimental.filters.Filters.eq;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.*;
import dev.morphia.*;
import dev.morphia.mapping.MapperOptions;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import org.bson.Document;
import org.httpsrv.conf.Config;
import org.httpsrv.database.entity.*;

public class Database {
    private static MongoClient instance;
    @Getter private static Datastore dataStore;
    private static final ExecutorService eventExecutor =new ThreadPoolExecutor(6, 6, 60, TimeUnit.SECONDS,new LinkedBlockingDeque<>(), new ThreadPoolExecutor.AbortPolicy());

    public static void Start() {
        initialize(Config.getPropertiesVar().dbUrl, Config.getPropertiesVar().dbName);
    }

    private static void initialize(String dbUrl, String dbName) {
        instance = MongoClients.create(dbUrl);

        // Set mapper options.
        MapperOptions mapperOptions = MapperOptions.builder().storeEmpties(true).storeNulls(false).build();
        dataStore = Morphia.createDatastore(instance, dbName, mapperOptions);
        dataStore.ensureIndexes();
    }

    public static boolean logCollection(String logName, Object log) {
        if(!Config.getPropertiesVar().saveLogs) return true;

        try {
            ObjectMapper mapper = new ObjectMapper();
            MongoCollection<Document> collection = Database.getDataStore().getDatabase().getCollection(logName);

            if(log instanceof String) {
                collection.insertOne(Document.parse((String)log));
            }
            else {
                collection.insertOne(Document.parse(mapper.writeValueAsString(log)));
            }

            return true;
        } catch (JsonProcessingException ex) {
            return false;
        }
    }

    public static Account findAccountByEmail(String email) {
        return getDataStore().find(Account.class).filter(eq("email", email)).first();
    }

    public static Account findAccountByMobile(String mobile) {
        return getDataStore().find(Account.class).filter(eq("mobile", mobile)).first();
    }

    public static Account findAccountByToken(String id) {
        return getDataStore().find(Account.class).filter(eq("sessionKey", id)).first();
    }

    public static Account findAccountById(String id) {
        return getDataStore().find(Account.class).filter(eq("_id", id)).first();
    }

    public static Ticket findTicket(String email, String type, boolean useMobile) {
        if(useMobile) {
            return getDataStore().find(Ticket.class).filter(eq("mobile", email), eq("type", type)).first();
        }

        return getDataStore().find(Ticket.class).filter(eq("email", email), eq("type", type)).first();
    }

    public static Ticket findTicket(String id) {
        return getDataStore().find(Ticket.class).filter(eq("_id", id)).first();
    }

    public static void saveAccountAsync(Object object) {
        eventExecutor.submit(() -> getDataStore().save(object));
    }

    public static void deleteAccountAsync(Object object) {eventExecutor.submit(() -> getDataStore().delete(object));}

    public static void deleteTicketById(String id) {
        getDataStore().find(Ticket.class).filter(eq("_id", id)).delete();
    }

    public static List<Goods> findAllGoods(String id) {
        return getDataStore().find(Goods.class).filter(eq("goods_owner", id)).stream().toList();
    }

    public static Account findAccountByStoken(String token) {
        return getDataStore().find(Account.class).filter(eq("stokenKey", token)).first();
    }

    public static Account findAccountByDeviceId(String deviceId) {
        return getDataStore().find(Account.class).filter(eq("deviceId", deviceId)).first();
    }

    public static Account findGuestAccount(String device) {
        return getDataStore().find(Account.class).filter(eq("deviceId", device), eq("accountType", 0)).first();
    }

    public static Integer getNextId(String collectionName) {
        MongoCollection<Document> collection = getDataStore().getDatabase().getCollection(collectionName);
        return Math.toIntExact(collection.countDocuments());
    }
}