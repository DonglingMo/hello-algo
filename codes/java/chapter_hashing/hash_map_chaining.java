/**
 * File: hash_map_chaining.java
 * Created Time: 2023-06-13
 * Author: krahets (krahets@163.com)
 */

package chapter_hashing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* 链式地址哈希表 */
class HashMapChaining {
    int size; // 键值对数量
    int capacity; // 哈希表容量
    double loadThres; // 触发扩容的负载因子阈值
    int extendRatio; // 扩容倍数
    List<List<Pair>> buckets; // 桶数组
    private Pair head, tail; // 维护双向链表头尾节点

    private static class Pair {
        int key;
        String val;
        Pair next;
        Pair prev;

        public Pair(int key, String val) {
            this.key = key;
            this.val = val;
        }
    }

    /* 构造方法 */
    public HashMapChaining() {
        size = 0;
        capacity = 4;
        loadThres = 2.0 / 3.0;
        extendRatio = 2;
        buckets = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            buckets.add(new ArrayList<>());
        }
        head = null;
        tail = null;
    }

    /* 哈希函数 */
    int hashFunc(int key) {
        return key % capacity;
    }

    /* 负载因子 */
    double loadFactor() {
        return (double) size / capacity;
    }

    /* 查询操作 */
    String get(int key) {
        int index = hashFunc(key);
        List<Pair> bucket = buckets.get(index);
        // 遍历桶，若找到 key ，则返回对应 val
        for (Pair pair : bucket) {
            if (pair.key == key) {
                return pair.val;
            }
        }
        // 若未找到 key ，则返回 null
        return null;
    }

    /* 添加操作 */
    void put(int key, String val) {
        // 当负载因子超过阈值时，执行扩容
        if (loadFactor() > loadThres) {
            extend();
        }
        int index = hashFunc(key);
        List<Pair> bucket = buckets.get(index);
        // 遍历桶，若遇到指定 key ，则更新对应 val 并返回
        for (Pair pair : bucket) {
            if (pair.key == key) {
                pair.val = val;
                return;
            }
        }
        // 若无该 key ，则将键值对添加至尾部
        Pair pair = new Pair(key, val);
        bucket.add(pair);
        if (tail == null) {
            head = tail = pair;
        } else {
            tail.next = pair;
            pair.prev = tail;
            tail = pair;
        }
        size++;
    }

    /* 删除操作 */
    void remove(int key) {
        int index = hashFunc(key);
        List<Pair> bucket = buckets.get(index);
        // 遍历桶，从中删除键值对
        Iterator<Pair> iterator = bucket.iterator();
        while (iterator.hasNext()) {
            Pair pair = iterator.next();
            if (pair.key == key) {
                iterator.remove();
                size--;

                // 维护链表
                if (pair.prev != null) {
                    pair.prev.next = pair.next;
                } else {
                    head = pair.next;
                }
                if (pair.next != null) {
                    pair.next.prev = pair.prev;
                } else {
                    tail = pair.prev;
                }
                return;
            }
        }
    }

    /* 扩容哈希表 */
    void extend() {
        // 初始化扩容后的新哈希表
        capacity *= extendRatio;
        buckets = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            buckets.add(new ArrayList<>());
        }
        Pair current = head;
        while (current != null) {
            List<Pair> bucket = buckets.get(hashFunc(current.key));
            bucket.add(current);
            current = current.next;
        }
    }

    /* 打印哈希表 */
    void print() {
        Pair current = head;
        while (current != null) {
            System.out.print(current.key + " -> ");
            current = current.next;
        }
        System.out.println("null");
    }
}

public class hash_map_chaining {
    public static void main(String[] args) {
        /* 初始化哈希表 */
        HashMapChaining map = new HashMapChaining();

        /* 添加操作 */
        // 在哈希表中添加键值对 (key, value)
        map.put(12836, "小哈");
        map.put(15937, "小啰");
        map.put(16750, "小算");
        map.put(13276, "小法");
        map.put(10583, "小鸭");
        System.out.println("\n添加完成后，哈希表为\nKey -> Value");
        map.print();

        /* 查询操作 */
        // 向哈希表中输入键 key ，得到值 value
        String name = map.get(13276);
        System.out.println("\n输入学号 13276 ，查询到姓名 " + name);

        /* 删除操作 */
        // 在哈希表中删除键值对 (key, value)
        map.remove(12836);
        System.out.println("\n删除 12836 后，哈希表为\nKey -> Value");
        map.print();
    }
}
