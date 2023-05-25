package com.mms.util;

@FunctionalInterface
public abstract interface TreeConsumer<T, U, V> {
	public abstract void accept(T t, U u, V v);
}
