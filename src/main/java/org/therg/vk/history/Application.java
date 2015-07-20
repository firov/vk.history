package org.therg.vk.history;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Application {
    public static final String tokenUrl =
            "https://oauth.vk.com/authorize?client_id=4917578&scope=messages&redirect_uri=https://oauth.vk.com/blank.html&display=page&v=5.32&response_type=token";

    private static Logger logger = LogManager.getLogger("vk.history");

    public static void main(String[] args) {
        logger.info("Starting");

        Arguments arguments = new Arguments();
        arguments.parse(args);

        if (arguments.getToken() == null || arguments.getToken().equals("")) {
            System.out.println("Please obtain access token from:");
            System.out.println(tokenUrl);
            System.out.println("Open this url in browser and, after confirming access for application, copy token from url");
            return;
        }

        MapperFactory factory = new DefaultMapperFactory.Builder().useAutoMapping(false).build();
        OrikaMapping.map(factory);

        HistoryDownloader downloader = new HistoryDownloader(arguments, factory.getMapperFacade());
        try {
            downloader.execute();
        } catch (ApplicationException e) {
            logger.error(e.getMessage());
            return;
        }

        logger.info("done.");
        logger.info("\nHave a nice day.\n\n");

    }
}