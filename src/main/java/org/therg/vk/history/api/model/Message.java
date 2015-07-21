package org.therg.vk.history.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.Collection;

public class Message {
    @SerializedName("id")
    public long id;

    @SerializedName("user_id")
    public long userId;

    @SerializedName("from_id")
    public long fromId;

    @SerializedName("date")
    public int date;

    @SerializedName("read_state")
    public int readState;

    @SerializedName("title")
    public String title;

    @SerializedName("body")
    public String body;

    @SerializedName("deleted")
    public int deleted;

    @SerializedName("important")
    public int important;

    @SerializedName("emoji")
    public int emoji;

    @SerializedName("attachments")
    public Collection<Attachment> attachments;

    // chat properties

    @SerializedName("action")
    public String action;

    @SerializedName("action_text")
    public String actionText;

    @SerializedName("action_mid")
    public String actionMid;

    @SerializedName("chat_id")
    public long chatId;

    @SerializedName("users_count")
    public long usersCount;

    @SerializedName("admin_id")
    public long adminId;
}
