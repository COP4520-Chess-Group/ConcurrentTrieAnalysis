import java.io.FileReader;
import java.nio.BufferOverflowException;
import collection.ConcurrentTrie;

import java.io.*;

public class Tester
{
    public static ConcurrentTrie<Character, StringWrapper> t = new ConcurrentTrie();
    public static void main(String [] args) throws IOException, InterruptedException
    {
        int threadCount;
        BufferedReader br = new BufferedReader(new FileReader("test.txt"));
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
            System.out.println("Execution Time: " + seconds + " seconds");
        } finally {
            br.close();
        }
    }
}