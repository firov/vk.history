package org.therg.vk.history;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.util.ArrayList;
import java.util.List;

public class Arguments {
    @Option(name = "-wp", aliases = "--without-photos", usage = "Do not download photos")
    private boolean withoutPhotos = false;

    @Option(name = "-wc", aliases = "--without-chats", usage = "Do not download chats")
    private boolean withoutChats = false;

    @Option(name = "-wd", aliases = "--without-dialogs", usage = "Do not download dialogs")
    private boolean withoutDialogs = false;

    @Option(name = "-f", aliases = "--format", required = false, usage = "output format (txt, html)")
    private String format = "html";

    @Option(name = "-t", aliases = "--token", required = true, usage = "vk API access token")
    private String token;

    @Argument
    private List<String> arguments = new ArrayList<String>();

    public void parse(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
        }
    }

    public String getToken() {
        return token;
    }

    public String getFormat() {
        return format;
    }

    public boolean isWithoutPhotos() {
        return withoutPhotos;
    }

    public boolean isWithoutDialogs() {
        return withoutDialogs;
    }

    public boolean isWithoutChats() {
        return withoutChats;
    }
}
