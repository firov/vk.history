package org.therg.vk.history.model;

import java.util.Collection;

public class ChatDialog extends Dialog {
    public Collection<User> partners;
    public String title;

    public ChatDialog(long id) {
        super(id);
    }
}
