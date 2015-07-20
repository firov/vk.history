package org.therg.vk.history;

import org.therg.vk.history.model.ChatDialog;
import org.therg.vk.history.model.UserDialog;

/**
 * Message history output appender
 */
public interface IHistoryAppender {
    /**
     * Processes dialog with user
     *
     * @param dialog dialog to process
     */
    public void proccessDialog(UserDialog dialog);

    /**
     * Processes chat
     *
     * @param dialog dialog to process
     */
    public void proccessDialog(ChatDialog dialog);

    /**
     * Initializes appender
     */
    public void initialize() throws ApplicationException;
}
