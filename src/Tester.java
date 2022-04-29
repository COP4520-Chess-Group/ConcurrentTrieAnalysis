import testing.StringTestHarness;
import testing.TestHarness;
import java.util.Scanner;


public class Tester {

    public static void main(String[] args) {
        TestHarness test;
        Scanner scanner = new Scanner(System.in);
        boolean save = false;
        outer: while (true) {
            System.out.print("How long of a string would you like to test? ");
            int len = scanner.nextInt();
            System.out.print("How many strings in the list? ");
            int num = scanner.nextInt();
            System.out.println("How many contains runs? ");
            int contains = scanner.nextInt();
            System.out.println("How many removals? ");
            int removes = scanner.nextInt();
            testType: while (true) {
                System.out.println(
                        "Which structure would you like to test?:\n(1) SkipListSet\n(2) Trie\nEnter your number below (-1 to exit):");
                int choice = scanner.nextInt();
                boolean skipList;
                if (choice == 1)
                    skipList = true;
                else if (choice == 2)
                    skipList = false;
                else if (choice < 0)
                    return;
                else {
                    System.out.println("That was not an option.");
                    continue;
                }
                
                System.out.print("How many threads? ");
                int threads = scanner.nextInt();

                System.out.println("Running...");
                test = new StringTestHarness.StringTestBuilder().stringLength(len).items(num).contains(contains)
                        .remove(removes).build();
                System.out.printf(
                        "Ran %s on %d threads with %d character long strings with %d strings in the list, %d contains, and %d removals in %dms.\n",
                        skipList ? "SkipList test" : "Trie test",
                        threads,
                        len,
                        num,
                        contains,
                        removes,
                        test.runTest(skipList, threads));
                System.out.print("Would you like to save the length, number of items, and contains (1 - yes, 2 - no)?");
                choice = scanner.nextInt();
                if (choice == 1)
                    continue testType;
                else
                    continue outer;
            }
        }
    }
}