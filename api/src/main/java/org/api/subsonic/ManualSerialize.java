package org.api.subsonic;


import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ManualSerialize {
    String value() default "";
}
