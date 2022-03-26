import java.io.*;
public class SkipThread extends Thread
{
    public void run()
    {
        try{
            BufferedReader br = new BufferedReader(new FileReader(this.getName()));
            try{
                int commands = Integer.parseInt(br.readLine());
                int wordCount = Integer.parseInt(br.readLine());
                String cmdType;
                String success;
                boolean s = true, actualS = true;
                int word;
                String wordBank[] = new String[wordCount];
                for (int i = 0; i < wordCount; i++)
                {
                    wordBank[i] = br.readLine();
                }
                for (int i = 0; i < commands; i++)
                {
                    cmdType = br.readLine();
                    word = Integer.parseInt(br.readLine());
                    if (cmdType.compareTo("ADD") != 0)
                    {
                        success = br.readLine();
                        if (success.compareTo("TRUE") == 0)
                        {
                            s = true;
                        }
                        else
                        {
                            s = false;
                        }
                    }
                    if (cmdType.compareTo("ADD") == 0)
                    {
                        Tester.s.add(wordBank[word]);
                        if (Tester.reporting) { System.out.println(this.getName() + " added " + wordBank[word]); }
                    }
                    else if (cmdType.compareTo("CONTAINS") == 0)
                    {
                        if (Tester.s.contains(wordBank[word]))
                        {
                            if (Tester.reporting) { System.out.println(this.getName() + " found that contains " + wordBank[word] + " is true"); }
                            actualS = true;
                        }
                        else
                        {
                            if (Tester.reporting) { System.out.println(this.getName() + " found that contains " + wordBank[word] + " is false"); }
                            actualS = false;
                        }
                    }
                    else if (cmdType.compareTo("REMOVE") == 0)
                    {
                        if (Tester.s.remove(wordBank[word]))
                        {
                            if (Tester.reporting) { System.out.println(this.getName() + " removed " + wordBank[word]); }
                            actualS = true;
                        }
                        else
                        {
                            if (Tester.reporting) { System.out.println(this.getName() + " failed to remove " + wordBank[word]); }
                            actualS = false;
                        }
                    }
                    if (cmdType.compareTo("ADD") != 0 && s != actualS)
                    {
                        System.out.println("Error");
                    }
                }
            } finally {
                br.close();
            }
        }
        catch(IOException e) {}
    }
}