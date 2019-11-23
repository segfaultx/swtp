package de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Model;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ModelNotFoundException extends NotFoundException {

    Model model;

    public ModelNotFoundException(Model model) {
        super(String.format("Could not find %s.", model.toString()));
    }

}
