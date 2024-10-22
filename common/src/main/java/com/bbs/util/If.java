package com.bbs.util;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.nonNull;

public class If {

    private Supplier<Boolean> conditionCallback;

    private Runnable successCallback;

    private Runnable failureCallback;

    private Consumer<Exception> errorCallback;

    public If(Supplier<Boolean> conditionCallback) {
        this.conditionCallback = conditionCallback;
    }

    public If success(Runnable callback) {
        this.successCallback = callback;
        return this;
    }

    public If failure(Runnable callback) {
        this.failureCallback = callback;
        return this;
    }

    public If error(Consumer<Exception> callback) {
        this.errorCallback = callback;
        return this;
    }

    public void exec() {
        try {
            if(conditionCallback.get()) {
                successCallback.run();
            } else {
                failureCallback.run();
            }
        } catch (Exception e) {
            if(nonNull(errorCallback)) errorCallback.accept(e);
        }
    }

    /**
     * 数据准备与结果获取
     * @param <T> 准备的数据
     * @param <R> 结果
     */
    public static class PreparedAndGet<T, R> {

        private final Supplier<T> preparedFunction;

        private Function<T, Boolean> conditionCallback;

        private Function<T, R> successCallback;

        private Function<T, R> failureCallback;

        private Function<Exception, R> errorCallback;

        public PreparedAndGet(Supplier<T> preparedFunction, Function<T, Boolean> conditionCallback) {
            this.preparedFunction = preparedFunction;
            this.conditionCallback = conditionCallback;
        }

        public PreparedAndGet<T, R> success(Function<T, R> callback) {
            this.successCallback = callback;
            return this;
        }

        public PreparedAndGet<T, R> failure(Function<T, R> callback) {
            this.failureCallback = callback;
            return this;
        }

        public PreparedAndGet<T, R> error(Function<Exception, R> callback) {
            this.errorCallback = callback;
            return this;
        }

        public R exec() {
            try {
                T params = preparedFunction.get();
                return conditionCallback.apply(params) ? successCallback.apply(params) : failureCallback.apply(params);
            } catch (Exception e) {
                return nonNull(errorCallback) ? errorCallback.apply(e) : null;
            }
        }
    }

    /**
     * 数据准备
     * @param <T> 准备的数据
     */
    public static class Prepared<T> {

        private final Supplier<T> preparedFunction;

        private Function<T, Boolean> conditionCallback;

        private Consumer<T> successCallback;

        private Consumer<T> failureCallback;

        private Consumer<Exception> errorCallback;

        public Prepared(Supplier<T> preparedFunction, Function<T, Boolean> conditionCallback) {
            this.preparedFunction = preparedFunction;
            this.conditionCallback = conditionCallback;
        }

        public Prepared<T> success(Consumer<T> callback) {
            this.successCallback = callback;
            return this;
        }

        public Prepared<T> failure(Consumer<T> callback) {
            this.failureCallback = callback;
            return this;
        }

        public Prepared<T> error(Consumer<Exception> callback) {
            this.errorCallback = callback;
            return this;
        }

        public void exec() {
            try {
                T params = preparedFunction.get();
                if(conditionCallback.apply(params)) {
                    successCallback.accept(params);
                } else {
                    failureCallback.accept(params);
                }
            } catch (Exception e) {
                if(nonNull(errorCallback)) errorCallback.accept(e);
            }
        }
    }
}
