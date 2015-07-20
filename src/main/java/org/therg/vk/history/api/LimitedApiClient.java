package org.therg.vk.history.api;

import org.therg.vk.history.api.messages.GetChatResult;
import org.therg.vk.history.api.messages.GetDialogsResult;
import org.therg.vk.history.api.messages.GetHistoryResult;
import org.therg.vk.history.api.users.UserInfoResult;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of IApiClient that supports 3 requests per second api limitation
 */
public class LimitedApiClient implements IApiClient {
    private interface Func<T> {
        public T execute();
    }

    IApiClient client;
    int lastRequestSecond;
    int numberOfRequestsDone;

    public LimitedApiClient(String token) {
        client = new ApiClient(token);
    }

    private <T extends ApiResult> T executeApiRequest(Func<T> method) {
        LocalDateTime now = LocalDateTime.now();
        int currentSecond = now.getSecond();
        if (currentSecond == lastRequestSecond) {
            numberOfRequestsDone++;
            if (numberOfRequestsDone > 3) {
                try {
                    Thread.sleep(TimeUnit.SECONDS.toMillis(1));
                } catch (InterruptedException e) {
                    return null;
                }
            }
        } else {
            lastRequestSecond = currentSecond;
            numberOfRequestsDone = 1;
        }

        while (true) {
            T result = method.execute();
            if (result.errorCode == 6) {
                try {
                    Thread.sleep(TimeUnit.SECONDS.toMillis(2));
                    continue;
                } catch (InterruptedException e) {
                    return result;
                }
            }

            return result;
        }
    }

    @Override
    public UserInfoResult getUserInfo(Collection<Long> ids) {
        return executeApiRequest(() -> client.getUserInfo(ids));
    }

    @Override
    public GetDialogsResult getDialogs(long offset, int count, int previewLength, int unread) {
        return executeApiRequest(
                () -> client.getDialogs(offset, count, previewLength, unread));
    }

    @Override
    public GetHistoryResult getMessageHistory(long offset, int count, Long userId, Long chatId, Long startId, int order) {
        return executeApiRequest(
                () -> client.getMessageHistory(offset, count, userId, chatId, startId, order));
    }

    @Override
    public GetChatResult getChat(Collection<Long> ids) {
        return executeApiRequest(() -> client.getChat(ids));
    }
}
