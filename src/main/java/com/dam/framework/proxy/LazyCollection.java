package com.dam.framework.proxy;

import java.lang.reflect.Proxy;
import java.util.*;

/**
 * Lazy loading collection that loads elements on demand.
 * Used for @OneToMany relationships with LAZY fetch type.
 * 
 * @param <E> The element type
 * @author Dev 1
 */
public class LazyCollection<E> implements List<E>, LazyLoadable {
    
    private final Class<E> elementClass;
    private final Object ownerId;
    private final String mappedBy;
    private final CollectionLoader<E> loader;
    
    private List<E> target;
    private boolean loaded;
    
    /**
     * Functional interface for loading collection elements.
     * 
     * @param <E> The element type
     */
    @FunctionalInterface
    public interface CollectionLoader<E> {
        List<E> load(Class<E> elementClass, Object ownerId, String mappedBy);
    }
    
    /**
     * Create a new lazy collection.
     * 
     * @param elementClass The class of elements in the collection
     * @param ownerId The ID of the owning entity
     * @param mappedBy The field name in the element that references the owner
     * @param loader The loader function to fetch elements
     */
    public LazyCollection(Class<E> elementClass, Object ownerId, String mappedBy, 
            CollectionLoader<E> loader) {
        this.elementClass = elementClass;
        this.ownerId = ownerId;
        this.mappedBy = mappedBy;
        this.loader = loader;
        this.loaded = false;
    }
    
    /**
     * Load the collection elements if not already loaded.
     */
    private void ensureLoaded() {
        if (!loaded) {
            synchronized (this) {
                if (!loaded) {
                    target = loader.load(elementClass, ownerId, mappedBy);
                    if (target == null) {
                        target = new ArrayList<>();
                    }
                    loaded = true;
                }
            }
        }
    }
    
    @Override
    public boolean isLoaded() {
        return loaded;
    }
    
    @Override
    public Object load() {
        ensureLoaded();
        return target;
    }
    
    @Override
    public Class<?> getEntityClass() {
        return elementClass;
    }
    
    @Override
    public Object getEntityId() {
        return ownerId;
    }
    
    // ========== List interface implementation ==========
    
    @Override
    public int size() {
        ensureLoaded();
        return target.size();
    }
    
    @Override
    public boolean isEmpty() {
        ensureLoaded();
        return target.isEmpty();
    }
    
    @Override
    public boolean contains(Object o) {
        ensureLoaded();
        return target.contains(o);
    }
    
    @Override
    public Iterator<E> iterator() {
        ensureLoaded();
        return target.iterator();
    }
    
    @Override
    public Object[] toArray() {
        ensureLoaded();
        return target.toArray();
    }
    
    @Override
    public <T> T[] toArray(T[] a) {
        ensureLoaded();
        return target.toArray(a);
    }
    
    @Override
    public boolean add(E e) {
        ensureLoaded();
        return target.add(e);
    }
    
    @Override
    public boolean remove(Object o) {
        ensureLoaded();
        return target.remove(o);
    }
    
    @Override
    public boolean containsAll(Collection<?> c) {
        ensureLoaded();
        return target.containsAll(c);
    }
    
    @Override
    public boolean addAll(Collection<? extends E> c) {
        ensureLoaded();
        return target.addAll(c);
    }
    
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        ensureLoaded();
        return target.addAll(index, c);
    }
    
    @Override
    public boolean removeAll(Collection<?> c) {
        ensureLoaded();
        return target.removeAll(c);
    }
    
    @Override
    public boolean retainAll(Collection<?> c) {
        ensureLoaded();
        return target.retainAll(c);
    }
    
    @Override
    public void clear() {
        ensureLoaded();
        target.clear();
    }
    
    @Override
    public E get(int index) {
        ensureLoaded();
        return target.get(index);
    }
    
    @Override
    public E set(int index, E element) {
        ensureLoaded();
        return target.set(index, element);
    }
    
    @Override
    public void add(int index, E element) {
        ensureLoaded();
        target.add(index, element);
    }
    
    @Override
    public E remove(int index) {
        ensureLoaded();
        return target.remove(index);
    }
    
    @Override
    public int indexOf(Object o) {
        ensureLoaded();
        return target.indexOf(o);
    }
    
    @Override
    public int lastIndexOf(Object o) {
        ensureLoaded();
        return target.lastIndexOf(o);
    }
    
    @Override
    public ListIterator<E> listIterator() {
        ensureLoaded();
        return target.listIterator();
    }
    
    @Override
    public ListIterator<E> listIterator(int index) {
        ensureLoaded();
        return target.listIterator(index);
    }
    
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        ensureLoaded();
        return target.subList(fromIndex, toIndex);
    }
    
    @Override
    public String toString() {
        if (!loaded) {
            return String.format("LazyCollection<%s>[ownerId=%s, loaded=false]", 
                elementClass.getSimpleName(), ownerId);
        }
        return target.toString();
    }
}
