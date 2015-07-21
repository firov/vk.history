package org.therg.vk.history.appenders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.therg.vk.history.IHistoryAppender;
import org.therg.vk.history.model.*;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class TextAppender extends BaseAppender implements IHistoryAppender {
    private static final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public TextAppender() {
        super("history/txt/", ".txt");
    }

    protected void proccessDialog(Dialog dialog, Writer outputFile, String filename, String outputPath) throws IOException {
        for (DialogMessage message : dialog.messages) {
            if (message.photos == null) {
                outputFile.write(String.format("%s %s\n%s\n\n",
                        message.from.displayName, dateFormat.format(message.timestamp), message.text));
            } else {
                outputFile.write(String.format("%s %s\n", message.from.displayName, dateFormat.format(message.timestamp)));

                for (PhotoInfo photo : message.photos) {
                    String relativePath = new File("images", String.valueOf(photo.id) + ".jpg").getPath();
                    photo.path = new File(outputPath, relativePath).getPath();
                    outputFile.write(String.format("%s\n", relativePath));
                }

                outputFile.write("\n\n");
            }
        }

        outputFile.close();

    }
}
