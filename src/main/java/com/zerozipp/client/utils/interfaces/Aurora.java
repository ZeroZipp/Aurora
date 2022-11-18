package com.zerozipp.client.utils.interfaces;

import com.zerozipp.client.utils.types.Type;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public @interface Aurora {
    Type[] value() default {};
}