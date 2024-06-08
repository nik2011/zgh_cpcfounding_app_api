package com.yitu.cpcFounding.api.utils;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * BeanCopyUtil
 * @author yaoyanhua
 * @version 1.0
 * @date 2020/6/11
 */
public final class BeanCopyUtil<S, T> {
    private final S source;
    private final T target;

    private BeanCopyUtil(S source, T target) {
        this.source = source;
        this.target = target;
    }

    /**
     * toFunction
     * @param targetClass 目标类
     * @param action action
     * @return java.util.function.Function<S,T>
     * @author yaoyanhua
     * @date 2020/6/23 18:39
     */
    public static <S, T> Function<S, T> toFunction(Class<T> targetClass, BiConsumer<? super S, ? super T> action) {
        Objects.requireNonNull(targetClass, "targetClass is null");
        return (s) -> ofNullable(s, targetClass).copy(action).get();
    }

    /**
     * of
     * @param source
     * @param target
     * @return com.yitu.year.utils.BeanCopyUtil<S,T>
     * @author yaoyanhua
     * @date 2020/6/23 18:39
     */
    public static <S, T> BeanCopyUtil<S, T> of(S source, T target) {
        Objects.requireNonNull(source, "source is null");
        Objects.requireNonNull(target, "target is null");
        return new BeanCopyUtil<>(source, target);
    }

    /**
     * ofNullable
     * @param source 来源
     * @param targetClass 目标类
     * @return com.yitu.year.utils.BeanCopyUtil<S,T>
     * @author yaoyanhua
     * @date 2020/6/23 18:39
     */
    public static <S, T> BeanCopyUtil<S, T> ofNullable(S source, Class<T> targetClass) {
        Objects.requireNonNull(targetClass, "targetClass is null");
        return Optional.ofNullable(source).map(s -> {
            try {
                return of(source, targetClass.newInstance());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).orElse(new BeanCopyUtil<>(null, null));
    }

    /**
     * copy
     * @param action action
     * @return com.yitu.year.utils.BeanCopyUtil<S,T>
     * @author yaoyanhua
     * @date 2020/6/23 18:40
     */
    public final BeanCopyUtil<S, T> copy(BiConsumer<? super S, ? super T> action) {
        Objects.requireNonNull(action, "action is null");
        Optional.ofNullable(this.source).ifPresent((s) -> action.accept(s, this.target));
        return this;
    }

    /**
     * set
     * @param action action
     * @return com.yitu.year.utils.BeanCopyUtil<S,T>
     * @author yaoyanhua
     * @date 2020/6/23 18:40
     */
    public final BeanCopyUtil<S, T> set(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        Optional.ofNullable(this.source).ifPresent((s) -> action.accept(this.target));
        return this;
    }

    /**
     * get
     * @param
     * @return T
     * @author yaoyanhua
     * @date 2020/6/23 18:40
     */
    public final T get() {
        return Optional.ofNullable(this.source).map((s) -> this.target).orElse(null);
    }
}
