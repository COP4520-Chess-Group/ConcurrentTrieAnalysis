package testing;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import collection.ConcurrentTrie;

public class TestHarness<K, V extends Iterable<K>> {
    protected ArrayList<Iterable<K>> items; 
    protected ArrayList<Iterable<K>> contains;
    protected ArrayList<Iterable<K>> remove;
    
    private ConcurrentSkipListSet<Iterable<K>> skipList;
    private ConcurrentTrie<K, Iterable<K>> trie;

    protected TestHarness(TestBuilder<K, V> builder) {
        this.items = new ArrayList<>(builder.items);
        this.contains = new ArrayList<>(builder.contains);
        this.remove = new ArrayList<>(builder.remove);
    }

    public long runTest(boolean skipList, int numOfThreads) {
        ExecutorService es = Executors.newFixedThreadPool(numOfThreads);
        ArrayList<Iterable<K>> localItems = new ArrayList<>(items);
        ArrayList<Iterable<K>> localContains = new ArrayList<>(contains);
        ArrayList<Iterable<K>> localRemove = new ArrayList<>(remove);
        long start = System.currentTimeMillis();
        if(skipList)
            this.runSkipListTest(es, localItems, localContains, localRemove);
        else   
            this.runTrieTest(es, localItems, localContains, localRemove);
        long end = System.currentTimeMillis();
        return end-start;
    }

    private void runSkipListTest(
        ExecutorService es,
        ArrayList<Iterable<K>> localItems,
        ArrayList<Iterable<K>> localContains,
        ArrayList<Iterable<K>> localRemove
    ) {
        this.skipList = new ConcurrentSkipListSet<>();
        Iterator<Iterable<K>> itemsIter = localItems.iterator();
        Iterator<Iterable<K>> containsIter = localContains.iterator();
        Iterator<Iterable<K>> removesIter = localRemove.iterator();
        boolean val = true;
        while(val) {
            val = false;
            if(itemsIter.hasNext()) {
                Iterable<K> item = itemsIter.next();
                es.submit(() -> this.skipList.add(item));
                val |= itemsIter.hasNext();
            }
            if(containsIter.hasNext()) {
                Iterable<K> containment = containsIter.next();
                es.submit(() -> this.skipList.contains(containment));
                val |= containsIter.hasNext();
            }
            if(removesIter.hasNext()) {
                Iterable<K> remove = removesIter.next();
                es.submit(() -> this.skipList.remove(remove));
                val |= removesIter.hasNext();
            }
        }
        try {
            es.shutdown();
            es.awaitTermination(100, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void runTrieTest(
        ExecutorService es,
        ArrayList<Iterable<K>> localItems,
        ArrayList<Iterable<K>> localContains,
        ArrayList<Iterable<K>> localRemove
    ) {
        this.trie = new ConcurrentTrie<>();
        Iterator<Iterable<K>> itemsIter = localItems.iterator();
        Iterator<Iterable<K>> containsIter = localContains.iterator();
        Iterator<Iterable<K>> removesIter = localRemove.iterator();
        boolean val = true;
        while(val) {
            val = false;
            if(itemsIter.hasNext()) {
                Iterable<K> item = itemsIter.next();
                es.submit(() -> this.trie.add(item));
                val |= itemsIter.hasNext();
            }
            if(containsIter.hasNext()) {
                Iterable<K> containment = containsIter.next();
                es.submit(() -> this.trie.contains(containment));
                val |= containsIter.hasNext();
            }
            if(removesIter.hasNext()) {
                Iterable<K> remove = removesIter.next();
                es.submit(() -> this.trie.remove(remove));
                val |= removesIter.hasNext();
            }
        }
        try {
            es.shutdown();
            es.awaitTermination(100, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static abstract class TestBuilder<K, V extends Iterable<K>> {
        
        protected Collection<Iterable<K>> items;
        protected Collection<Iterable<K>> contains;
        protected Collection<Iterable<K>> remove;

        public abstract TestBuilder<K, V> items(int number);
        public abstract TestBuilder<K, V> contains(int number);
        public abstract TestBuilder<K, V> remove(int number);
        
        public abstract TestHarness<K, V> build();
    }
}
