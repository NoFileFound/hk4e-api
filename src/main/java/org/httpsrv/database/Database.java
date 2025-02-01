package org.httpsrv.database;

import static dev.morphia.query.experimental.filters.Filters.eq;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.*;
import dev.morphia.*;
import dev.morphia.mapping.MapperOptions;
import java.util.ArrayList;
import java.util.Arrays;
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

    /**
     * Connects to the database.
     */
    public static void Start() {
        initialize(Config.getPropertiesVar().dbUrl, Config.getPropertiesVar().dbName);
    }

    /**
     * Collects the game logging.
     * @param logName The log name.
     * @param log The log content.
     */
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

    /**
     * Searches for account instance by given device id.
     * @param deviceId The given device identifier.
     * @return The account instance if exist or else null.
     */
    public static Account findAccountByDeviceId(String deviceId) {
        return getDataStore().find(Account.class).filter(eq("currentDeviceId", deviceId)).first();
    }

    /**
     * Searches for account instance by given email address.
     * @param email The given email address.
     * @return The account instance if exist or else null.
     */
    public static Account findAccountByEmail(String email) {
        return getDataStore().find(Account.class).filter(eq("email", email)).first();
    }

    /**
     * Searches for account instance by given mobile number.
     * @param mobile The given mobile number.
     * @return The account instance if exist or else null.
     */
    public static Account findAccountByMobile(String mobile) {
        return getDataStore().find(Account.class).filter(eq("mobile", mobile)).first();
    }

    /**
     * Searches for account instance by given session token.
     * @param key The given session token.
     * @return The account instance if exist or else null.
     */
    public static Account findAccountByToken(String key) {
        return getDataStore().find(Account.class).filter(eq("sessionKey", key)).first();
    }

    /**
     * Searches for account instance by given stoken.
     * @param token The given stoken.
     * @return The account instance if exist or else null.
     */
    public static Account findAccountByStoken(String token) {
        return getDataStore().find(Account.class).filter(eq("stokenKey", token)).first();
    }

    /**
     * Searches for account instance by id.
     * @param id The given id.
     * @return The account instance if exist or else null.
     */
    public static Account findAccountById(String id) {
        return getDataStore().find(Account.class).filter(eq("_id", id)).first();
    }

    /**
     * Searches for launcher experiments by given ids.
     * @param id The given id.
     * @return The list of experiments.
     */
    public static List<LauncherExperiment> findAllLauncherExperiments(String id) {
        List<String> scenes = new ArrayList<>(Arrays.asList(id.split(",")));
        List<LauncherExperiment> experimentsList = new ArrayList<>();

        for(String scene : scenes) {
            List<LauncherExperiment> tmp = getDataStore().find(LauncherExperiment.class).filter(eq("config_id", scene)).stream().toList();
            experimentsList.addAll(tmp);
        }

        return experimentsList;
    }

    /**
     * Searches for all blacklisted devices.
     * @return The list of all blacklisted devices.
     */
    public static List<FirebaseBL> findAllBlacklistedDevices() {
        return getDataStore().find(FirebaseBL.class).stream().toList();
    }

    /**
     * Searches for experiments by given scene ids.
     * @param scene_id The given scene id.
     * @return The list of experiments.
     */
    public static List<Experiment> findAllExperiments(String scene_id) {
        List<String> scenes = new ArrayList<>(Arrays.asList(scene_id.split(",")));
        List<Experiment> experimentsList = new ArrayList<>();

        for(String scene : scenes) {
            List<Experiment> tmp = getDataStore().find(Experiment.class).filter(eq("config_id", scene)).stream().toList();
            experimentsList.addAll(tmp);
        }

        return experimentsList;
    }

    /**
     * Searches for all payment platforms.
     * @return The given payment platforms.
     */
    public static List<PayPlatform> findAllPaymentPlatforms() {
        return getDataStore().find(PayPlatform.class).stream().toList();
    }

    /**
     * Searches for all payment tiers.
     * @return The list of all payment  tiers.
     */
    public static List<PayTier> findAllPaymentTiers() {
        return getDataStore().find(PayTier.class).stream().toList();
    }

    /**
     * Searches for all payment options.
     * @return The given payment options.
     */
    public static List<PayType> findAllPayTypes() {
        return getDataStore().find(PayType.class).stream().toList();
    }

    /**
     * Searches for all device extensions by given platform id.
     * @param platform The given platform id.
     * @return The DeviceExt object.
     */
    public static DeviceExt findDeviceExtensions(int platform) {
        return getDataStore().find(DeviceExt.class).filter(eq("platform", platform)).first();
    }

    /**
     * Searches for guest account instance by id.
     * @param device The given device identifier.
     * @return The account instance if exist or else null.
     */
    public static Account findGuestAccount(String device) {
        return getDataStore().find(Account.class).filter(eq("currentDeviceId", device), eq("accountType", 0)).first();
    }

    /**
     * Searches for ticket by id.
     * @param id The given id.
     * @param action_type The given action type.
     * @return The ticket instance if exist or else null.
     */
    public static Ticket findTicket(String id, String action_type) {
        return getDataStore().find(Ticket.class).filter(eq("_id", id), eq("type", action_type)).first();
    }

    /**
     * Searches for ticket by account id.
     * @param accountId The given account id.
     * @param action_type The given action type.
     * @return The ticket instance if exist or else null.
     */
    public static Ticket findTicketByAccountId(String accountId, String action_type) {
        return getDataStore().find(Ticket.class).filter(eq("type", action_type), eq("accountId", accountId)).first();
    }

    /**
     * Searches for user tag by alias and region.
     * @param alias The given alias.
     * @param region The given region.
     * @return The ticket instance if exist or else null.
     */
    public static UserTag findUserTag(String alias, String region) {
        return getDataStore().find(UserTag.class).filter(eq("alias", alias), eq("region", region)).first();
    }

    /**
     * Searches for webapi profile by given account id.
     * @param id The given account id.
     * @return The WebProfile instance if exist or else null.
     */
    public static WebProfile findWebProfile(String id) {
        return getDataStore().find(WebProfile.class).filter(eq("_id", id)).first();
    }

    /**
     * Searches for webapi profile by given cookie token.
     * @param cookieToken The given cookie token.
     * @return The WebProfile instance if exist or else null.
     */
    public static WebProfile findWebProfileByCookie(String cookieToken) {
        return getDataStore().find(WebProfile.class).filter(eq("cookieKey", cookieToken)).first();
    }


    public static void saveObjectAsync(Object object) {
        eventExecutor.submit(() -> getDataStore().save(object));
    }

    public static void deleteObjectAsync(Object object) {eventExecutor.submit(() -> getDataStore().delete(object));}

    public static Integer getNextId(String collectionName) {
        MongoCollection<Document> collection = getDataStore().getDatabase().getCollection(collectionName);
        return Math.toIntExact(collection.countDocuments());
    }

    private static void initialize(String dbUrl, String dbName) {
        instance = MongoClients.create(dbUrl);

        // Set mapper options.
        MapperOptions mapperOptions = MapperOptions.builder().storeEmpties(true).storeNulls(false).build();
        dataStore = Morphia.createDatastore(instance, dbName, mapperOptions);
        dataStore.ensureIndexes();
    }
}