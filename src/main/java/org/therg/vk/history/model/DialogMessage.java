package org.therg.vk.history.model;

import java.util.Collection;
import java.util.Date;

public class DialogMessage {
    public User from;
    public Date timestamp;
    public String text;
    public Collection<PhotoInfo> photos;
}
