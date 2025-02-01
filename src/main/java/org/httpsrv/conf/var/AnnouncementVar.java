package org.httpsrv.conf.var;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class AnnouncementVar {
    public List<AnnouncementContentObject> announcementContent = new ArrayList<>();
    public List<AnnouncementPicObject> announcementPics = new ArrayList<>();
    public List<AnnouncementObject> announcementFullObjects = new ArrayList<>();
    public List<AnnouncementPicFullObject> announcementPicFullObjects = new ArrayList<>();
    public List<LinkedHashMap<String, Object>> announcementTypeList = new ArrayList<>();
    public List<LinkedHashMap<String, Object>> announcementPicsTypeList = new ArrayList<>();

    public static class AnnouncementContentObject {
        public Integer ann_id;
        public String title;
        public String subtitle;
        public String banner;
        public String content;
        public String lang;
    }

    public static class AnnouncementPicObject {

    }

    public static class AnnouncementFullObject {
        public Integer ann_id;
        public String title;
        public String subtitle;
        public String banner;
        public String content;
        public String type_label;
        public String tag_label;
        public String tag_icon;
        public Integer login_alert;
        public String lang;
        public String start_time;
        public String end_time;
        public Integer type;
        public Integer remind;
        public Integer alert;
        public String tag_start_time;
        public String tag_end_time;
        public Integer remind_ver;
        public Boolean has_content;
        public Integer extra_remind;
        public String tag_icon_hover;
    }

    public static class AnnouncementPicFullObject {

    }

    public static class AnnouncementObject {
        public List<AnnouncementFullObject> list = new ArrayList<>();
        public Integer type_id;
        public String type_label;
    }
}