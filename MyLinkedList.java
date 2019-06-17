

import java.util.Scanner;

public class MyLinkedList<AnyType> implements Iterable<AnyType>
{
    private static class Node<AnyType> //nested Node generic class
    {
        public Node( AnyType d, Node<AnyType> p, Node<AnyType> n )//constructor of Node class
        {
            data = d; prev = p; next = n;
        }

        //fields of Node class
        public AnyType data;
        public Node<AnyType> prev;
        public Node<AnyType> next;
    }

    public MyLinkedList( ) //constructor of MyLinkedList class
    {
        doClear( );
    }

    public void clear( ) //clear routine
    {
        doClear( );
    }

    private void doClear( )//doClear routine
        {
        beginMarker = new Node<AnyType>( null, null, null );
        endMarker = new Node<AnyType>( null, beginMarker, null );
        beginMarker.next = endMarker;

        theSize = 0;
        modCount++;
        }


    public int size( )//size routine
    {
        return theSize;
    }

    public boolean isEmpty( )//isEmpty routine
    {
        return size( ) == 0;
    }

    public boolean add( AnyType x )//routine for adding to the end - calls other add method
    {
        add( size( ), x );
        return true;
    }

    public void add( int idx, AnyType x )//routine for adding at a specific index - calls addBefore method
    {
        addBefore( getNode( idx, 0, size( ) ), x );
    }

    public AnyType get( int idx )//for getting data at a specific index
    {
        return getNode( idx ).data;
    }

    public AnyType set( int idx, AnyType newVal )//modifying data at a specific index
    {
        Node<AnyType> p = getNode( idx );
        AnyType oldVal = p.data;
        p.data = newVal;
        return oldVal;
    }

    public AnyType remove( int idx )//removing the indexed node
    {
        return remove( getNode( idx ) );
    }

    private void addBefore( Node<AnyType> p, AnyType x )//add a new node before node p
    {
        Node<AnyType> newNode = new Node<>( x, p.prev, p );
        newNode.prev.next = newNode;
        p.prev = newNode;
        theSize++;
        modCount++;
    }

    private AnyType remove( Node<AnyType> p ) //removing node p
    {
        p.next.prev = p.prev;
        p.prev.next = p.next;
        theSize--;
        modCount++;

        return p.data;
    }


    public void swap(int idx1, int idx2)
    {
        if(idx1>theSize || idx2>theSize )
        {
            throw new IndexOutOfBoundsException( );
        }

        if(idx2<idx1)//making sure idx1 has the lower index
        {
            int temp = idx2;
            idx2 = idx1;
            idx1 = temp;
        }

        Node<AnyType> p=getNode(idx1);
        Node<AnyType> q=getNode(idx2);


        if(idx2-idx1==1)//special case when the indexes are one after the other
        {
            p.next=q.next;
            q.prev=p.prev;
            p.prev.next=q;
            q.next.prev=p;
            q.next=p;
            p.prev=q;
        }
        else {
            //storing q's neighbours to a temporary variable
            Node<AnyType> tempprev = q.prev;
            Node<AnyType> tempnext = q.next;

            //changing references around p
            p.prev.next = q;
            q.prev = p.prev;
            q.next = p.next;
            p.next.prev = q;

            //changing references around where q used to be
            tempprev.next = p;
            p.prev = tempprev;
            p.next = tempnext;
            tempnext.prev = p;
        }

        modCount++;
    }

    public void shift(int k)  //k is the number of items to be shifted
    {
        if((k*k)>=(theSize*theSize))//cancelling out shifts through the entire list
        {
            k=k%theSize;
        }

        if(k==0){}//do nothing
        else {
        //linking the first and last nodes
        beginMarker.next.prev=endMarker.prev;
        endMarker.prev.next=beginMarker.next;
        Node<AnyType> p;
        Node<AnyType> q;

            if (k < 0) {
                p = getNode(-k);
                q = getNode(-k - 1);
            } else {
                p = getNode(theSize - k);
                q = getNode(theSize - k - 1);
            }
            //making reference changes only at the beginning and end nodes
            beginMarker.next = p;
            p.prev = beginMarker;
            q.next = endMarker;
            endMarker.prev = q;

        }
        modCount++;//since changes have been made to the list
    }

    public void erase(int idx, int k) //remove k elements starting from index idx
    {
        if(idx+k>theSize)
        {
            throw new IndexOutOfBoundsException( );
        }

        java.util.Iterator itr= MyLinkedList.this.iterator();

        for(int i=0;i<=idx;i++)//since after 1st next(), iterator is at the first node only
        {
            itr.next();
        }
        //now the current position of iterator is the one at index idx

        for(int i=0;i<k;i++)
        {
            itr.remove();//since we are using itr.remove(), iterator is not invalidated
            if (itr.hasNext())
            {
            itr.next();
            }

        }


        modCount++;  //since k elements removed
        theSize-=k;   //since k elements removed
    }



    public void insertList(MyLinkedList<AnyType> lst, int idx)
    {
        //MyLinkedList.this is ours & lst is the new one
        if(idx>theSize)
        {
            throw new IndexOutOfBoundsException( );
        }

        int length=0;
        java.util.Iterator itr1=lst.iterator();
        while(itr1.hasNext())
        {
            length++;
            itr1.next();
        }

        //now we have the length of the new linked list we received

        java.util.Iterator itr2=lst.iterator();
        for(int i=0; i<length; i++)
        {
            MyLinkedList.this.add(idx, ((AnyType) itr2.next()));
            idx++;
        }

    }



