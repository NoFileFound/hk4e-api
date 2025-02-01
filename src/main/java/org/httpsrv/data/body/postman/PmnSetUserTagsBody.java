package org.httpsrv.data.body.postman;

import java.util.List;
import lombok.Getter;

@Getter
public class PmnSetUserTagsBody {
    @Getter
    public static class SetUserTagsSubBody {
        private String alias;
        private String region;
        private List<String> add_tags;
        private List<String> remove_tags;
    }

    private Integer app_id;
    private Integer ts;
    private List<SetUserTagsSubBody> user_tags;
    private String sign;
}