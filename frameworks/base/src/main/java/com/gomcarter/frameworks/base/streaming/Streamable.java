package com.gomcarter.frameworks.base.streaming;

import com.google.common.collect.Lists;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * @author gomcarter
 */
public class Streamable<T> {

    private Stream<T> stream;

    public static <T> Streamable<T> valueOf(Collection<T> collection) {
        return new Streamable<>(collection);
    }

    public static <T> Streamable<T> valueOf(T... array) {
        return new Streamable<>(Arrays.stream(array));
    }

    public static <T> Streamable<T> valueOf(Stream<T> other) {
        return new Streamable<>(other);
    }

    private Streamable(Collection<T> collection) {
        this(collection == null ? Stream.empty() : collection.stream());
    }

    private Streamable(Stream<T> stream) {
        if (stream == null) {
            stream = Stream.empty();
        }
        this.stream = stream;
    }

    public Stream<T> get() {
        return stream;
    }

    /**
     * @param keyMapper   keyMapper
     * @param valueMapper valueMapper
     * @param <KEY>       :  map的key
     * @param <VAL>       ： 新的值 Collection&lt;VAL&gt;
     * @return Groupable
     */
    public <KEY, VAL> Groupable<KEY, List<VAL>> groupby(Function<T, KEY> keyMapper, Function<T, VAL> valueMapper) {
        return Groupable.of(this.stream.collect(
                Collectors.toMap(
                        keyMapper,
                        (T v) -> Lists.newArrayList(valueMapper.apply(v)),
                        (oldValue, newValue) -> {
                            oldValue.addAll(newValue);
                            return oldValue;
                        }))
        );
    }

    /**
     * @param keyMapper keyMapper
     * @param <KEY>     map的key
     * @return Groupable
     */
    public <KEY> Groupable<KEY, List<T>> groupby(Function<T, KEY> keyMapper) {
        return this.groupby(keyMapper, Function.identity());
    }

    /**
     * @param keyMapper   keyMapper
     * @param valueMapper valueMapper
     * @param <KEY>       map的key
     * @param <VAL>       新的值 VAL, 如果基于key重复了，则报错 java.lang.IllegalStateException
     * @return Groupable
     */
    public <KEY, VAL> Groupable<KEY, VAL> uniqueGroupby(Function<T, KEY> keyMapper, Function<T, VAL> valueMapper) {
        return Groupable.of(this.stream.collect(
                Collectors.toMap(
                        keyMapper,
                        valueMapper)
                )
        );
    }

    /**
     * @param keyMapper keyMapper
     * @param <KEY>     map的key
     * @return Groupable
     */
    public <KEY> Groupable<KEY, T> uniqueGroupby(Function<T, KEY> keyMapper) {
        return this.uniqueGroupby(keyMapper, Function.identity());
    }

    /**
     * @param keyMapper   keyMapper
     * @param valueMapper valueMapper
     * @param <KEY>       map的key
     * @param <VAL>       新的值 VAL, 如果基于key重复了，新的值覆盖老的值
     * @return Groupable
     */
    public <KEY, VAL> Groupable<KEY, VAL> uniqueGroupbySafely(Function<T, KEY> keyMapper, Function<T, VAL> valueMapper) {
        return Groupable.of(this.stream
                .collect(Collectors.toMap(
                        keyMapper,
                        valueMapper,
                        (oldValue, newValue) -> newValue
                )));
    }

    /**
     * @param keyMapper 新的值, 如果基于key重复了，新的值覆盖老的值
     * @param <KEY>     map的key
     * @return Groupable
     */
    public <KEY> Groupable<KEY, T> uniqueGroupbySafely(Function<T, KEY> keyMapper) {
        return this.uniqueGroupbySafely(keyMapper, Function.identity());
    }

    /**
     * @param predicate predicate
     * @return Streamable
     */
    public Streamable<T> filter(Predicate<T> predicate) {
        this.stream = this.stream.filter(predicate);
        return this;
    }

    /**
     * @param mapper mapper
     * @return IntStream
     */
    public IntStream mapToInt(ToIntFunction<T> mapper) {
        return this.stream.mapToInt(mapper);
    }

    /**
     * @param mapper mapper
     * @return LongStream
     */
    public LongStream mapToLong(ToLongFunction<T> mapper) {
        return this.stream.mapToLong(mapper);
    }

    /**
     * @param mapper mapper
     * @return DoubleStream
     */
    public DoubleStream mapToDouble(ToDoubleFunction<T> mapper) {
        return this.stream.mapToDouble(mapper);
    }

    /**
     * @return Streamable
     */
    public Streamable<T> distinct() {
        this.stream = this.stream.distinct();
        return this;
    }

    /**
     * @return Streamable
     */
    public Streamable<T> sorted() {
        this.stream = this.stream.sorted();
        return this;
    }

    /**
     * @param comparator comparator
     * @return Streamable
     */
    public Streamable<T> sorted(Comparator<? super T> comparator) {
        this.stream = this.stream.sorted(comparator);
        return this;
    }

    /**
     * @param action action
     * @return Streamable
     */
    public Streamable<T> peek(Consumer<? super T> action) {
        this.stream = this.stream.peek(action);
        return this;
    }

