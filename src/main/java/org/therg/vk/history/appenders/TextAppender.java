package org.therg.vk.history.appenders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.therg.vk.history.ApplicationException;
import org.therg.vk.history.FileUtils;
import org.therg.vk.history.IHistoryAppender;
import org.therg.vk.history.model.*;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class TextAppender implements IHistoryAppender {
    private static final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private static Logger logger = LogManager.getLogger("vk.history");
    private static String basePath = "history/txt/";

    private void proccessDialog(Dialog dialog, Writer file) {
        try {
            for (DialogMessage message : dialog.messages) {
                if (message.photos == null) {
                    file.write(String.format("%s %s\n%s\n\n",
                            message.from.displayName, dateFormat.format(message.timestamp), message.text));
                } else {
                    file.write(String.format("%s %s\n", message.from.displayName, dateFormat.format(message.timestamp)));

                    for (PhotoInfo photo : message.photos) {
                        String relativePath = "images/" + String.valueOf(photo.id) + ".jpg";
                        photo.path = basePath + relativePath;
                        file.write(String.format("../%s\n", relativePath));
                    }

                    file.write("\n\n");
                }
            }

            file.close();

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void proccessDialog(UserDialog dialog) {
        Writer file;
        try {
            String title = FileUtils.escapeFilename(dialog.partner.displayName);
            file = new BufferedWriter(new FileWriter(basePath + "dialogs/" + title + ".txt", false));

            try {
                proccessDialog(dialog, file);
            } finally {
                file.close();
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void proccessDialog(ChatDialog dialog) {
        Writer file;
        try {
            String title = FileUtils.escapeFilename(dialog.title);
            file = new BufferedWriter(new FileWriter(basePath + "chats/" + title + ".txt", false));

            try {
                proccessDialog(dialog, file);
            } finally {
                file.close();
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void initialize() throws ApplicationException {
        File dialogsPath = new File(basePath + "dialogs");
        File chatsPath = new File(basePath + "chats");
        File imagesPath = new File(basePath + "images");

        if (!dialogsPath.exists() && !dialogsPath.mkdirs())
            throw new ApplicationException("failed to create output directory");

        if (!chatsPath.exists() && !chatsPath.mkdirs())
            throw new ApplicationException("failed to create output directory");

        if (!imagesPath.exists() && !imagesPath.mkdirs())
            throw new ApplicationException("failed to create output directory");
    }
}
