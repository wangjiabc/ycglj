package com.ycglj.manage.daoSQL.annotations;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PrimaryKey {
    Constraints constraints() default @Constraints(primarykey = true);
}
