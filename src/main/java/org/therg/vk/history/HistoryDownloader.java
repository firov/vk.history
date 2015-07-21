package org.therg.vk.history;

import ma.glasnost.orika.MapperFacade;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.therg.vk.history.api.IApiClient;
import org.therg.vk.history.api.LimitedApiClient;
import org.therg.vk.history.api.messages.GetChatResult;
import org.therg.vk.history.api.users.UserInfoResult;
import org.therg.vk.history.appenders.HtmlAppender;
import org.therg.vk.history.appenders.TextAppender;
import org.therg.vk.history.infrastructure.ApiExtensions;
import org.therg.vk.history.infrastructure.UserService;
import org.therg.vk.history.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class HistoryDownloader {
    private static Logger logger = LogManager.getLogger("vk.history");

    final IApiClient apiClient;
    final ApiExtensions apiExtensions;
    final MapperFacade mapper;
    final Arguments arguments;

    public HistoryDownloader(Arguments arguments, MapperFacade mapper) {
        this.arguments = arguments;
        this.mapper = mapper;
        this.apiClient = new LimitedApiClient(arguments.getToken());
        this.apiExtensions = new ApiExtensions(this.apiClient, mapper);
    }

    public boolean execute() throws ApplicationException {
        IHistoryAppender appender;
        if (arguments.getFormat().equals("html"))
            appender = new HtmlAppender();
        else
            appender = new TextAppender();

        appender.initialize();

        UserInfoResult response = apiClient.getUserInfo(null);
        if (response.errorCode != 0) {
            logger.error("failed to load user info");
            logger.error(response.errorMessage);
            return false;
        }

        User currentUser = mapper.map(response.result.get(0), User.class);

        logger.info(String.format("Loading history for user %d", currentUser.id));

        Collection<DialogInfo> dialogs = this.apiExtensions.loadDialogs();

        logger.info(String.format("Total number of dialogs to load: %s", dialogs.size()));

        Set<Long> participants = new HashSet<>();
        participants.add(currentUser.id);
        participants.addAll(dialogs.stream().filter(x -> !x.isChat).map(x -> x.id).collect(Collectors.toList()));

        logger.debug("Getting chats info");
        Map<Long, String> chatsTitles = new HashMap<>();
        GetChatResult chats = apiClient.getChat(dialogs.stream()
                .filter(x -> x.isChat)
                .map(x -> x.id)
                .collect(Collectors.toList()));

        for (GetChatResult.ChatEntry chat : chats.result) {
            participants.addAll(chat.users.stream().collect(Collectors.toList()));
            if (chat.title != null)
                chatsTitles.put(chat.id, chat.title);
            else
                chatsTitles.put(chat.id, String.valueOf(chat.id));
        }

        logger.info(String.format("Loading %d users profiles", participants.size()));

        UserService usersService = new UserService();
        usersService.setupUsers(this.apiExtensions.getUsers(participants));

        if (!arguments.isWithoutDialogs()) {
            logger.info("Loading dialogs");

            dialogs.stream().filter(x -> !x.isChat).map(x -> x.id).forEach(dialogId -> {
                UserDialog dialog = new UserDialog(dialogId);
                dialog.partner = usersService.getUser(dialogId);
                dialog.owner = currentUser;

                logger.info(String.format("Loading conversation with '%d'", dialog.partner.id));

                dialog.messages = this.apiExtensions.getDialogMessages(usersService, dialogId);

                appender.proccessDialog(dialog);

                if (!arguments.isWithoutPhotos()) {
                    logger.info("Loading photos");
                    downloadPhotos(dialog);
                }
            });
        }

        if (!arguments.isWithoutChats()) {
            logger.info("Loading chats");

            dialogs.stream().filter(x -> x.isChat).map(x -> x.id).forEach(chatId -> {
                ChatDialog dialog = new ChatDialog(chatId);
                dialog.owner = currentUser;
                dialog.title = chatsTitles.get(chatId);

                logger.info(String.format("Loading chat '%d'", dialog.id));

                dialog.messages = this.apiExtensions.getChatMessages(usersService, chatId);

                appender.proccessDialog(dialog);

                if (!arguments.isWithoutPhotos()) {
                    logger.info("Loading photos");
                    downloadPhotos(dialog);
                }
            });
        }

        return true;
    }

    private void downloadPhotos(Dialog dialog) {
        for (DialogMessage message : dialog.messages) {
            if (message.photos == null)
                continue;

            for (PhotoInfo photo : message.photos) {
                File outputFile = new File(photo.path);

                if (outputFile.exists())
                    continue;

                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(outputFile);
                    apiClient.downloadTarget(photo.url, outputStream);
                } catch (FileNotFoundException e) {
                    logger.error("error creating photo file", e);
                } finally {
                    try {
                        if (outputStream != null)
                            outputStream.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        }
    }
}
