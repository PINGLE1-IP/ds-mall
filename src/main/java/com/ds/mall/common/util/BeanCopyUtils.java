package com.ds.mall.common.util;

import org.springframework.beans.BeanUtils;

import java.util.List;

public final class BeanCopyUtils {

    private BeanCopyUtils() {
    }

    public static <T> T copy(Object source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException("对象复制失败", ex);
        }
    }

    public static <T> List<T> copyList(List<?> sourceList, Class<T> targetClass) {
        return sourceList.stream().map(source -> copy(source, targetClass)).toList();
    }
}
