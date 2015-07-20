package org.therg.vk.history.api.messages;

import com.google.gson.annotations.SerializedName;
import org.therg.vk.history.api.ApiResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GetChatResult extends ApiResult {
    public GetChatResult() {
        this.result = new ArrayList<>();
    }

    public class ChatEntry {
        @SerializedName("id")
        public long id;

        @SerializedName("type")
        public String type;

        @SerializedName("title")
        public String title;

        @SerializedName("admin_id")
        public Long adminId;

        @SerializedName("users")
        public Collection<Long> users;
    }

    @SerializedName("response")
    public List<ChatEntry> result;
}
