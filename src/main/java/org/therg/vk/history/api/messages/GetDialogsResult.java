package org.therg.vk.history.api.messages;

import org.therg.vk.history.api.ApiResult;
import org.therg.vk.history.api.model.Message;

import java.util.List;

public class GetDialogsResult extends ApiResult {
    public class DialogEntry {
        public int unread;
        public Message message;
    }

    public int count;
    public int unread_dialogs;

    public List<DialogEntry> items;
}
