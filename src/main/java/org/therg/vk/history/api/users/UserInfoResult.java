package org.therg.vk.history.api.users;

import com.google.gson.annotations.SerializedName;
import org.therg.vk.history.api.ApiResult;

import java.util.Collection;
import java.util.List;

public class UserInfoResult extends ApiResult {
    @SerializedName("response")
    public List<UserInfo> result;
}
