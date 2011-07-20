package checks;

import java.util.UUID;

import net.sf.oval.Validator;
import net.sf.oval.configuration.annotation.AbstractAnnotationCheck;
import net.sf.oval.context.OValContext;
import net.sf.oval.exception.OValException;
import annotations.Uuid;

public class UuidCheck extends AbstractAnnotationCheck<Uuid> {
 
    @Override
    public boolean isSatisfied(Object validatedObject, Object value, 
    		OValContext context, Validator validator)
    throws OValException {
 
        try {
            UUID.fromString(value.toString());
            return true;
        } catch (IllegalArgumentException e) {}

        return false;
    }
}
