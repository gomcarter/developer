package io.github.gomcarter.frameworks.base.streaming;

import com.google.common.collect.Lists;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * @Author： gomcarter
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
     * @param keyMapper
     * @param valueMapper
     * @param <KEY>       :  map的key
     * @param <VAL>       ： 新的值 Collection< VAL >
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
     * @param keyMapper
     * @param <KEY>     :  map的key
     * @return Groupable
     */
    public <KEY> Groupable<KEY, List<T>> groupby(Function<T, KEY> keyMapper) {
        return this.groupby(keyMapper, Function.identity());
    }

    /**
     * @param keyMapper
     * @param valueMapper
     * @param <KEY>       :  map的key
     * @param <VAL>       ： 新的值 VAL, 如果基于key重复了，则报错 java.lang.IllegalStateException
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
     * @param keyMapper
     * @param <KEY>     :  map的key
     * @return Groupable
     */
    public <KEY> Groupable<KEY, T> uniqueGroupby(Function<T, KEY> keyMapper) {
        return this.uniqueGroupby(keyMapper, Function.identity());
    }

    /**
     * @param keyMapper
     * @param valueMapper
     * @param <KEY>       :  map的key
     * @param <VAL>       ： 新的值 VAL, 如果基于key重复了，新的值覆盖老的值
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
     * @param <KEY>     :  map的key
     * @return Groupable
     */
    public <KEY> Groupable<KEY, T> uniqueGroupbySafely(Function<T, KEY> keyMapper) {
        return this.uniqueGroupbySafely(keyMapper, Function.identity());
    }

    public Streamable<T> filter(Predicate<T> predicate) {
        this.stream = this.stream.filter(predicate);
        return this;
    }

    public IntStream mapToInt(ToIntFunction<T> mapper) {
        return this.stream.mapToInt(mapper);
    }

    public LongStream mapToLong(ToLongFunction<T> mapper) {
        return this.stream.mapToLong(mapper);
    }

    public DoubleStream mapToDouble(ToDoubleFunction<T> mapper) {
        return this.stream.mapToDouble(mapper);
    }

    public Streamable<T> distinct() {
        this.stream = this.stream.distinct();
        return this;
    }

    public Streamable<T> sorted() {
        this.stream = this.stream.sorted();
        return this;
    }

    public Streamable<T> sorted(Comparator<? super T> comparator) {
        this.stream = this.stream.sorted(comparator);
        return this;
    }

    public Streamable<T> peek(Consumer<? super T> action) {
        this.stream = this.stream.peek(action);
        return this;
    }

    public Streamable<T> limit(long maxSize) {
        this.stream = this.stream.limit(maxSize);
        return this;
    }

    public Streamable<T> skip(long n) {
        this.stream = this.stream.skip(n);
        return this;
    }

    public void forEach(Consumer<? super T> action) {
        this.stream.forEach(action);
    }

    public void forEachOrdered(Consumer<? super T> action) {
        this.stream.forEachOrdered(action);
    }

    public Object[] toArray() {
        return this.stream.toArray();
    }

    public Optional<T> min(Comparator<? super T> comparator) {
        return this.stream.min(comparator);
    }

    public Optional<T> max(Comparator<? super T> comparator) {
        return this.stream.max(comparator);
    }

    public long count() {
        return this.stream.count();
    }

    public boolean anyMatch(Predicate<? super T> predicate) {
        return this.stream.anyMatch(predicate);
    }

    public boolean allMatch(Predicate<? super T> predicate) {
        return this.stream.allMatch(predicate);
    }

    public boolean noneMatch(Predicate<? super T> predicate) {
        return this.stream.noneMatch(predicate);
    }

    public Optional findFirst() {
        return this.stream.findFirst();
    }

    public Optional findAny() {
        return this.stream.findAny();
    }

    public List<T> collect() {
        return this.stream.collect(Collectors.toList());
    }

    public <A, R> R collect(Collector<T, A, R> collector) {
        return this.stream.collect(collector);
    }

    public <R> R collect(Supplier<R> supplier, BiConsumer<R, T> accumulator, BiConsumer<R, R> combiner) {
        return this.stream.collect(supplier, accumulator, combiner);
    }

    public <U> U reduce(U identity,
                        BiFunction<U, ? super T, U> accumulator,
                        BinaryOperator<U> combiner) {
        return this.stream.reduce(identity, accumulator, combiner);
    }

    public T[] toArray(IntFunction<T[]> generator) {
        return this.stream.toArray(generator);
    }

    public DoubleStream flatMapToDouble(Function<T, DoubleStream> mapper) {
        return this.stream.flatMapToDouble(mapper);
    }

    public LongStream flatMapToLong(Function<T, LongStream> mapper) {
        return this.stream.flatMapToLong(mapper);
    }

    public IntStream flatMapToInt(Function<T, IntStream> mapper) {
        return this.stream.flatMapToInt(mapper);
    }

    public <R> Streamable<R> flatMap(Function<T, Stream<R>> mapper) {
        return new Streamable<R>(this.stream.flatMap(mapper));
    }

    public <R> Streamable<R> map(Function<T, R> mapper) {
        return new Streamable<>(this.stream.map(mapper));
    }

    public Iterator iterator() {
        return this.stream.iterator();
    }

    public Spliterator spliterator() {
        return this.stream.spliterator();
    }

    public boolean isParallel() {
        return this.stream.isParallel();
    }

    public Streamable sequential() {
        this.stream.sequential();
        return this;
    }

    public Streamable parallel() {
        this.stream.parallel();
        return this;
    }

    public Streamable unordered() {
        this.stream.unordered();
        return this;
    }

    public Streamable onClose(Runnable closeHandler) {
        this.stream.onClose(closeHandler);
        return this;
    }

    public Streamable close() {
        this.stream.close();
        return this;
    }


}
