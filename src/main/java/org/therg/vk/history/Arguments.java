package org.therg.vk.history;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.util.ArrayList;
import java.util.List;

public class Arguments {
    enum Format {HTML, TXT}


    @Option(name = "-wp", aliases = "--without-photos", usage = "Do not download photos")
    private boolean withoutPhotos = false;

    @Option(name = "-wc", aliases = "--without-chats", usage = "Do not download chats")
    private boolean withoutChats = false;

    @Option(name = "-wd", aliases = "--without-dialogs", usage = "Do not download dialogs")
    private boolean withoutDialogs = false;

    @Option(name = "-f", aliases = "--format", required = false, usage = "output format (txt, html)")
    private Format format = Format.HTML;

    @Option(name = "-t", aliases = "--token", required = true, usage = "vk.com API access token")
    private String token;

    @Argument
    private List<String> arguments = new ArrayList<>();

    public void parse(String[] args) throws CmdLineException {
        CmdLineParser parser = new CmdLineParser(this);
        parser.parseArgument(args);
    }

    public String getToken() {
        return token;
    }

    public Format getFormat() {
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
