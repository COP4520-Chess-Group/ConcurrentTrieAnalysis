package types;

import java.util.Iterator;
import java.util.Random;

public class StringWrapper implements Iterable<Character>, Iterator<Character>, Comparable<StringWrapper> {

    private String string;
    private ThreadLocal<Integer> tl = new ThreadLocal<>();
    private static Random RandomGenerator;
    private static byte[] buf = new byte[1024];

    public StringWrapper(String string) {
        this.string = string;
        tl.set(0);
    }

    @Override
    public Iterator<Character> iterator() {
        tl.set(0);
        return this;
    }

    @Override
    public boolean hasNext() {
        return tl.get() + 1 <= this.string.length();
    }

    @Override
    public Character next() {
        char c = this.string.charAt(tl.get());
        tl.set(tl.get() + 1);
        return c;
    }

    @Override
    public String toString() {
        return this.string;
    }

    @Override
    public int compareTo(StringWrapper o) {
        return this.string.compareTo(o.string);
    }

    public static StringWrapper randomString(int len) {
        if(RandomGenerator == null)
            RandomGenerator = new Random(len);
        
        if(len > 1024) len = 1024;
		
		buf[len - 1] = (byte) 0;

		for(int j = 0; j < (len - 1); j++) {
			buf[j] = (byte) (RandomGenerator.nextInt(94) + 32);
		}

		return new StringWrapper(new String(buf));
    }
}