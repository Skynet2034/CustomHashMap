package ru.hacker.collection;

import java.util.LinkedList;
import java.util.Objects;

public class HashMapCustom {

  public static final int M = 6_999_999;

  public Object[] massiv = new Object[M];

  private class Entry {

    // Сущность, которая хранится в нашей корзине (buckets). Как было сказано, в корзине может быть несколько сущностей из-за коллизии
    public Entry(Object key, Object value) {
      this.key = key;
      this.value = value;
    }

    Object key;
    Object value;
  }

  public void put(Object key, String value) {
    int index = hash(key);
      //TODO: добавить проверки, что если по этому индексу есть уже элемент (словили коллизию),
    //TODO: то просто добавить новый ключ в уже имеющийся список
    Entry entry = new Entry(key, value);
    LinkedList<Entry> entries;
    if (massiv[index]!=null)
    {
      entries=(LinkedList<Entry>) massiv[index];
      entries.add(entry);
    }
    else
    {
    entries = new LinkedList<>();
    entries.add(entry);}
    massiv[index] = entries;
    //один элемент массива - это одна корзина, в которой может хранится несколько Entry из-за коллизий, поэтому, чтобы не потерять значения, используем связанный список, для
    //хранения всех ключей, у которых, к сожалению, совпали значения хешей (но по-хорошему, пусть хеши и совпали, но equals должен на таких ключах давать false, иначе все теряет смысл)
  }

  /**
   * Поскольку нам нужен индекс массива, а не 32-битное число, мы в наших реализациях
   * объединяем вызов hashCode() с модульным хешированием, которые нам дает целые числа от 0 до М-1.
   * В идеале, пользовательская функция hashCode() должна равномерно распределять ключи на все 32-битные значения
   * результата.
   * То есть для любого объекта можно записать x.hashCode() и в принципе ожидать, что с одинаковой вероятностью  будет
   * возвращено одно из 2^32 значений.
   * РЕАЛИЗАЦИЯ в Java для типов String, Integer, Double, File, URL придерживается этого соглашения. Но для собственного
   * типа стоит делать это самостоятельно.
   *
   * @param key
   *
   * @return index массива
   */
  private int hash(Object key) {//вся магия хеширования по сути здесь, получить индекс в массиве и все, счастье близко. Если бы не было коллизий полностью, было бы идеально.
    int h = Objects.hashCode(key);
    int index = (h & 0x7fff_ffff)
        % M;// это маскирование знакового бита (чтобы превратить 32-битное число в неотрицательное 31-битное), а затем как в модульном хешировании, вычисляем остаток от деления;
    //при таком подходе в качестве размера хеш-таблицы берут ПРОСТОЕ число, тогда задействуются все биты хеш-кода и мы как-то минимизируем коллизию
    return index;
  }

  /**
   * Получить объект по ключу
   *
   * @param key
   *
   * @return
   */
  public Object get(Object key) {
    int index = hash(key);
    LinkedList<Entry> entries = (LinkedList<Entry>)massiv[index];
    if (entries.size() == 1) {//идеальная ситуация, коллизий нет, в корзине один элемент - просто берем его из связанного списка и все.
      return entries.get(0).value;
    } else {//не очень хорошо, у нас коллизия, нужно что-то делать
      int found=0;
      Entry e=entries.get(0);
      while (!e.key.equals(key))
      {
        found++;
        e=entries.get(found);
      }
      return entries.get(found).value;
      // TODO: подумайте как здесь сделать поиск нужного ключа, если в процессе добавления у нас возникли коллизии и в одной корзине более одного ключа со своим собственным значением
      // вспомните про контракт поиска по хешу - сначала использует hashCode, потом equals!
      }
  }
}
