package org.therg.vk.history.infrastructure;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.therg.vk.history.api.model.Attachment;
import org.therg.vk.history.api.model.Message;
import org.therg.vk.history.model.DialogMessage;
import org.therg.vk.history.model.PhotoInfo;

import java.util.ArrayList;
import java.util.Date;

public class MessageCustomConverter extends CustomMapper<Message, DialogMessage> {
    @Override
    public void mapAtoB(Message message, DialogMessage dialogMessage, MappingContext context) {
        dialogMessage.timestamp = new Date((long) message.date * 1000);
        if (message.action != null) {
            dialogMessage.text = message.action;
            if (message.actionMid != null)
                dialogMessage.text += " " + message.actionMid;

            if (message.actionText != null)
                dialogMessage.text += " " + message.actionText;
        }

        if (message.attachments != null) {
            StringBuilder text = new StringBuilder();
            for (Attachment attachment : message.attachments) {
                if (text.length() > 0)
                    text.append("\n");

                if (attachment.video != null)
                    text.append(attachment.video.title);
                else if (attachment.audio != null)
                    text.append(attachment.audio.artist).append(" - ").append(attachment.audio.title);
                else if (attachment.document != null)
                    text.append(attachment.document.title);
                else if (attachment.photo != null) {
                    if (dialogMessage.photos == null)
                        dialogMessage.photos = new ArrayList<>();
                    PhotoInfo photoInfo = new PhotoInfo(attachment.photo.id, attachment.photo.getBestUrl());
                    dialogMessage.photos.add(photoInfo);
                } else if (attachment.sticker != null) {
                    if (dialogMessage.photos == null)
                        dialogMessage.photos = new ArrayList<>();
                    PhotoInfo photoInfo = new PhotoInfo(attachment.sticker.id, attachment.sticker.getSmallestSizeUrl());
                    dialogMessage.photos.add(photoInfo);
                }
            }

            dialogMessage.text = text.toString();
        }
    }
}
