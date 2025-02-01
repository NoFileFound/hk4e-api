package org.httpsrv.controllers.account;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.httpsrv.algorithms.RSA;
import org.httpsrv.conf.Config;
import org.httpsrv.data.Retcode;
import org.httpsrv.data.body.account.AthBindMobileBody;
import org.httpsrv.data.body.account.AthBindRealnameBody;
import org.httpsrv.data.body.account.AthModifyRealnameBody;
import org.httpsrv.database.Database;
import org.httpsrv.database.entity.Account;
import org.httpsrv.database.entity.Ticket;
import org.httpsrv.database.entity.WebProfile;
import org.httpsrv.thirdparty.GeoIP;
import org.httpsrv.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "account/auth/api", produces = "application/json")
public class Auth implements org.httpsrv.ResponseHandler {
    @Override public LinkedHashMap<String, Object> makeResponse(Retcode retcode, String message, Object data) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        response.put("retcode", retcode);
        response.put("message", message);
        response.put("data", data);

        return response;
    }

    /**
     *  Source: <a href="https://gameapi-account.mihoyo.com/account/auth/api/cookieLogout">https://gameapi-account.mihoyo.com/account/auth/api/cookieLogout</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br>
     */
    @PostMapping(value = "cookieLogout")
    public ResponseEntity<LinkedHashMap<String, Object>> SendCookieLogout() {
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>()));
    }

    /**
     *  Source: <a href="https://gameapi-account.mihoyo.com/account/auth/api/genAuthKey">https://gameapi-account.mihoyo.com/account/auth/api/genAuthKey</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - game_biz: Game region id.<br>
     */
    @PostMapping(value = "genAuthKey")
    public ResponseEntity<LinkedHashMap<String, Object>> SendAuthKey(@RequestBody String game_biz, @CookieValue(value = "stoken") String stoken) {
        Account account = Database.findAccountByStoken(stoken);
        if(account == null || !Utils.checkBizName(game_biz)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_FAILED, "登录失效，请重新登录", null));
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("auth_key", account.getSessionKey().getBytes());
            put("sign_type", 2);
            put("auth_key_ver", 1);
        }}));
    }

    /**
     *  Source: <a href="https://gameapi-account.mihoyo.com/account/auth/api/getLTokenBySToken">https://gameapi-account.mihoyo.com/account/auth/api/getLTokenBySToken</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br>
     */
    @GetMapping(value = "getLTokenBySToken")
    public ResponseEntity<LinkedHashMap<String, Object>> SendLTokenBySToken(@CookieValue(value = "stoken") String stoken) {
        Account account = Database.findAccountByStoken(stoken);
        if(account == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_FAILED, "登录失效，请重新登录", null));
        }

        WebProfile profile = Database.findWebProfile(account.getId());
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("ltoken", profile.getLtoken());
        }}));
    }

    /**
     *  Source: <a href="https://api-account-os.hoyoverse.com/account/auth/api/getConfig">https://api-account-os.hoyoverse.com/account/auth/api/getConfig</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br>
     */
    @GetMapping(value = "getConfig")
    public ResponseEntity<LinkedHashMap<String, Object>> SendConfig(HttpServletRequest request) {
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("ip", new LinkedHashMap<String, Object>() {{
                put("country_code", GeoIP.getCountryCode(request.getRemoteAddr()));
                put("language", "en-us");
                put("area_code", GeoIP.getCountryMobileCode(request.getRemoteAddr()));
            }});
            put("area_wl", new ArrayList<>(List.of("KR")));
            put("realname_wl", new ArrayList<>());
            put("guardian_age_limit", "14");
            put("disable_mmt", Config.getHttpConfig().disableMMT);
            put("show_birthday", "false");
        }}));
    }

    /**
     *  Source: <a href="https://gameapi-account.mihoyo.com/account/auth/api/getCookieAccountInfoBySToken">https://gameapi-account.mihoyo.com/account/auth/api/getCookieAccountInfoBySToken</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - game_biz: Game region id.<br>
     */
    @GetMapping(value = "getCookieAccountInfoBySToken")
    public ResponseEntity<LinkedHashMap<String, Object>> SendCookieAccountInfoBySToken(String game_biz, @CookieValue(value = "stoken") String stoken) {
        if(game_biz == null || !Utils.checkBizName(game_biz) || stoken == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "参数错误", null));
        }

        Account account = Database.findAccountByStoken(stoken);
        if(account == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_FAILED, "登录失效，请重新登录", null));
        }

        WebProfile profile = Database.findWebProfile(account.getId());
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("uid", account.getId());
            put("cookie_token", profile.getCookieToken().getBytes());
        }}));
    }

    /**
     *  Source: <a href="https://gameapi-account.mihoyo.com/account/auth/api/getMultiTokenByLoginTicket">https://gameapi-account.mihoyo.com/account/auth/api/getMultiTokenByLoginTicket</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - login_ticket: Ticket to log in.<br>
     *      - token_types: Token type.<br>
     *      - uid: Account id.<br>
     */
    @GetMapping(value = "getMultiTokenByLoginTicket")
    public ResponseEntity<LinkedHashMap<String, Object>> SendMultiTokenByLoginTicket(String login_ticket, Integer token_types, String uid) {
        if(login_ticket == null || token_types == null || uid == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "参数错误", null));
        }

        Ticket info = Database.findTicket(login_ticket, "getMultiToken");
        if(info == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_BBS_NOT_LOGIN, "登录状态失效，请重新登录", null));
        }

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<String, Object>() {{
            put("list", new ArrayList<>());
        }}));
    }

    /**
     *  Source: <a href="https://gameapi-account.mihoyo.com/account/auth/api/getCookieAccountInfoByGameToken">https://gameapi-account.mihoyo.com/account/auth/api/getCookieAccountInfoByGameToken</a><br><br>
     *  Method: GET<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - account_id: Account id.<br>
     *      - game_token: Account session key.<br>
     */
    @GetMapping(value = "getCookieAccountInfoByGameToken")
    public ResponseEntity<LinkedHashMap<String, Object>> SendCookieAccountInfoByGameToken(String account_id, String game_token) {
        if(account_id == null || game_token == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "参数错误", null));
        }

        Account account = Database.findAccountByToken(game_token);
        if(account == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_FAILED, "登录失效，请重新登录", null));
        }

        WebProfile profile = Database.findWebProfile(account.getId());
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("uid", account.getId());
            put("cookie_token", profile.getCookieToken().getBytes());
        }}));
    }

    /**
     *  Source: <a href="https://gameapi-account.mihoyo.com/account/auth/api/modifyRealname">https://gameapi-account.mihoyo.com/account/auth/api/modifyRealname</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - action_ticket: Ticket id.<br>
     *      - realname: Real name.<br>
     *      - identity_card: Identity card.<br>
     *      - is_crypto: Is encrypted with RSA.<br>
     */
    @PostMapping(value = "modifyRealname")
    public ResponseEntity<LinkedHashMap<String, Object>> SendModifyRealname(@RequestBody AthModifyRealnameBody body) {
        String actionTicket = body.getAction_ticket();
        String realName = body.getRealname();
        String identityCard = body.getIdentity_card();
        Boolean isEncrypted = body.getIs_crypto();

        if(actionTicket == null || realName == null || identityCard == null || isEncrypted == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "参数错误", null));
        }

        Ticket myTicket = Database.findTicket(actionTicket, "modify_realname");
        if(myTicket == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_CANCEL, "系统请求失败，请返回重试", null));
        }

        String encIdentityCard = identityCard;
        String encRealName = realName;
        if(isEncrypted) {
            try {
                realName = RSA.DecryptPassword(realName);
                identityCard = RSA.DecryptPassword(identityCard);
            }catch(Exception ignored) {
                return ResponseEntity.ok(this.makeResponse(Retcode.RET_SYSTEM_ERROR, "系统错误", null));
            }
        }

        Account account = Database.findAccountById(myTicket.getAccountId());
        account.setRealname(realName);
        account.setIdentityCard(identityCard);
        account.setRealPersonOperationName("None");
        account.setRequireRealPerson(false);
        account.save();

        myTicket.delete();

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("realname_operation", "updated");
            put("uid", account.getId());
            put("name", Utils.maskString(account.getName()));
            put("email", Utils.maskString(account.getEmail()));
            put("identity_card", encIdentityCard);
            put("realname", encRealName);
        }}));
    }

    /**
     *  Source: <a href="https://gameapi-account.mihoyo.com/account/auth/api/bindRealname">https://gameapi-account.mihoyo.com/account/auth/api/bindRealname</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - action_ticket: Ticket id.<br>
     *      - realname: Real name.<br>
     *      - identity_card: Identity card.<br>
     *      - is_crypto: Is encrypted with RSA.<br>
     */
    @PostMapping(value = "bindRealname")
    public ResponseEntity<LinkedHashMap<String, Object>> SendBindRealname(@RequestBody AthBindRealnameBody body) {
        String actionTicket = body.getTicket();
        String realName = body.getRealname();
        String identityCard = body.getIdentity();
        Boolean isEncrypted = body.getIs_crypto();

        if(actionTicket == null || realName == null || identityCard == null || isEncrypted == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "参数错误", null));
        }

        Ticket myTicket = Database.findTicket(actionTicket, "bind_realname");
        if(myTicket == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_CANCEL, "系统请求失败，请返回重试", null));
        }

        String encIdentityCard = identityCard;
        String encRealName = realName;
        if(isEncrypted) {
            try {
                realName = RSA.DecryptPassword(realName);
                identityCard = RSA.DecryptPassword(identityCard);
            }catch(Exception ignored) {
                return ResponseEntity.ok(this.makeResponse(Retcode.RET_SYSTEM_ERROR, "系统错误", null));
            }
        }

        Account account = Database.findAccountById(myTicket.getAccountId());
        account.setRealname(realName);
        account.setIdentityCard(identityCard);
        account.setRealPersonOperationName("None");
        account.setRequireRealPerson(false);
        account.save();

        myTicket.delete();

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("realname_operation", "completed");
            put("uid", account.getId());
            put("name", Utils.maskString(account.getName()));
            put("email", Utils.maskString(account.getEmail()));
            put("identity_card", encIdentityCard);
            put("realname", encRealName);
        }}));
    }

    /**
     *  Source: <a href="https://gameapi-account.mihoyo.com/account/auth/api/bindMobile">https://gameapi-account.mihoyo.com/account/auth/api/bindMobile</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - area_code: International mobile code.<br>
     *      - ticket: Ticket id.<br>
     *      - mobile: Mobile number.<br>
     *      - captcha: Verification code.<br>
     *      - uid: Account id.<br>
     */
    @PostMapping(value = "bindSafeMobile")
    public ResponseEntity<LinkedHashMap<String, Object>> SendBindSafeMobile(@RequestBody AthBindMobileBody body) {
        String actionTicket = body.getTicket();
        String areaCode = body.getArea_code();
        String mobile = body.getMobile();
        String verCode = body.getCaptcha();
        String userId = body.getUid();

        if(areaCode == null || mobile == null || actionTicket == null || verCode == null || userId == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "参数错误", null));
        }

        Ticket myTicket = Database.findTicket(actionTicket, "bind_safemobile");
        if(myTicket == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_CANCEL, "系统请求失败，请返回重试", null));
        }

        if(!myTicket.getCode().equals(verCode)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MISSING_CONFIGURATION, "验证码错误", null));
        }

        Account account = Database.findAccountById(userId);
        account.setRequireSafeMobile(false);
        account.setSafeMobile(mobile);
        account.setSafeMobileArea(areaCode.replace("+", ""));
        account.save();

        myTicket.delete();

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("ticket", actionTicket);
        }}));
    }

    /**
     *  Source: <a href="https://gameapi-account.mihoyo.com/account/auth/api/bindMobile">https://gameapi-account.mihoyo.com/account/auth/api/bindMobile</a><br><br>
     *  Method: POST<br>
     *  Content-Type: application/json<br><br>
     *  Parameters:<br>
     *      - area_code: International mobile code.<br>
     *      - ticket: Ticket id.<br>
     *      - mobile: Mobile number.<br>
     *      - captcha: Verification code.<br>
     *      - uid: Account id.<br>
     */
    @PostMapping(value = "bindMobile")
    public ResponseEntity<LinkedHashMap<String, Object>> SendBindMobile(@RequestBody AthBindMobileBody body) {
        String area_code = body.getArea_code();
        String mobile = body.getMobile();
        String actionTicket = body.getTicket();
        String verCode = body.getCaptcha();
        String userId = body.getUid();

        if(area_code == null || mobile == null || actionTicket == null || verCode == null || userId == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_PARAMETER_ERROR, "参数错误", null));
        }

        Ticket myTicket = Database.findTicket(actionTicket, "bind_mobile");
        if(myTicket == null) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_LOGIN_CANCEL, "系统请求失败，请返回重试", null));
        }

        if(!myTicket.getCode().equals(verCode)) {
            return ResponseEntity.ok(this.makeResponse(Retcode.RET_MISSING_CONFIGURATION, "验证码错误", null));
        }

        Account account = Database.findAccountById(userId);
        account.setRequireSafeMobile(false);
        account.setMobile(mobile);
        account.setMobileArea(area_code.replace("+", ""));
        account.save();

        myTicket.delete();

        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", new LinkedHashMap<>() {{
            put("ticket", actionTicket);
        }}));
    }

    /**
     *  Source: <a href="https://api-account-os.hoyoverse.com/account/auth/api/getAreaCode">https://api-account-os.hoyoverse.com/account/auth/api/getAreaCode</a><br><br>
     *  Methods: GET, POST<br>
     *  Content-Type: application/json<br>
     */
    @RequestMapping(value = "getAreaCode", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<LinkedHashMap<String, Object>> SendAreaCode() {
        String[][] countryCodes = {
                {"CN", "86"}, {"AF", "93"}, {"AL", "355"}, {"DZ", "213"}, {"AS", "1684"}, {"AD", "376"},
                {"AO", "244"}, {"AI", "1264"}, {"AG", "1268"}, {"AR", "54"}, {"AM", "374"}, {"AW", "297"},
                {"AU", "61"}, {"AT", "43"}, {"AZ", "994"}, {"BS", "1242"}, {"BH", "973"}, {"BD", "880"},
                {"BB", "1246"}, {"BY", "375"}, {"BE", "32"}, {"BZ", "501"}, {"BJ", "229"}, {"BM", "1441"},
                {"BT", "975"}, {"BO", "591"}, {"BA", "387"}, {"BW", "267"}, {"BR", "55"}, {"BN", "673"},
                {"BG", "359"}, {"BF", "226"}, {"BI", "257"}, {"KH", "855"}, {"CM", "237"}, {"CA", "1"},
                {"CV", "238"}, {"KY", "1345"}, {"CF", "236"}, {"TD", "235"}, {"CL", "56"}, {"CO", "57"},
                {"KM", "269"}, {"CK", "682"}, {"CR", "506"}, {"HR", "385"}, {"CU", "53"}, {"CW", "599"},
                {"CY", "357"}, {"CZ", "420"}, {"CD", "243"}, {"DK", "45"}, {"DJ", "253"}, {"DM", "1767"},
                {"DO", "1809"}, {"EC", "593"}, {"EG", "20"}, {"SV", "503"}, {"GQ", "240"}, {"ER", "291"},
                {"EE", "372"}, {"ET", "251"}, {"FO", "298"}, {"FJ", "679"}, {"FI", "358"}, {"FR", "33"},
                {"GF", "594"}, {"PF", "689"}, {"GA", "241"}, {"GM", "220"}, {"GE", "995"}, {"DE", "49"},
                {"GH", "233"}, {"GI", "350"}, {"GR", "30"}, {"GL", "299"}, {"GD", "1473"}, {"GP", "590"},
                {"GU", "1671"}, {"GT", "502"}, {"GN", "224"}, {"GW", "245"}, {"GY", "592"}, {"HT", "509"},
                {"HN", "504"}, {"HK", "852"}, {"HU", "36"}, {"IS", "354"}, {"IN", "91"}, {"ID", "62"},
                {"IR", "98"}, {"IQ", "964"}, {"IE", "353"}, {"IL", "972"}, {"IT", "39"}, {"CI", "225"},
                {"JM", "1876"}, {"JP", "81"}, {"JO", "962"}, {"KZ", "7"}, {"KE", "254"}, {"KI", "686"},
                {"KW", "965"}, {"KG", "996"}, {"LA", "856"}, {"LV", "371"}, {"LB", "961"}, {"LS", "266"},
                {"LR", "231"}, {"LY", "218"}, {"LI", "423"}, {"LT", "370"}, {"LU", "352"}, {"MO", "853"},
                {"MK", "389"}, {"MG", "261"}, {"MW", "265"}, {"MY", "60"}, {"MV", "960"}, {"ML", "223"},
                {"MT", "356"}, {"MQ", "596"}, {"MR", "222"}, {"MU", "230"}, {"YT", "269"}, {"MX", "52"},
                {"MD", "373"}, {"MC", "377"}, {"MN", "976"}, {"ME", "382"}, {"MS", "1664"}, {"MA", "212"},
                {"MZ", "258"}, {"MM", "95"}, {"NA", "264"}, {"NP", "977"}, {"NL", "31"}, {"NC", "687"},
                {"NZ", "64"}, {"NI", "505"}, {"NE", "227"}, {"NG", "234"}, {"NO", "47"}, {"OM", "968"},
                {"PK", "92"}, {"PW", "680"}, {"BL", "970"}, {"PA", "507"}, {"PG", "675"}, {"PY", "595"},
                {"PE", "51"}, {"PH", "63"}, {"PL", "48"}, {"PT", "351"}, {"PR", "1787"}, {"QA", "974"},
                {"CG", "242"}, {"RE", "262"}, {"RO", "40"}, {"RU", "7"}, {"RW", "250"}, {"KN", "1869"},
                {"LC", "1758"}, {"PM", "508"}, {"VC", "1784"}, {"WS", "685"}, {"SM", "378"}, {"ST", "239"},
                {"SA", "966"}, {"SN", "221"}, {"RS", "381"}, {"SC", "248"}, {"SL", "232"}, {"SG", "65"},
                {"SX", "1721"}, {"SK", "421"}, {"SI", "386"}, {"SB", "677"}, {"SO", "252"}, {"ZA", "27"},
                {"KR", "82"}, {"ES", "34"}, {"LK", "94"}, {"SD", "249"}, {"SR", "597"}, {"SZ", "268"},
                {"SE", "46"}, {"CH", "41"}, {"SY", "963"}, {"TW", "886"}, {"TJ", "992"}, {"TZ", "255"},
                {"TH", "66"}, {"TL", "670"}, {"TG", "228"}, {"TO", "676"}, {"TT", "1868"}, {"TN", "216"},
                {"TR", "90"}, {"TM", "993"}, {"TC", "1649"}, {"UG", "256"}, {"UA", "380"}, {"AE", "971"},
                {"GB", "44"}, {"US", "1"}, {"UY", "598"}, {"UZ", "998"}, {"VU", "678"}, {"VE", "58"},
                {"VN", "84"}, {"VG", "1340"}, {"VI", "1284"}, {"YE", "967"}, {"ZM", "260"}, {"ZW", "263"}
        };

        var areaCodes = new ArrayList<>();
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        for (String[] info : countryCodes) {
            LinkedHashMap<String, String> areaMap = new LinkedHashMap<>();
            areaMap.put("country_code", info[0]);
            areaMap.put("area_code", info[1]);
            areaCodes.add(areaMap);
        }

        data.put("area_code", areaCodes);
        return ResponseEntity.ok(this.makeResponse(Retcode.RETCODE_SUCC, "OK", data));
    }
}

/// TODO Implement: https://gameapi-account.mihoyo.com/account/auth/api/bindRealperson
/// TODO Implement: https://gameapi-account.mihoyo.com/account/auth/api/confirmRealperson
/// TODO Implement: https://gameapi-account.mihoyo.com/account/auth/api/webLoginByPassword
/// TODO Implement: https://gameapi-account.mihoyo.com/account/auth/api/getActionTicketByCookieToken?action_type=game_role
/// TODO Implement: https://gameapi-account.mihoyo.com/account/auth/api/getUserAccountInfoByLToken