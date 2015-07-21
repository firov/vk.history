package org.therg.vk.history.appenders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.therg.vk.history.model.ApplicationException;
import org.therg.vk.history.infrastructure.FileUtils;
import org.therg.vk.history.IHistoryAppender;
import org.therg.vk.history.model.ChatDialog;
import org.therg.vk.history.model.Dialog;
import org.therg.vk.history.model.UserDialog;

import java.io.*;

public abstract class BaseAppender implements IHistoryAppender {
    private static Logger logger = LogManager.getLogger("vk.history");
    private final String basePath;
    private final String extension;

    public BaseAppender(String basePath, String extension) {
        this.basePath = basePath;
        this.extension = extension;
    }

    public void initialize() throws ApplicationException {
        File dialogsPath = new File(basePath + "dialogs");
        File chatsPath = new File(basePath + "chats");

        if (!dialogsPath.exists() && !dialogsPath.mkdirs())
            throw new ApplicationException("failed to create output directory");

        if (!chatsPath.exists() && !chatsPath.mkdirs())
            throw new ApplicationException("failed to create output directory");
    }

    protected abstract void proccessDialog(Dialog dialog, Writer outputFile, String outputPath) throws IOException;

    protected void proccessDialogInternal(Dialog dialog, String path, String filename) {
        Writer file;
        File dialogDirectory = new File(path);
        if (!dialogDirectory.exists())
            if (!dialogDirectory.mkdirs()) {
                logger.error("failed to create dialog's directory");
                return;
            }

        File imagesDirectory = new File(path, "images");
        if (!imagesDirectory.exists())
            if (!imagesDirectory.mkdirs()) {
                logger.error("failed to create dialog's image directory");
                return;
            }

        try {
            file = new BufferedWriter(new FileWriter(new File(path, filename), false));

            try {
                proccessDialog(dialog, file, path);
            } finally {
                file.close();
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void proccessDialog(UserDialog dialog) {
        String title = FileUtils.escapeFilename(dialog.partner.displayName);
        proccessDialogInternal(dialog, basePath + "dialogs/" + title + "/", title + extension);

    }

    public void proccessDialog(ChatDialog dialog) {
        String title = FileUtils.escapeFilename(dialog.title);
        proccessDialogInternal(dialog, basePath + "chats/" + title + "/", title + extension);
    }
}
