package org.therg.vk.history.model;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Dialog {
    public final long id;

    public Dialog(long id) {
        this.messages = new ArrayList<>();
        this.id = id;
    }

    public User owner;
    public Collection<DialogMessage> messages;
}
