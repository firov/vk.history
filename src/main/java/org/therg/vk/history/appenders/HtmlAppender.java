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

public class HtmlAppender implements IHistoryAppender {
    private static final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private static Logger logger = LogManager.getLogger("vk.history");
    private String htmlHeader;
    private static String basePath = "history/html/";

    public HtmlAppender() {
        htmlHeader = convertStreamToString(HtmlAppender.class.getResourceAsStream("/html_header.html"));
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private void proccessDialog(Dialog dialog, Writer file) {
        try {
            file.write(htmlHeader);
            file.write("<body>\n");
            for (DialogMessage message : dialog.messages) {
                if (message.photos == null) {
                    String text = message.text.isEmpty() ? "&nbsp;" : message.text;
                    file.write(String.format("<div>\n<div>%s</div>\n<div>%s</div>\n<div>%s</div>\n</div>",
                            message.from.displayName, dateFormat.format(message.timestamp), text));
                } else {
                    file.write(String.format("<div>\n<div>%s</div>\n<div>%s</div>\n<div>\n",
                            message.from.displayName, dateFormat.format(message.timestamp)));

                    for (PhotoInfo photo : message.photos) {
                        String relativePath = "images/" + String.valueOf(photo.id) + ".jpg";
                        photo.path = basePath + relativePath;
                        file.write(String.format("<img src=\"../%s\" />\n", relativePath));
                    }

                    file.write("</div></div>");
                }
            }
            file.write("</body>\n");

            file.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void proccessDialog(UserDialog dialog) {
        Writer file;
        try {
            String title = FileUtils.escapeFilename(dialog.partner.displayName);
            file = new BufferedWriter(new FileWriter(basePath + "dialogs/" + title + ".html", false));

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
            file = new BufferedWriter(new FileWriter(basePath + "chats/" + title + ".html", false));
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
