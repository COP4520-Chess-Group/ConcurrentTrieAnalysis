package collection;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentTrieNode<K, V extends Iterable<K>> {
    private volatile ConcurrentHashMap<K, ConcurrentTrieNode<K, V>> children;
    private V value;
    private boolean isEnd = false;

    protected ConcurrentTrieNode() {
        children = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<K, ConcurrentTrieNode<K, V>> getChildren() {
        return children;
    }

    public void setEnd(boolean b) {
        this.isEnd = b;
    }

    public boolean isEnd() {
        return this.isEnd;
    }

    public V getValue() {
        return this.value;
    }

    public void setValue(V e) {
        this.value = e;
    }
}
