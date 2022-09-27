package sit.int221.integratedprojectbe.exceptions;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StringEnumerationValidator implements ConstraintValidator<Enummismatch, String> {

    private Set<String> AVAILABLE_ENUM_NAMES;

    public static Set<String> getNamesSet(Class<? extends Enum<?>> e) {
        Enum<?>[] enums = e.getEnumConstants();
        String[] names = new String[enums.length];
        for (int i = 0; i < enums.length; i++) {
            names[i] = enums[i].name();
        }
        return new HashSet<String>(Arrays.asList(names));
    }

    @Override
    public void initialize(Enummismatch stringEnumeration) {
        Class<? extends Enum<?>> enumSelected = stringEnumeration.enumClass();
        AVAILABLE_ENUM_NAMES = getNamesSet(enumSelected);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if ( value == null || value.equals("")) {
            return true;
        } else {
            return AVAILABLE_ENUM_NAMES.contains(value);
        }
    }

}
