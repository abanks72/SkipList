// Sincere Banks

import java.util.*;

class Node<T extends Comparable<T>>
{
  ArrayList<Node<T>> next;
  int height;
  T data;

  Node(int height)
  {
    this.next = new ArrayList<>();
    this.height = height;

    for(int i = 0; i<height;i++)
    {
      next.add(i, null);
    }

    data = null;
  }

  Node(T data, int height)
  {
    this.next = new ArrayList<>();
    this.height = height;
    this.data = data;

    for(int i = 0; i<height;i++)
    {
      next.add(i, null);
    }
  }

  public T value()
  {
    return data;
  }

  public int height()
  {
    return height;
  }

  public Node<T> next(int level)
  {
      if ((level > (height - 1)) || (level < 0))
        return null;

      return next.get(level);
  }

  public void setNext(int level, Node<T> node)
  {
    next.set(level,node);
  }

  public void grow()
  {
    next.add(null);
    height++;
  }

  public boolean maybeGrow()
  {
    Random num = new Random();
    if(num.nextBoolean())
    {
      grow();
      return true;
    }

    return false;
  }

  public void trim(int height)
  {
    while(this.height > height)
    {
      next.remove(this.height-1);
      height--;
    }
  }
}

public class SkipList<T extends Comparable<T>> {

    public Node<T> root;
    public int height;
    public int size;

    int count = 0;

  // Constructors
  SkipList()
  {
    this.size = 0;
    this.height = 1;
    root = new Node<>(1);
  }

  SkipList(int height)
  {
    this.size = 0;
    this.height = height;
    root = new Node<>(height);
  }

  //
   public int size()
   {
     return size;
   }

   public int height()
   {
     return height;
   }

   public Node<T> head()
   {
     return root;
   }

   private static int generateRandomHeight(int maxHeight)
   {
     Random num = new Random();
     int i = 1;

     while(i < maxHeight)
     {
       if(num.nextInt()%2 == 0)
          return i;
        i++;
     }

     return i;
   }

   private void growSkipList()
   {
     ArrayList<Node<T>> inc = new ArrayList<>();
     Node<T> temp = this.root.next(height-1);

     for (; temp != null;)
     {
       if(temp.maybeGrow())
        inc.add(temp);

       temp = temp.next(height-1);
      }

     root.grow();
     inc.add(0,root);

     this.height++;
   }

   private void trimSkipList(int height)
   {
     Node<T> blank = this.root;
     int top = height - 1;

     for (; blank != null;)
     {
       blank.trim(height);
       blank = blank.next(top);
     }

     this.height = height;
   }

   public void insert(T data)
   {
     ArrayList<Node<T>> trail = new ArrayList<>();
     Node<T> temp = this.root;

     int max;
     double ceiling;

     ceiling = Math.log10(this.size+1)/Math.log10(2);


     if (Math.ceil(ceiling) > 0)
      max = (int)Math.ceil(ceiling);
    else
      max = 1;


     if (max > this.height)
     {
       growSkipList();
     }

     int level = this.height-1;
     Node<T> aye = new Node<>(data, generateRandomHeight(this.height));

     while(level >=0)
     {
       if(temp.next(level) == null || data.compareTo(temp.next(level).value()) <= 0)
       {
         trail.add(temp);
         level--;
       }
       else
       {
         temp = temp.next(level);
       }
     }

     ArrayList<Node<T>> trail2 = new ArrayList<>();

     for (int i = trail.size()-1; i >= 0; i--)
     {
       trail2.add(trail.get(i));
     }


     for(int i = 0; i < aye.height(); i++)
     {
       aye.setNext(i, trail2.get(i).next(i));
       trail2.get(i).setNext(i, aye);
     }
     this.size++;
   }

   public void insert(T data, int height)
   {
     ArrayList<Node<T>> trail = new ArrayList<>();
     Node<T> temp = this.root;

     int max;
     double ceiling;

     ceiling = Math.log10(this.size+1)/Math.log10(2);


     if (Math.ceil(ceiling) > 0)
      max = (int)Math.ceil(ceiling);
    else
      max = 1;


     if (max > this.height)
     {
       growSkipList();
     }

     int level = this.height-1;
     Node<T> aye = new Node<>(data, height);

     while(level >=0)
     {
       if(temp.next(level) == null || data.compareTo(temp.next(level).value()) <= 0)
       {
         trail.add(temp);
         level--;
       }
       else
       {
         temp = temp.next(level);
       }
     }

     ArrayList<Node<T>> trail2 = new ArrayList<>();

     for (int i = trail.size()-1; i >= 0; i--)
     {
       trail2.add(trail.get(i));
     }


     for(int i = 0; i < aye.height(); i++)
     {
       aye.setNext(i, trail2.get(i).next(i));
       trail2.get(i).setNext(i, aye);
     }
     this.size++;
   }

   public void delete(T data)
   {
     ArrayList<Node<T>> breadcrumbs = new ArrayList<>();
     Node<T> temp = this.root;
     Node<T> temp2;
     boolean flag = false;

     int max;
     double ceiling;

     ceiling = Math.log10(this.size-1)/Math.log10(2);


     if (Math.ceil(ceiling) > 0)
      max = (int)Math.ceil(ceiling);
    else
      max = 1;

     int level = this.height-1;

     while(level >=0)
     {
       if(temp.next(level) == null || data.compareTo(temp.next(level).value()) < 0)
       {
         breadcrumbs.add(temp);
         level--;
       }
       else if(temp.next(level).value().compareTo(data) == 0 && level > 0)
       {
        breadcrumbs.add(temp);
        level--;
       }
       else if(temp.next(level).value().compareTo(data) == 0)
       {
         breadcrumbs.add(temp);
         temp = temp.next(level);
         flag = true;
         level--;
         continue;
       }
       else
       {
         temp = temp.next(level);
       }
     }
     Collections.reverse(breadcrumbs);

     if(flag)
     {
       for(int i = 0; i < temp.height(); i++)
       {
         breadcrumbs.get(i).setNext(i, temp.next(i));
       }
       this.size--;
     }

     if(max < this.height && flag)
     {
       ceiling = Math.log10(this.size)/Math.log10(2);
       max = (int)Math.ceil(ceiling);
       trimSkipList(max);
     }

   }

   public boolean contains(T data)
   {
     Node<T> blank = this.root;
     int numb = height-1;

     for (; numb >= 0;)
     {
       if (data.compareTo(blank.next(numb).value()) < 0 || blank.next(numb) == null)
        numb--;
       else if (data.compareTo(blank.next(numb).value()) == 0)
        return true;
       else
        blank = blank.next(numb);
     }

     return false;
   }

   public Node<T> get(T data)
   {
     Node<T> blank = this.root;
     int numb = height-1;

     while(numb >= 0)
     {
       if(blank.next(numb) == null || data.compareTo(blank.next(numb).value()) == 0)
        numb--;
       else if(data.compareTo(blank.next(numb).value()) == 0){
        blank = blank.next(numb);
        return blank;
      }
       else
        blank = blank.next(numb);
     }

     return null;
   }

   public static double difficultyRating()
   {
     return 5;
   }

   public static double hoursSpent()
   {
     return 27;
   }
}