    //getNode method overloading
    private Node<AnyType> getNode( int idx )
    {
        return getNode( idx, 0, size( ) - 1 );
    }

    private Node<AnyType> getNode( int idx, int lower, int upper )
    {
        Node<AnyType> p;

        if( idx < lower || idx > upper )
            throw new IndexOutOfBoundsException( );

        if( idx < size( ) / 2 )
        {
            p = beginMarker.next;
            for( int i = 0; i < idx; i++ )
                p = p.next;
        }

        else
        {
            p = endMarker;
            for( int i = size( ); i > idx; i-- )
            p = p.prev;
        }

        return p;
    }


    public java.util.Iterator<AnyType> iterator( )//iterator method declaration
    {
        return new LinkedListIterator( );
    }

    private class LinkedListIterator implements java.util.Iterator<AnyType>//inner iterator class
    {
        private Node<AnyType> current = beginMarker.next;
        private int expectedModCount = modCount;
        private boolean okToRemove = false;

        public boolean hasNext( )
        {
            return current != endMarker;
        }

        public AnyType next( )
        {
            if( modCount != expectedModCount )
                throw new java.util.ConcurrentModificationException( );
            if( !hasNext( ) )
                throw new java.util.NoSuchElementException( );

            AnyType nextItem = current.data;
            current = current.next;
            okToRemove = true;
            return nextItem;
        }

        public void remove( )
        {
            if( modCount != expectedModCount )
                throw new java.util.ConcurrentModificationException( );
            if( !okToRemove )
                throw new IllegalStateException( );

            MyLinkedList.this.remove( current.prev );
            expectedModCount++; /*because MyLinkedList's remove method will inrement ModCount and we don't want
            iterator's remove method to invalidate the iterator */
            okToRemove = false;
        }

    }

    private int theSize;
    private int modCount = 0;
    private Node<AnyType> beginMarker;
    private Node<AnyType> endMarker;

    public static void main (String args[])
   {

       MyLinkedList<Integer> lst=new MyLinkedList<Integer>();
       Scanner s = new Scanner(System.in);


       System.out.println("Enter the linked list with spaces in between elements: ");
       String input = s.nextLine();
       String[] array1 = input.split("\\s+");//reading numbers with spaces in between

       for(int i=0; i<array1.length; i++)
       {
           lst.add(Integer.parseInt(array1[i]));
       }

       System.out.print("The Linked List you entered: ");
       java.util.Iterator itr3=lst.iterator();
       while(itr3.hasNext())
       {
           System.out.print(itr3.next()+" ");
       }

       System.out.println();


       System.out.println("Enter the indexes you want to swap with space in between");
       String input1 = s.nextLine();
       String[] array2 = input1.split("\\s+");//reading numbers with spaces in between

       int idx1=Integer.parseInt(array2[0]);
       int idx2=Integer.parseInt(array2[1]);

       lst.swap(idx1,idx2);
       System.out.print("The Linked List after using swap("+idx1+","+idx2+"): ");
       java.util.Iterator itr4=lst.iterator();
       while(itr4.hasNext())
       {
           System.out.print(itr4.next()+" ");
       }

       System.out.println();


       System.out.println("Enter the number of places (+ve/-ve) the list is to be shifted : ");
       String n = s.nextLine();

       lst.shift(Integer.parseInt(n));
       System.out.print("The Linked List after using shift("+n+"): ");
       java.util.Iterator itr5=lst.iterator();
       while(itr5.hasNext())
       {
           System.out.print(itr5.next()+" ");
       }



       System.out.println();



       MyLinkedList<Integer> lst1=new MyLinkedList<Integer>();

       System.out.println("Enter the linked list to be inserted with spaces in between elements: ");
       String input2 = s.nextLine();
       String[] array3 = input2.split("\\s+");//reading numbers with spaces in between

       for(int i=0; i<array3.length; i++)
       {
           lst1.add(Integer.parseInt(array3[i]));
       }


       System.out.print("The Linked List you entered: ");

       java.util.Iterator itr7=lst1.iterator();
       while(itr7.hasNext())
       {
           System.out.print(itr7.next()+" ");
       }

       System.out.println();
       System.out.println("At which index do you want to insert this list");
       int v=s.nextInt();
       lst.insertList(lst1, v);

       System.out.print("The Linked List after inserting the new list at index "+v+": ");
       java.util.Iterator itr8=lst.iterator();
       while(itr8.hasNext())
       {
           System.out.print(itr8.next()+" ");
       }

       System.out.println();


       System.out.println("Enter the starting index and number of items to be erased with space in between: ");


       String input4="";
       while ((input4.equals(""))) {
           input4 = s.nextLine();
       }
       String[] array4 = input4.split("\\s+");//reading numbers with spaces in between
       System.out.println(array4[0]);
       int index=Integer.parseInt(array4[0]);
       int num=Integer.parseInt(array4[1]);

       lst.erase(index,num);
       System.out.print("The Linked List after using erase("+index+","+num+"): ");
       java.util.Iterator itr6=lst.iterator();
       while(itr6.hasNext())
       {
           System.out.print(itr6.next()+" ");
       }

   }
}
