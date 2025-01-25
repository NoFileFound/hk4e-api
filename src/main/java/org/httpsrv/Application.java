package org.httpsrv;

import java.util.HashMap;
import java.util.Map;
import org.httpsrv.algorithms.RSA;
import org.httpsrv.conf.Config;
import org.httpsrv.database.Database;
import org.httpsrv.thirdparty.GeoIP;
import org.httpsrv.thirdparty.JakartaMail;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        Config.loadConfig();
        RSA.loadEncryption();
        JakartaMail.initSmtpConfig();
        GeoIP.loadGeoIp();
        Database.Start();

        SpringApplication app = new SpringApplication(Application.class);
        Map<String, Object> defaultProperties = new HashMap<>();
        defaultProperties.put("server.port", Config.getPropertiesVar().port);

        app.setDefaultProperties(defaultProperties);
        app.setWebApplicationType(WebApplicationType.SERVLET);
        app.run(args);
    }
}