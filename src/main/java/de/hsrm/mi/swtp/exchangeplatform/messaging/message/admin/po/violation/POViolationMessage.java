package de.hsrm.mi.swtp.exchangeplatform.messaging.message.admin.po.violation;

import de.hsrm.mi.swtp.exchangeplatform.messaging.message.Message;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;

/**
 * A marker interface for {@link Message messages} which are to be sent to a {@link User student} affected by PO restriction changes.
 */
public abstract class POViolationMessage extends Message {}
