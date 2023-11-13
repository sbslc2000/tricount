package goorm.tricount.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.*;

@Target(ElementType.TYPE)
@Retention(RUNTIME)
public @interface HandlesTypes {
    Class<?>[] value();
}
