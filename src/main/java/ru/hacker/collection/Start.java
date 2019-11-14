package ru.hacker.collection;

import java.util.LinkedList;

public class Start {

  public static void main(String[] args) {

    HashMapCustom<Car, Integer> map = new HashMapCustom<>();
for (int i=0; i<5_000_000;i++) {
  map.put(new Car(String.valueOf(i * 2), i), i * 3);
}

int collisions=0;
int depth=0;
for (int i=0;i<map.massiv.length;i++)
{
  if (map.massiv[i]!=null)
  {
   LinkedList item=(LinkedList)map.massiv[i];
    int n=item.size();
    if (n>1)
    {
      collisions+=n-1;
      if ((n-1)>depth) depth=n-1;
    }

  }
}
    System.out.println("Map elements="+map.size);
    System.out.println("Collisisons - "+collisions);
    System.out.println("Max colisisons per element - "+depth);
  }

}


