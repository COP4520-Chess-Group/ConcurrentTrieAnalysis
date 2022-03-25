import java.io.*;
public class TestThread extends Thread
{
    public void run()
    {
        try{
            BufferedReader br = new BufferedReader(new FileReader(this.getName()));
            try{
                int commands = Integer.parseInt(br.readLine());
                int wordCount = Integer.parseInt(br.readLine());
                String cmdType;
                int word;
                String wordBank[] = new String[wordCount];
                StringWrapper words[] = new StringWrapper[wordCount];
                for (int i = 0; i < wordCount; i++)
                {
                    wordBank[i] = br.readLine();
                    words[i] = new StringWrapper(wordBank[i]);
                }
                for (int i = 0; i < commands; i++)
                {
                    cmdType = br.readLine();
                    word = Integer.parseInt(br.readLine());
                    if (cmdType.compareTo("ADD") == 0)
                    {
                        Tester.t.add(words[word]);
                        if (Tester.reporting) { System.out.println(this.getName() + " added " + wordBank[word]); }
                    }
                    else if (cmdType.compareTo("CONTAINS") == 0)
                    {
                        if (Tester.t.contains(words[word]))
                        {
                            if (Tester.reporting) { System.out.println(this.getName() + " found that contains " + wordBank[word] + " is true"); }
                        }
                        else
                        {
                            if (Tester.reporting) { System.out.println(this.getName() + " found that contains " + wordBank[word] + " is false"); }
                        }
                    }
                    else if (cmdType.compareTo("REMOVE") == 0)
                    {
                        if (Tester.t.remove(words[word]))
                        {
                            if (Tester.reporting) { System.out.println(this.getName() + " removed " + wordBank[word]); }
                        }
                        else
                        {
                            if (Tester.reporting) { System.out.println(this.getName() + " failed to remove " + wordBank[word]); }
                        }
                    }
                }
            } finally {
                br.close();
            }
        }
        catch(IOException e) {}
    }
}