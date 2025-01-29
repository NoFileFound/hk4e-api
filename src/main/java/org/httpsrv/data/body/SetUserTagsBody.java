package org.httpsrv.data.body;

import java.util.List;
import lombok.Getter;

@Getter
public class SetUserTagsBody {
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