package org.example;

import java.util.function.BinaryOperator;
import java.util.function.Supplier;

/**
 * Hello world!
 *
 */

//Answers range queries - Given range r (l,r) - > Finds max between that range in arr
public class SegmentTree
{
    private static int[] arr = {8,2,5,1,4,5,3,9,6,10};
    private static int n = arr.length;
    private static int[] segmentTree = new int[4*n];

    private static BinaryOperator<Integer> op;

    private static Supplier<Integer> identity;


    public static void main( String[] args )
    {
        int i;
        BinaryOperator<Integer> max = (a,b)->Math.max(a,b);
        BinaryOperator<Integer> sum = (a,b)->a+b;
        /*op=max;
        identity = ()->Integer.MIN_VALUE;

        build(0,0,n-1);
        int[][] queries = {{1,2},{0,9},{3,8},{3,6},{0,1}};
        for(int[] query:queries) {
            int l = query[0];
            int r = query[1];
            int res = query(0,l,r,0,n-1);
            System.out.println(String.format("max in range [%d,%d] is %d", l, r,res));
        }*/

        op = sum;
        identity = ()->0;

        build(0,0,n-1);
        int[][] queries2 = {{1,2},{0,9},{3,8},{3,6},{0,1}};
        int[][] updates = {{3,0}};
        for(int[] update:updates) {
            int pos = update[0];
            int val=update[1];
            int res = query(0,0,n-1,0,n-1);
            System.out.println(String.format("Sum in range [0,n-1] is %d before update",res));
            update(pos,val,0,0,n-1);
             res = query(0,0,n-1,0,n-1);
            System.out.println(String.format("Sum in range [0,n-1] is %d after update",res));

        }
        for(int[] query:queries2) {
            int l = query[0];
            int r = query[1];
            int res = query(0,l,r,0,n-1);
            System.out.println(String.format("Sum in range [%d,%d] is %d", l, r,res));
        }



    }

    private static void update(int pos, int val, int index, int st, int end) {
        if(st==end&&st==pos) {
            arr[st]=val;
            segmentTree[index] = val;
            return;
        }
        if(pos>end||pos<st) {
            return;
        }
        int mid = st+(end-st)/2;
        if(pos<=mid&&pos>=st) {
            update(pos,val,2*index+1,st,mid);
        } else {
            update(pos,val,2*index+2,mid+1,end);
        }
        segmentTree[index] = op.apply(segmentTree[2*index+1],segmentTree[2*index+2] );

    }

    private static int query(int i,int l, int r, int st, int end) {
       // System.out.println(st+","+end+" - "+l+","+r);
        if(st>end) {
            return identity.get();
        }
        //Case 1: If st,end is inside l,r - just pick val
        if(st>=l&&end<=r) {
            return segmentTree[i];
        }
        //Case 2: If st,end is not in range of l,r
        if(end<l||st>r) {
            return identity.get();
        }
        int mid = st+(end-st)/2;
        int left = query(2*i+1,l,r,st,mid);
        int right = query(2*i+2,l,r,mid+1,end);
        return op.apply(left,right);

    }

    private static void build(int segmentTreeIndex, int st, int end) {
        if(st==end) {
         //   System.out.println("st,end,index"+" "+st+","+end+","+segmentTreeIndex);
            segmentTree[segmentTreeIndex] = arr[st];
            return;
        }
      //  System.out.println(segmentTreeIndex+",,"+st+":"+end);
        int mid = st+(end-st)/2;
        build(2*segmentTreeIndex+1,st,mid);
        build(2*segmentTreeIndex+2,mid+1,end);;
      //  System.out.println((2*segmentTreeIndex+1)+"&&"+(2*segmentTreeIndex+2));

      //  System.out.println(segmentTree[(2*segmentTreeIndex+1)]+"&&$"+segmentTree[(2*segmentTreeIndex+2)]);
        segmentTree[segmentTreeIndex] = op.apply(segmentTree[2*segmentTreeIndex+1], segmentTree[2*segmentTreeIndex+2]);
    }
}
