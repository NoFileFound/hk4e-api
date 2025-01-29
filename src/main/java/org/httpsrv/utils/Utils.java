package org.httpsrv.utils;

import java.security.SecureRandom;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;

public final class Utils {
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static boolean checkBizName(String biz) {
        return biz.equals("hk4e_global") || biz.equals("hk4e_cn");
    }

    public static boolean checkGameLanguage(String language) {
        return language.equals("en") || language.equals("zh-tw") || language.equals("zh-cn") || language.equals("ja-jp");
    }

    public static boolean checkGameVersion(String version) {
        return version.contains("OSRELWin") || version.contains("OSRELAndroid") || version.contains("CNRELWin") || version.contains("CNRELAndroid");
    }

    public static int getConfigId(Integer platformId) {
        return switch (platformId) {
            case 1 -> 4;
            case 2 -> 5;
            case 3 -> 6;
            case 6 -> 30;
            case 8 -> 27;
            case 9 -> 53;
            case 10 -> 26;
            case 11 -> 28;
            case 13 -> 44;
            default -> -1;
        };
    }

    public static String getPlatformNameById(Integer id) {
        return switch (id) {
            case 1 -> "IOS";
            case 2 -> "Android";
            case 3 -> "PC";
            case 4, 5 -> "Browser";
            case 6 -> "PS";
            case 8 -> "CloudAndroid";
            case 9 -> "CloudPC";
            case 10 -> "CloudIOS";
            case 11 -> "PS5";
            case 12 -> "MacOS";
            case 13 -> "CloudMacOS";
            default -> "";
        };
    }

    public static String maskString(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        } else if (text.length() < 4) {
            return "*".repeat(text.length()); // Mask the entire string if less than 4 characters
        } else {
            int start = text.length() >= 10 ? 2 : 1;
            int end = text.length() > 5 ? 2 : 1;
            String uncoveredStart = text.substring(0, start);
            String uncoveredEnd = text.substring(text.length() - end);
            String maskedMiddle = "*".repeat(text.length() - start - end);
            return uncoveredStart + maskedMiddle + uncoveredEnd;
        }
    }

    public static byte[] readResourceFile(String fileName) {
        try {
            return new ClassPathResource(fileName).getInputStream().readAllBytes();
        }catch (Exception ex) {
            return null;
        }
    }

    public static String generateMessage(Map<String, Object> data) {
        return data.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + (entry.getValue() == null ? "" : entry.getValue()))
                .collect(Collectors.joining("&"));
    }
}