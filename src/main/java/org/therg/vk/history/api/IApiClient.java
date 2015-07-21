package org.therg.vk.history.api;

import org.therg.vk.history.api.messages.GetChatResult;
import org.therg.vk.history.api.messages.GetDialogsResult;
import org.therg.vk.history.api.messages.GetHistoryResult;
import org.therg.vk.history.api.users.UserInfoResult;

import java.io.OutputStream;
import java.util.Collection;

public interface IApiClient {
    public UserInfoResult getUserInfo(Collection<Long> ids);

    public GetDialogsResult getDialogs(long offset, int count, int previewLength, int unread);

    public GetHistoryResult getMessageHistory(long offset, int count, Long userId, Long chatId, Long startId, int order);

    public GetChatResult getChat(Collection<Long> ids);

    public void downloadTarget(String url, OutputStream outputStream);
}
