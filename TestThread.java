import java.io.*;
public class TestThread extends Thread
{
    public void run()
    {
        try{
            BufferedReader br = new BufferedReader(new FileReader(this.getName()));
            try{
                int commands = Integer.parseInt(br.readLine());
                String cmdType, str;
                StringWrapper s;
                for (int i = 0; i < commands; i++)
                {
                    cmdType = br.readLine();
                    str = br.readLine();
                    s = new StringWrapper(str);
                    if (cmdType.compareTo("ADD") == 0)
                    {
                        Tester.t.add(s);
                        System.out.println(this.getName() + " added " + str);
                    }
                    else if (cmdType.compareTo("CONTAINS") == 0)
                    {
                        if (Tester.t.contains(s))
                        {
                            System.out.println(this.getName() + " found that contains " + str + " is true");
                        }
                        else
                        {
                            System.out.println(this.getName() + " found that contains " + str + " is false"); 
                        }
                    }
                    else if (cmdType.compareTo("REMOVE") == 0)
                    {
                        Tester.t.remove(s);
                        System.out.println(this.getName() + " removed " + str);
                    }
                }
            } finally {
                br.close();
            }
        }
        catch(IOException e) {}
    }
}