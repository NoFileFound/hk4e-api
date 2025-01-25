package org.httpsrv.thirdparty;

import com.maxmind.geoip2.WebServiceClient;
import org.httpsrv.conf.Config;

import java.net.InetAddress;

public final class GeoIP {
    private static WebServiceClient client;

    public static void loadGeoIp() {
        client = new WebServiceClient.Builder(Config.getPropertiesVar().geoipAccountId, Config.getPropertiesVar().geoipKey).build();
    }

    public static String getCountryCode(String ip) {
        try {
            InetAddress ipAddress = InetAddress.getByName(ip);
            return client.city(ipAddress).getCountry().getIsoCode();
        }catch (Exception ignored) {
            return "";
        }
    }
}