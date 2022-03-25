package collection;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentTrie<K, V extends Iterable<K>> {
    private ConcurrentTrieNode<K, V> root;

    public ConcurrentTrie() {
        root = new ConcurrentTrieNode<>();
    }

    public boolean isEmpty() {
        synchronized (root.getChildren()) {
            return root.getChildren().isEmpty();
        }
    }

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
                current = node;
                if (node.getValue() != null)
                    if (node.getValue().equals(o))
                        break;
            }
        }
        return current.isEnd();
    }

    public void add(V e) {
        ConcurrentTrieNode<K, V> current = root;
        Iterator<K> keys = e.iterator();
        while (keys.hasNext()) {
            K key = keys.next();
            ConcurrentHashMap<K, ConcurrentTrieNode<K, V>> children = current.getChildren();
            synchronized (children) {
                current = current.getChildren().computeIfAbsent(key, c -> new ConcurrentTrieNode<K, V>());
            }
        }
        current.setValue(e);
        current.setEnd(true);
    }

    public boolean remove(V o) {
        ConcurrentTrieNode<K, V> current = root;
        Iterator<K> keys = o.iterator();
        K key = keys.next();
        while (keys.hasNext()) {
            K childKey = keys.next();
            ConcurrentHashMap<K, ConcurrentTrieNode<K, V>> children = current.getChildren();
            synchronized (children) {
                current = children.get(key);
                if (current == null)
                    return false;
                children = current.getChildren();
                synchronized (children) {
                    if (children.get(childKey).getValue() != null) {
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
                }
            }
            key = childKey;
        }
        return false;
    }

}
