package de.hsrm.mi.swtp.exchangeplatform.messaging;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Model;

public interface MessageSender<T extends Model> {

    void send();

    void send(T model);

}
