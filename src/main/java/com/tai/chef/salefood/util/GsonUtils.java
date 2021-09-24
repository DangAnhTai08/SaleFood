package com.tai.chef.salefood.util;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class GsonUtils {

    public static String toJson(Object object) {
        if (Objects.isNull(object))
            return StringUtils.EMPTY;
        return new Gson().toJson(object);
    }
}
