package cn.ecosync.iframework.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface CollectionDelegate<E> extends Collection<E> {
    Collection<E> delegate();

    default Collection<E> collection() {
        return CollectionUtils.nullSafeOf(delegate());
    }

    @Override
    default int size() {
        return collection().size();
    }

    @Override
    default boolean isEmpty() {
        return collection().isEmpty();
    }

    @Override
    default boolean contains(Object o) {
        return collection().contains(o);
    }

    @Override
    default Iterator<E> iterator() {
        return collection().iterator();
    }

    @Override
    default Object[] toArray() {
        return collection().toArray();
    }

    @Override
    default <T> T[] toArray(T[] a) {
        return collection().toArray(a);
    }

    @Override
    default boolean add(E e) {
        return collection().add(e);
    }

    @Override
    default boolean remove(Object o) {
        return collection().remove(o);
    }

    @Override
    default boolean containsAll(Collection<?> c) {
        return collection().containsAll(c);
    }

    @Override
    default boolean addAll(Collection<? extends E> c) {
        return collection().addAll(c);
    }

    @Override
    default boolean removeAll(Collection<?> c) {
        return collection().removeAll(c);
    }

    @Override
    default boolean retainAll(Collection<?> c) {
        return collection().retainAll(c);
    }

    @Override
    default void clear() {
        collection().clear();
    }

    @Override
    default <T> T[] toArray(IntFunction<T[]> generator) {
        return collection().toArray(generator);
    }

    @Override
    default boolean removeIf(Predicate<? super E> filter) {
        return collection().removeIf(filter);
    }

    @Override
    default Spliterator<E> spliterator() {
        return collection().spliterator();
    }

    @Override
    default Stream<E> stream() {
        return collection().stream();
    }

    @Override
    default Stream<E> parallelStream() {
        return collection().parallelStream();
    }

    @Override
    default void forEach(Consumer<? super E> action) {
        collection().forEach(action);
    }
}
