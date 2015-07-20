package org.therg.vk.history.api;

import org.therg.vk.history.api.messages.GetChatResult;
import org.therg.vk.history.api.messages.GetDialogsResult;
import org.therg.vk.history.api.messages.GetHistoryResult;
import org.therg.vk.history.api.users.UserInfoResult;

import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

public class ApiClient extends ApiClientBase implements IApiClient {

    public ApiClient(String token) {
        super(token);
    }

    public UserInfoResult getUserInfo(Collection<Long> ids) {
        HashMap<String, Object> args = new HashMap<>();
        if (ids != null)
            args.put("user_ids", ids.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(",")));

        return this.executeGet(UserInfoResult.class, true,
                "users.get", args);
    }

    public GetDialogsResult getDialogs(long offset, int count, int previewLength, int unread) {
        HashMap<String, Object> args = new HashMap<>();
        args.put("offset", offset);
        args.put("count", count);
        args.put("preview_length", previewLength);
        args.put("unread", unread);

        return this.executeGet(GetDialogsResult.class, false, "messages.getDialogs", args);
    }

    public GetHistoryResult getMessageHistory(long offset,
                                              int count,
                                              Long userId,
                                              Long chatId,
                                              Long startId,
                                              int order) {

        HashMap<String, Object> args = new HashMap<>();
        args.put("offset", offset);
        args.put("count", count);
        args.put("user_id", userId);
        args.put("chat_id", chatId);
        args.put("start_message_id", startId);
        args.put("rev", order);

        return this.executeGet(GetHistoryResult.class, false, "messages.getHistory", args);
    }

    public GetChatResult getChat(Collection<Long> ids) {
        HashMap<String, Object> args = new HashMap<>();
        args.put("chat_ids", ids.stream()
                .map(Object::toString)
                .collect(Collectors.joining(",")));

        return this.executeGet(GetChatResult.class, true, "messages.getChat", args);
    }
}
