package collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentTrie<K, V extends Iterable<K>> implements Collection<V> {
    private ConcurrentTrieNode<K, V> root;

    public ConcurrentTrie() {
        root = new ConcurrentTrieNode<>();
    }

    /**
     * 
     * @return whether this trie is empty or not
     */
    public boolean isEmpty() {
        synchronized (root.getChildren()) {
            return root.getChildren().isEmpty();
        }
    }

    /**
     * 
     * @param o Search object
     * @return Whether the trie contains the search object or not
     */
    public boolean contains(Object o) {
        ConcurrentTrieNode<K, V> current = root;
        if (o instanceof Iterable<?>) {
            Iterator<?> keys = ((Iterable<?>) o).iterator();
            while (keys.hasNext()) {
                Object key = keys.next();
                ConcurrentHashMap<K, ConcurrentTrieNode<K, V>> children = current.getChildren();
                ConcurrentTrieNode<K, V> node = children.get(key);
                if (node == null) {
                    return false;
                }

                if (node.getValue() != null)
                    if (node.getValue().equals(o))
                        break;

                current = node;
            }
        }
        return current.isEnd();
    }

    /**
     * 
     * @param e
     */
    @Override
    public boolean add(V e) {
        ConcurrentTrieNode<K, V> current = root;
        Iterator<K> keys = e.iterator();
        while (keys.hasNext()) {
            K key = keys.next();
            current = current.getChildren().computeIfAbsent(key, c -> new ConcurrentTrieNode<K, V>());
        }

        // At this point, the node is added
        current.setValue(e);
        current.setEnd(true);

        return true;
    }

    @Override
    public boolean remove(Object o) {
        ConcurrentTrieNode<K, V> current = root;
        if(!(o instanceof Iterable<?>)) {
            return false;
        }
        Iterator<?> keys = ((Iterable<?>) o).iterator();
        Object key = keys.next();
        while (keys.hasNext()) {
            Object childKey = keys.next();
            ConcurrentHashMap<K, ConcurrentTrieNode<K, V>> children = current.getChildren();
            current = children.get(key);
            if (current == null)
                return false;
            children = current.getChildren();
            if (children.get(childKey) != null && children.get(childKey).getValue() != null) {
                ConcurrentTrieNode<K, V> child = children.get(childKey);
                if (child.getValue().equals(o) && child.isEnd()) {
                    if (child.getChildren().isEmpty()) {
                        children.remove(key);
                        child.setEnd(false);
                        return true;
                    } else {
                        child.setEnd(false);
                        child.setValue(null);
                        return true;
                    }
                } else {
                    return false;
                }
            }
            key = childKey;
        }
        return false;
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<V> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object[] toArray() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends V> c) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        root = new ConcurrentTrieNode<>();
    }

}