    /**
     * @param maxSize maxSize
     * @return Streamable
     */
    public Streamable<T> limit(long maxSize) {
        this.stream = this.stream.limit(maxSize);
        return this;
    }

    /**
     * @param n n
     * @return Streamable
     */
    public Streamable<T> skip(long n) {
        this.stream = this.stream.skip(n);
        return this;
    }

    /**
     * @param action action
     */
    public void forEach(Consumer<? super T> action) {
        this.stream.forEach(action);
    }

    /**
     * @param action action
     */
    public void forEachOrdered(Consumer<? super T> action) {
        this.stream.forEachOrdered(action);
    }

    /**
     * @return Object[]
     */
    public Object[] toArray() {
        return this.stream.toArray();
    }

    /**
     * @param comparator comparator
     * @return Optional
     */
    public Optional<T> min(Comparator<? super T> comparator) {
        return this.stream.min(comparator);
    }

    /**
     * @param comparator comparator
     * @return Optional
     */
    public Optional<T> max(Comparator<? super T> comparator) {
        return this.stream.max(comparator);
    }

    /**
     * @return counts
     */
    public long count() {
        return this.stream.count();
    }

    /**
     * @param predicate predicate
     * @return true if matched
     */
    public boolean anyMatch(Predicate<? super T> predicate) {
        return this.stream.anyMatch(predicate);
    }

    /**
     * @param predicate predicate
     * @return true if allMatched
     */
    public boolean allMatch(Predicate<? super T> predicate) {
        return this.stream.allMatch(predicate);
    }

    /**
     * @param predicate predicate
     * @return true if no one matched
     */
    public boolean noneMatch(Predicate<? super T> predicate) {
        return this.stream.noneMatch(predicate);
    }

    /**
     * @return Optional
     */
    public Optional findFirst() {
        return this.stream.findFirst();
    }

    /**
     * findAny
     *
     * @return findAny
     */
    public Optional findAny() {
        return this.stream.findAny();
    }

    /**
     * @return List
     */
    public List<T> collect() {
        return this.stream.collect(Collectors.toList());
    }

    /**
     * @param collector collector
     * @param <A>       collector a
     * @param <R>       collector r
     * @return r
     */
    public <A, R> R collect(Collector<T, A, R> collector) {
        return this.stream.collect(collector);
    }

    /**
     * @param supplier    supplier
     * @param accumulator accumulator
     * @param combiner    combiner
     * @param <R>         R
     * @return R
     */
    public <R> R collect(Supplier<R> supplier, BiConsumer<R, T> accumulator, BiConsumer<R, R> combiner) {
        return this.stream.collect(supplier, accumulator, combiner);
    }

    /**
     * @param identity    identity
     * @param accumulator accumulator
     * @param combiner    combiner
     * @param <U>         U
     * @return U
     */
    public <U> U reduce(U identity,
                        BiFunction<U, ? super T, U> accumulator,
                        BinaryOperator<U> combiner) {
        return this.stream.reduce(identity, accumulator, combiner);
    }

    /**
     * @param generator generator
     * @return T[]
     */
    public T[] toArray(IntFunction<T[]> generator) {
        return this.stream.toArray(generator);
    }

    /**
     * @param mapper mapper
     * @return DoubleStream
     */
    public DoubleStream flatMapToDouble(Function<T, DoubleStream> mapper) {
        return this.stream.flatMapToDouble(mapper);
    }

    /**
     * @param mapper mapper
     * @return LongStream
     */
    public LongStream flatMapToLong(Function<T, LongStream> mapper) {
        return this.stream.flatMapToLong(mapper);
    }

    /**
     * @param mapper mapper
     * @return IntStream
     */
    public IntStream flatMapToInt(Function<T, IntStream> mapper) {
        return this.stream.flatMapToInt(mapper);
    }

    /**
     * @param mapper mapper
     * @param <R>    r
     * @return Streamable
     */
    public <R> Streamable<R> flatMap(Function<T, Stream<R>> mapper) {
        return new Streamable<R>(this.stream.flatMap(mapper));
    }

    /**
     * @param mapper mapper
     * @param <R>    r
     * @return Streamable
     */
    public <R> Streamable<R> map(Function<T, R> mapper) {
        return new Streamable<>(this.stream.map(mapper));
    }

    /**
     * @return Iterator
     */
    public Iterator iterator() {
        return this.stream.iterator();
    }

    /**
     * @return Spliterator
     */
    public Spliterator spliterator() {
        return this.stream.spliterator();
    }

    /**
     * @return true if parallel
     */
    public boolean isParallel() {
        return this.stream.isParallel();
    }

    /**
     * @return Streamable
     */
    public Streamable sequential() {
        this.stream.sequential();
        return this;
    }

    /**
     * @return paralleled Streamable
     */
    public Streamable parallel() {
        this.stream.parallel();
        return this;
    }

    /**
     * @return unordered Streamable
     */
    public Streamable unordered() {
        this.stream.unordered();
        return this;
    }

    /**
     * @param closeHandler closeHandler
     * @return Streamable
     */
    public Streamable onClose(Runnable closeHandler) {
        this.stream.onClose(closeHandler);
        return this;
    }

    public void close() {
        this.stream.close();
    }
}
