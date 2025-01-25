package org.httpsrv.conf.var;

import java.util.ArrayList;
import java.util.List;

public class PropertiesVar {
    public int port = 8881;
    public boolean saveLogs = false;
    public String privateKey = "touhou"; // do not leak
    public List<String> appId = new ArrayList<>(List.of(
            "c77bxgx7ljb4", // overseas (pc)
            "c76ync6mutq8" // chinese (android)
    ));
    public List<String> launcherIds = new ArrayList<>(List.of(
            "8fANlj5K7I" // overseas (pc)
    ));
    public String geetestPrivateKey = "";
    public String geetestId = "";
    public String smtpHost = "smtp.gmail.com";
    public Integer smtpPort = 587;
    public String smtpUsername = "";
    public String smtpPassword = "";
    public String twilioPhone = "";
    public String twilioToken = "";
    public String twilioSID = "";
    public String dbUrl = "mongodb://localhost:27017";
    public String dbName = "genshinimpact";
    public String geoipKey = "";
    public Integer geoipAccountId = 0;
}