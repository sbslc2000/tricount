package goorm.tricount.common;

import org.springframework.stereotype.Component;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.*;

@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RUNTIME)
@Component
public @interface RepresentModel {
    Class<?> value();
}
