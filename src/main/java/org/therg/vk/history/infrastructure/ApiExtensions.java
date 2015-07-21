package org.therg.vk.history.infrastructure;

import ma.glasnost.orika.MapperFacade;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.therg.vk.history.api.IApiClient;
import org.therg.vk.history.api.messages.GetDialogsResult;
import org.therg.vk.history.api.messages.GetHistoryResult;
import org.therg.vk.history.api.model.Message;
import org.therg.vk.history.api.users.UserInfoResult;
import org.therg.vk.history.model.DialogInfo;
import org.therg.vk.history.model.DialogMessage;
import org.therg.vk.history.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Helpers for api usage
 */
public class ApiExtensions {
    private static final int batchSize = 200;
    private static Logger logger = LogManager.getLogger("vk.history");

    private final IApiClient client;
    private final MapperFacade mapper;

    public ApiExtensions(IApiClient client, MapperFacade mapper) {
        this.client = client;
        this.mapper = mapper;
    }

    /**
     * Loads all dialog's messages
     *
     * @param usersService   user service
     * @param conversationId conversation identifier
     * @param isChat         true if dialog is a multi-user chat
     * @return Dialog's messages
     */
    private Collection<DialogMessage> getConversationMessages(UserService usersService, Long conversationId, boolean isChat) {
        Collection<DialogMessage> messages = new ArrayList<>();
        long offset = 0;
        while (true) {
            GetHistoryResult apiResult = isChat ?
                    client.getMessageHistory(offset, batchSize, null, conversationId, null, 1) :
                    client.getMessageHistory(offset, batchSize, conversationId, null, null, 1);

            if (apiResult.errorCode != 0) {
                logger.error(String.format("failed to load conversation messages, error %d", apiResult.errorCode));
                return messages;
            }

            for (Message message : apiResult.items) {
                DialogMessage dialogMessage = mapper.map(message, DialogMessage.class);
                dialogMessage.from = usersService.getUser(message.fromId);

                messages.add(dialogMessage);
            }

            offset += batchSize;
            if (offset > apiResult.count)
                break;
        }

        return messages;
    }

    /**
     * Loads all dialog's messages
     *
     * @param usersService user service
     * @param dialogId     dialog identifier
     * @return
     */
    public Collection<DialogMessage> getDialogMessages(UserService usersService, Long dialogId) {
        return getConversationMessages(usersService, dialogId, false);
    }

    /**
     * Loads all chat's messages
     *
     * @param usersService user service
     * @param chatId       chat identifier
     * @return
     */
    public Collection<DialogMessage> getChatMessages(UserService usersService, Long chatId) {
        return getConversationMessages(usersService, chatId, true);
    }

    /**
     * Loads users info by ids
     *
     * @param ids users ids
     * @return users map
     */
    public Map<Long, User> getUsers(Collection<Long> ids) {
        UserInfoResult usersInfo = this.client.getUserInfo(ids);
        return usersInfo.result
                .stream()
                .collect(Collectors.toMap(x -> x.id, x -> mapper.map(x, User.class)));
    }

    /**
     * Loads current user's dialogs
     *
     * @return Dialogs
     */
    public Collection<DialogInfo> loadDialogs() {
        Collection<DialogInfo> result = new ArrayList<>();
        long offset = 0;

        while (true) {
            GetDialogsResult apiResult = this.client.getDialogs(offset, batchSize, 1, 0);

            for (GetDialogsResult.DialogEntry entry : apiResult.items) {
                DialogInfo dialogInfo = new DialogInfo();

                Message message = entry.message;

                if (message.chatId > 0) {
                    dialogInfo.id = message.chatId;
                    dialogInfo.isChat = true;
                } else
                    dialogInfo.id = message.userId;

                result.add(dialogInfo);
            }

            offset += batchSize;
            if (offset > apiResult.count)
                break;
        }

        return result;
    }
}
