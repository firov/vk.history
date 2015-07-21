package org.therg.vk.history;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.therg.vk.history.model.ApplicationException;
import org.therg.vk.history.model.OrikaMapping;

public class Application {
    public static final String tokenUrl =
            "https://oauth.vk.com/authorize?client_id=4917578&scope=messages&redirect_uri=https://oauth.vk.com/blank.html&display=page&v=5.32&response_type=token";

    private static Logger logger = LogManager.getLogger("vk.history");

    public static void main(String[] args) {
        logger.info("Starting");

        Arguments arguments = new Arguments();
        try {
            arguments.parse(args);
        } catch (CmdLineException e) {
            logger.error(e.getMessage());
            System.exit(1);
        }

        if (arguments.getToken() == null || arguments.getToken().equals("")) {
            System.out.println("Please obtain access token from:");
            System.out.println(tokenUrl);
            System.out.println("Open this url in browser and, after confirming access for application, copy token from url");
            System.exit(1);
        }

        logger.info(String.format("Using '%s' format", arguments.getFormat()));

        if (arguments.isWithoutPhotos())
            logger.info("Photos will be skipped");

        if (arguments.isWithoutDialogs())
            logger.info("Dialogs will be skipped");

        if (arguments.isWithoutChats())
            logger.info("Chats will be skipped");

        MapperFactory factory = new DefaultMapperFactory.Builder().useAutoMapping(false).build();
        OrikaMapping.map(factory);

        HistoryDownloader downloader = new HistoryDownloader(arguments, factory.getMapperFacade());
        try {
            if (!downloader.execute())
                System.exit(1);
        } catch (ApplicationException e) {
            logger.error(e.getMessage());
            System.exit(1);
        }

        logger.info("done.");
        logger.info("\nHave a nice day.\n\n");
    }
}
