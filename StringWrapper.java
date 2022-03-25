import java.util.Iterator;
class StringWrapper implements Iterable<Character>, Iterator<Character> {

    private String string;
    private ThreadLocal<Integer> tl = new ThreadLocal<>();

    public StringWrapper(String string) {
        this.string = string;
        tl.set(0);
    }

    @Override
    public Iterator<Character> iterator() {
        tl.set(0);
        return this;
    }

    private String getString() {
        return this.string;
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
}