package org.httpsrv.conf;

import lombok.Getter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.httpsrv.conf.var.*;
import org.httpsrv.utils.Jackson;

public final class Config {
    private static final File configFile = new File("config.json");
    private static final File regionFile = new File("region.json");
    private static final File announcementFile = new File("announcement.json");
    @Getter private static ConfigVar httpConfig;
    @Getter private static PropertiesVar propertiesVar = new PropertiesVar();
    @Getter private static RegionVar regionVar = new RegionVar();
    @Getter private static AnnouncementVar announcementVar = new AnnouncementVar();

    public static void loadConfig() {
        try {
            httpConfig = Jackson.fromJsonString(configFile, ConfigVar.class);
            regionVar = Jackson.fromJsonString(regionFile, RegionVar.class);
            announcementVar = Jackson.fromJsonString(announcementFile, AnnouncementVar.class);

        } catch (IOException ex) {
            ex.printStackTrace();

            httpConfig = new ConfigVar();
            regionVar = new RegionVar();
            announcementVar = new AnnouncementVar();
            saveConfig();
        }
    }

    private static void saveConfig() {
        try {
            saveConfig(httpConfig);
            saveConfig(regionVar);
            saveConfig(announcementVar);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void saveConfig(ConfigVar httpConfig) throws IOException {
        try (FileWriter file = new FileWriter(configFile)) {
            file.write(Jackson.toJsonString(httpConfig));
        }

    }

    private static void saveConfig(RegionVar regionVar) throws IOException {
        try (FileWriter file = new FileWriter(regionFile)) {
            file.write(Jackson.toJsonString(regionVar));
        }
    }

    private static void saveConfig(AnnouncementVar announcementVar) throws IOException {
        try (FileWriter file = new FileWriter(announcementFile)) {
            file.write(Jackson.toJsonString(announcementVar));
        }
    }
}