import java.io.FileReader;
import collection.ConcurrentTrie;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicLong;

import java.io.*;

public class Tester
{
    public static ConcurrentTrie<Character, StringWrapper> t = new ConcurrentTrie<>();
    public static ConcurrentSkipListSet<String> s = new ConcurrentSkipListSet<>();
    public static boolean reporting = false;
    public static AtomicLong addTime = new AtomicLong(0);
    public static AtomicLong containsTime = new AtomicLong(0);
    public static AtomicLong removeTime = new AtomicLong(0);
    public static void main(String [] args) throws IOException, InterruptedException
    {
        int threadCount;
        BufferedReader br = new BufferedReader(new FileReader("ConcurrentTrieAnalysis/resources/test.txt"));
        try{
            threadCount = Integer.parseInt(br.readLine());
            String files[] = new String[threadCount];
            for (int i = 0; i < threadCount; i++)
            {
                files[i] = br.readLine();
            }
            long startTime = System.nanoTime();
            TestThread threads[] = new TestThread[threadCount];
            for (int i = 0; i < threadCount; i++)
            {
                threads[i] = new TestThread();
                threads[i].setName(files[i]);
                threads[i].start();
            }
            for (int i = 0; i < threadCount; i++)
            {
                threads[i].join();
            }
            long stopTime = System.nanoTime();
            double seconds = (double)(stopTime - startTime) / 1_000_000_000.0;
            System.out.println("ConcurrentTrie Execution: ");
            System.out.println("Total Time Elapsed: " + seconds);
            seconds = addTime.get() / 1_000_000_000.0;
            System.out.println("Time Adding: " + seconds);
            seconds = containsTime.get() / 1_000_000_000.0;
            System.out.println("Time Contains: " + seconds);
            seconds = removeTime.get() / 1_000_000_000.0;
            System.out.println("Time Removing: " + seconds);
            addTime.set(0);
            containsTime.set(0);
            removeTime.set(0);
        } finally {
            br.close();
        }

        br = new BufferedReader(new FileReader("ConcurrentTrieAnalysis/resources/test.txt"));
        try{
            threadCount = Integer.parseInt(br.readLine());
            String files[] = new String[threadCount];
            for (int i = 0; i < threadCount; i++)
            {
                files[i] = br.readLine();
            }
            long startTime = System.nanoTime();
            SkipThread threads[] = new SkipThread[threadCount];
            for (int i = 0; i < threadCount; i++)
            {
                threads[i] = new SkipThread();
                threads[i].setName(files[i]);
                threads[i].start();
            }
            for (int i = 0; i < threadCount; i++)
            {
                threads[i].join();
            }
            long stopTime = System.nanoTime();
            double seconds = (double)(stopTime - startTime) / 1_000_000_000.0;
            System.out.println("ConcurrentSkipListSet Execution: ");
            System.out.println("Total Time Elapsed: " + seconds);
            seconds = addTime.get() / 1_000_000_000.0;
            System.out.println("Time Adding: " + seconds);
            seconds = containsTime.get() / 1_000_000_000.0;
            System.out.println("Time Contains: " + seconds);
            seconds = removeTime.get() / 1_000_000_000.0;
            System.out.println("Time Removing: " + seconds);
        } finally {
            br.close();
        }
    }
}