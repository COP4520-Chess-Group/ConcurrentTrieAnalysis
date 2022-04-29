package testing;
import java.util.HashSet;
import java.util.Random;

import types.StringWrapper;

public class StringTestHarness extends TestHarness<Character, StringWrapper> {
    
    private StringTestHarness(StringTestBuilder builder) {
        super(builder);
    }

    public static class StringTestBuilder extends TestHarness.TestBuilder<Character, StringWrapper> {
        private int stringLength;

        @Override
        public StringTestBuilder items(int number) {
            this.items = new HashSet<Iterable<Character>>();
            for(int i = 0; i < number; i++)
                items.add(StringWrapper.randomString(this.stringLength));
            return this;
        }

        @Override
        public StringTestBuilder contains(int number) {
            this.contains = new HashSet<Iterable<Character>>();
            Object[] items = this.items.toArray(); 
            Random RandomGenerator = new Random(number);
            for(int i = 0; i < number; i++)
                if(items instanceof Iterable<?>[])
                    this.contains.add((Iterable<Character>) items[RandomGenerator.nextInt(this.items.size())]);
            return this;
        }

        @Override
        public StringTestBuilder remove(int number) {
            this.remove = new HashSet<Iterable<Character>>();
            Object[] items = this.items.toArray(); 
            Random RandomGenerator = new Random(number);
            for(int i = 0; i < number; i++)
                if(items instanceof Iterable<?>[])
                    this.remove.add((Iterable<Character>) items[RandomGenerator.nextInt(this.items.size())]);
            return this;
        }

        public StringTestBuilder stringLength(int number) {
            this.stringLength = number;
            return this;
        }

        @Override
        public TestHarness<Character, StringWrapper> build() {
            return new StringTestHarness(this);
        }

    }
}
