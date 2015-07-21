package org.therg.vk.history.appenders;

import org.therg.vk.history.IHistoryAppender;
import org.therg.vk.history.model.ApplicationException;
import org.therg.vk.history.model.Dialog;
import org.therg.vk.history.model.DialogMessage;
import org.therg.vk.history.model.PhotoInfo;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class HtmlAppender extends BaseAppender implements IHistoryAppender {
    private static final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private String htmlHeader;

    public HtmlAppender() {
        super("history/html/", ".html");
        this.htmlHeader = convertStreamToString(HtmlAppender.class.getResourceAsStream("/html_header.html"));
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    protected void proccessDialog(Dialog dialog, Writer outputFile, String filename, String outputPath) throws IOException {
        outputFile.write(htmlHeader.replace("__TITLE__", filename));
        outputFile.write("<body>\n");
        for (DialogMessage message : dialog.messages) {
            if (message.photos == null) {
                String text = message.text.isEmpty() ? "&nbsp;" : message.text;
                outputFile.write(String.format("<div>\n<div>%s</div>\n<div>%s</div>\n<div>%s</div>\n</div>",
                        message.from.displayName, dateFormat.format(message.timestamp), text));
            } else {
                outputFile.write(String.format("<div>\n<div>%s</div>\n<div>%s</div>\n<div>\n",
                        message.from.displayName, dateFormat.format(message.timestamp)));

                for (PhotoInfo photo : message.photos) {
                    String relativePath = new File("images", String.valueOf(photo.id) + ".jpg").getPath();
                    photo.path = new File(outputPath, relativePath).getPath();
                    outputFile.write(String.format("<img src=\"%s\" />\n", relativePath));
                }

                outputFile.write("</div></div>");
            }
        }
        outputFile.write("</body>\n");

        outputFile.close();
    }

    @Override
    public void initialize() throws ApplicationException {
        super.initialize();
    }
}
