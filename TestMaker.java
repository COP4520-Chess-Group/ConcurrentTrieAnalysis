import java.util.*;

public class TestMaker
{
    public static int counter = 0;
    public static HashMap<String, Integer> ids = new HashMap<String, Integer>();
    public static HashMap<Integer, String> strs = new HashMap<Integer, String>();
    // Permutation code based on code from UCF Team Programming Notes
    public static HashSet<String> permutations(int numb)
    {
        String alpha = "abcdefghijklmnopqrstuvwxyz";
        String[] items = new String[26];
        for (int i = 0; i < 26; i++)
        {
            items[i] = alpha.substring(i, i+1);
        }
        int count = items.length;
        int[] permutation = new int[count];
        boolean[] used = new boolean[count];
        HashSet<String> permutations = new HashSet<>();
        permutations(items, 0, permutation, used, count, permutations, numb);
        return permutations;
    }

    public static void permutations(String[] items, int position, int[] permutation, boolean[] used, int count, HashSet<String> permutations, int numb)
    {
        if (permutations.size() == numb)
        {
            return;
        }
        else if (position == count)
        {
            String s = "";
            for (int i = 0; i < count; i++)
            {
                s += items[permutation[i]];
            }
            permutations.add(s);
            ids.put(s, counter);
            strs.put(counter, s);
            counter++;
        }
        else
        {
            for (int i = 0; i < count; i++)
            {
                if (!used[i])
                {
                    used[i] = true;
                    permutation[position] = i;
                    permutations(items, position + 1, permutation, used, count, permutations, numb);
                    used[i] = false;
                }
            }
        }
    }

    public static void add(HashSet<String> perms, HashSet<String> struct, int count, ArrayList<String> cmds)
    {
        Iterator<String> it = perms.iterator();
        String temp;
        while (count > 0)
        {
            temp = it.next();
            if (!struct.contains(temp))
            {
                struct.add(temp);
                count--;
                cmds.add("ADD");
                cmds.add(Integer.toString(ids.get(temp)));
            }
        }
    }

    public static void containSuccess(HashSet<String> perms, HashSet<String> struct, int count, ArrayList<String> cmds)
    {
        Iterator<String> it = perms.iterator();
        String temp;
        while (count > 0)
        {
            temp = it.next();
            if (struct.contains(temp))
            {
                count--;
                cmds.add("CONTAINS");
                cmds.add(Integer.toString(ids.get(temp)));
                cmds.add("TRUE");
            }
        }
    }

    public static void containFail(HashSet<String> perms, HashSet<String> struct, int count, ArrayList<String> cmds)
    {
        Iterator<String> it = perms.iterator();
        String temp;
        while (count > 0)
        {
            temp = it.next();
            if (!struct.contains(temp))
            {
                count--;
                cmds.add("CONTAINS");
                cmds.add(Integer.toString(ids.get(temp)));
                cmds.add("FALSE");
            }
        }
    }

    public static void removeSuccess(HashSet<String> perms, HashSet<String> struct, int count, ArrayList<String> cmds)
    {
        Iterator<String> it = perms.iterator();
        String temp;
        while (count > 0)
        {
            temp = it.next();
            if (struct.contains(temp))
            {
                struct.remove(temp);
                count--;
                cmds.add("REMOVE");
                cmds.add(Integer.toString(ids.get(temp)));
                cmds.add("TRUE");
            }
        }
    }

    public static void removeFail(HashSet<String> perms, HashSet<String> struct, int count, ArrayList<String> cmds)
    {
        Iterator<String> it = perms.iterator();
        String temp;
        while (count > 0)
        {
            temp = it.next();
            if (!struct.contains(temp))
            {
                count--;
                cmds.add("REMOVE");
                cmds.add(Integer.toString(ids.get(temp)));
                cmds.add("FALSE");
            }
        }
    }

    public static void main(String[] args)
    {
        HashSet<String> perms = permutations(10);
        HashSet<String> struct = new HashSet<String>();
        ArrayList<String> cmds = new ArrayList<String>();
        add(perms, struct, 5, cmds);
        containSuccess(perms, struct, 3, cmds);
        containFail(perms, struct, 3, cmds);
        removeSuccess(perms, struct, 3, cmds);
        removeFail(perms, struct, 3, cmds);
        System.out.println(cmds.size());
        System.out.println(perms.size());
        for (int i = 0; i < perms.size(); i++)
        {
            System.out.println(strs.get(i));
        }
        for (int i = 0; i < cmds.size(); i++)
        {
            System.out.println(cmds.get(i));
        }
    }
}
