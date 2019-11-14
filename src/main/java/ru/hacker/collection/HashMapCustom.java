package ru.hacker.collection;

import java.util.LinkedList;
import java.util.Objects;

public class HashMapCustom<E, T>  {

  //TODO: необходимо сделать динамическое расширение массива при достижении 75% его заполненности
  //т.е. у нас 16 элементов по умолчанию, как только мы заполнили его на 12 элементов, увеличиваем
  //массив в 2 раза. А потом пробегаемся по всем уже заполненным индексам и заново пересчитываем
  //индексы с учетом новой размерности.
  //Задание со звездочкой: сделать разный loadFactor - а именно указывать не только 75%, а любое другое
  // коэффициент загрузки в виде float - 0.75f; добавьте Generics.
  public float loadFactor;
  public Object[] massiv;
  public int size=0;

  public HashMapCustom(int capacity, float loadFactor)
        {
          massiv=new Object[capacity];
          this.loadFactor=loadFactor;
        }
  public HashMapCustom(int capacity)
  {
   this(capacity, 0.75f);
  }
  public HashMapCustom()
  {
    this(16, 0.75f);
  }

  class Entry<E, T> {

    // Сущность, которая хранится в нашей корзине (buckets). Как было сказано, в корзине может быть несколько сущностей из-за коллизии
    public Entry(E key, T value) {
      this.key = key;
      this.value = value;
    }

    E key;//это Car
    T value;// это какое-то значение, привязанное к Car
  }

  public void put(E key, T value) {
    int index = hash(key);
    LinkedList<Entry> entries = (LinkedList<Entry>)massiv[index];
    if (entries == null) {
      Entry<E,T> entry = new Entry(key, value);
      entries = new LinkedList<>();
      entries.add(entry);
      massiv[index] =
          entries;//один элемент массива - это одна корзина, в которой может хранится несколько Entry из-за коллизий, поэтому, чтобы не потерять значения, используем связанный список, для
      //хранения всех ключей, у которых, к сожалению, совпали значения хешей (но по-хорошему, пусть хеши и совпали, но equals должен на таких ключах давать false, иначе все теряет смысл)
    } else {
      Entry<E,T> entry = new Entry(key, value);
      entries.add(entry);
    }
    size++;
    if (size>massiv.length*loadFactor) resize();
  }

private void resize()
{
  size=0;
  LinkedList[] tmp=new LinkedList[massiv.length*2+1];
  System.arraycopy(massiv,0,tmp,0,massiv.length);
  massiv=new Object[massiv.length*2+1];
  for (int i=0;i<tmp.length;i++)
  {
    if (tmp[i]!=null)
    {
      for (int j=0;j<tmp[i].size();j++)
      {
        Entry<E,T> item=(Entry<E,T>)tmp[i].get(j);
        put(item.key, item.value);
      }
    }
  }
}

  private int hash(E key) {//вся магия хеширования по сути здесь, получить индекс в массиве и все, счастье близко. Если бы не было коллизий полностью, было бы идеально.
    int h = key.hashCode();
    int index = (h & 0x7fff_ffff)
        % massiv.length;// это маскирование знакового бита (чтобы превратить 32-битное число в неотрицательное 31-битное), а затем как в модульном хешировании, вычисляем остаток от деления;
    //при таком подходе в качестве размера хеш-таблицы берут ПРОСТОЕ число, тогда задействуются все биты хеш-кода и мы как-то минимизируем коллизию
    return index;
  }


  public T get(E key) {
    int index = hash(key);
    LinkedList<Entry> entries = (LinkedList<Entry>)massiv[index];
    if (entries != null && entries.size()
        == 1) {//идеальная ситуация, коллизий нет, в корзине один элемент - просто берем его из связанного списка и все.
      return (T)entries.get(0).value;
    } else if (entries != null) {//не очень хорошо, у нас коллизия, нужно что-то делать
      for (int i = 0; i < entries.size(); i++) {
        Entry<E,T> entry = entries.get(i);
        if (entry.key.equals(key)) {
          return entry.value;
        }
      }
    }
    return null;
  }
}
