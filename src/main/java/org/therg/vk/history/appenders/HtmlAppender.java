package org.therg.vk.history.appenders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.therg.vk.history.ApplicationException;
import org.therg.vk.history.FileUtils;
import org.therg.vk.history.IHistoryAppender;
import org.therg.vk.history.model.ChatDialog;
import org.therg.vk.history.model.Dialog;
import org.therg.vk.history.model.DialogMessage;
import org.therg.vk.history.model.UserDialog;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class HtmlAppender implements IHistoryAppender {
    private static final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private static Logger logger = LogManager.getLogger("vk.history");
    private String htmlHeader;

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
                String text = message.text.isEmpty() ? "&nbsp;" : message.text;
                file.write(String.format("<div>\n<div>%s</div>\n<div>%s</div>\n<div>%s</div>\n</div>",
                        message.from.displayName, dateFormat.format(message.timestamp), text));
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
            file = new BufferedWriter(new FileWriter("history/html/dialogs/" + title + ".html", false));

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
            file = new BufferedWriter(new FileWriter("history/html/chats/" + title + ".html", false));
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
        File dialogsPath = new File("history/html/dialogs");
        File chatsPath = new File("history/html/chats");

        if (!dialogsPath.exists() && !dialogsPath.mkdirs())
            throw new ApplicationException("failed to create output directory");

        if (!chatsPath.exists() && !chatsPath.mkdirs())
            throw new ApplicationException("failed to create output directory");
    }
}
