package com.teamagam.gimelgimel.helpers_autodesk.control.database.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DbBinder {

	String dbName() default "";
    boolean isNullable() default true;
}
