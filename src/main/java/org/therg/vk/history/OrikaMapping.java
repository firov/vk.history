package org.therg.vk.history;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import org.therg.vk.history.api.messages.Message;
import org.therg.vk.history.api.users.UserInfo;
import org.therg.vk.history.model.DialogMessage;
import org.therg.vk.history.model.User;

import java.util.Date;

/**
 * Classes map
 */
public class OrikaMapping {
    public static void map(MapperFactory mapper) {
        mapper.classMap(UserInfo.class, User.class)
                .mapNulls(false)
                .byDefault()
                .customize(new CustomMapper<UserInfo, User>() {
                    @Override
                    public void mapAtoB(UserInfo userInfo, User user, MappingContext context) {
                        user.displayName = String.format("%s %s", user.firstName, user.lastName);
                    }
                })
                .register();

        mapper.classMap(Message.class, DialogMessage.class)
                .field("body", "text")
                .customize(new CustomMapper<Message, DialogMessage>() {
                    @Override
                    public void mapAtoB(Message message, DialogMessage dialogMessage, MappingContext context) {
                        dialogMessage.timestamp = new Date((long) message.date * 1000);
                    }
                })
                .register();
    }
}
