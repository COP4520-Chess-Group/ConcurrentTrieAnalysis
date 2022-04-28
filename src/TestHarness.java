import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import collection.ConcurrentTrie;

public class TestHarness<K, V extends Iterable<K>> {
    private ArrayList<? extends Iterable<K>> items; 
    private ArrayList<? extends Iterable<K>> contains;
    private ArrayList<? extends Iterable<K>> remove;
    
    private ConcurrentSkipListSet<? extends Iterable<K>> skipList;
    private ConcurrentTrie<K, ? extends Iterable<K>> trie;

    public TestHarness(
        Collection<? extends Iterable<K>> items,
        Collection<? extends Iterable<K>> contains,
        Collection<? extends Iterable<K>> remove
    ) {
        this.items = new ArrayList<>(items);
        this.contains = new ArrayList<>(contains);
        this.remove = new ArrayList<>(remove);
    }

    public long runTest(boolean skipList, int numOfThreads) {
        ExecutorService es = Executors.newFixedThreadPool(numOfThreads);
        ArrayList<? extends Iterable<K>> localItems = new ArrayList<>(items);
        ArrayList<? extends Iterable<K>> localContains = new ArrayList<>(contains);
        ArrayList<? extends Iterable<K>> localRemove = new ArrayList<>(remove);
        long start = System.currentTimeMillis();
        if(skipList)
            this.runSkipListTest(es, localItems, localContains, localRemove);
        else   
            this.runTrieTest(es, localItems, localContains, localRemove);
        long end = System.currentTimeMillis();
        return end-start;
    }

    private <V extends Iterable<K>> runSkipListTest(
        ExecutorService es,
        ArrayList<? extends Iterable<K>> localItems,
        ArrayList<? extends Iterable<K>> localContains,
        ArrayList<? extends Iterable<K>> localRemove
    ) {
        this.skipList = new ConcurrentSkipListSet<>();
        Iterator<? extends Iterable<K>> itemsIter = localItems.iterator();
        Iterator<? extends Iterable<K>> containsIter = localContains.iterator();
        Iterator<? extends Iterable<K>> removesIter = localRemove.iterator();
        boolean val = true;
        while(val) {
            val = false;
            if(itemsIter.hasNext()) {
                V item = (Iterable<K>) itemsIter.next();
                es.submit(() -> this.skipList.add(item));
                val |= itemsIter.hasNext();
            }
            if(containsIter.hasNext()) {
                V containment = containsIter.next();
                es.submit(() -> this.skipList.contains(containment));
                val |= containsIter.hasNext();
            }
            if(removesIter.hasNext()) {
                V remove = removesIter.next();
                es.submit(() -> this.skipList.remove(remove));
                val |= removesIter.hasNext();
            }
        }
        es.shutdown();
        es.awaitTermination(100, TimeUnit.SECONDS);
    }

    private void runTrieTest(
        ExecutorService es,
        ArrayList<? extends Iterable<K>> localItems,
        ArrayList<? extends Iterable<K>> localContains,
        ArrayList<? extends Iterable<K>> localRemove
    ) {
        this.trie = new ConcurrentTrie<>();
        Iterator<Iterable<K>> itemsIter = localItems.iterator();
        Iterator<Iterable<T>> containsIter = localContains.iterator();
        Iterator<Iterable<T>> removesIter = localRemove.iterator();
        boolean val = true;
        while(val) {
            val = false;
            if(itemsIter.hasNext()) {
                V item = itemsIter.next();
                es.submit(() -> this.trie.add(item));
                val |= itemsIter.hasNext();
            }
            if(containsIter.hasNext()) {
                V containment = containsIter.next();
                es.submit(() -> this::trie.contains(containment));
                val |= containsIter.hasNext();
            }
            if(removesIter.hasNext()) {
                V remove = removesIter.next();
                es.submit(() -> this.trie.remove(remove));
                val |= removesIter.hasNext();
            }
        }
        es.shutdown();
        es.awaitTermination(100, TimeUnit.SECONDS);
    }


}
