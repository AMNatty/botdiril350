package cz.tefek.botdiril.framework.command;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import cz.tefek.botdiril.framework.permission.EnumPowerLevel;


@Retention(RUNTIME)
@Target(TYPE)
public @interface Command {
    String[] aliases() default {};

    CommandCategory category() default CommandCategory.GENERAL;

    String description();

    int levelLock() default 0;

    EnumPowerLevel powerLevel() default EnumPowerLevel.EVERYONE;

    EnumSpecialCommandProperty[] special() default {};

    String value();
}
