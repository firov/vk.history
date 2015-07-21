package org.therg.vk.history.api.messages;

import org.therg.vk.history.api.ApiResult;
import org.therg.vk.history.api.model.Message;

import java.util.List;

public class GetHistoryResult extends ApiResult {
    public int count;

    public List<Message> items;
}
