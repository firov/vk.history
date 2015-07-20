package org.therg.vk.history.api.users;

import com.google.gson.annotations.SerializedName;

public class UserInfo {
    @SerializedName("id")
    public Long id;

    @SerializedName("first_name")
    public String firstName;

    @SerializedName("last_name")
    public String lastName;
}
