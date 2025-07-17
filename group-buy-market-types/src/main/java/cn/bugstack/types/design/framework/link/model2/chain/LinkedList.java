package cn.bugstack.types.design.framework.link.model2.chain;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 功能链路
 * @create 2025-01-18 09:31
 */
public class LinkedList<E> implements ILink<E> {

    // 自定义双向链表实现，用于责任链模式模型，使用Node<E>内部类作为节点

    /**
     * 责任链名称
     */
    private final String name;

    transient int size = 0;

    transient Node<E> first;

    transient Node<E> last;

    public LinkedList(String name) {
        this.name = name;
    }

    // 头插节点
    void linkFirst(E e) {
        final Node<E> f = first;
        final Node<E> newNode = new Node<>(null, e, f);
        first = newNode;
        if (f == null)
            last = newNode;
        else
            f.prev = newNode;
        size++;
    }

    // 尾插节点
    void linkLast(E e) {
        final Node<E> l = last;
        final Node<E> newNode = new Node<>(l, e, null);
        last = newNode;
        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }
        size++;
    }

    @Override
    public boolean add(E e) {
        linkLast(e);
        return true;
    }

    @Override
    public boolean addFirst(E e) {
        linkFirst(e);
        return true;
    }

    @Override
    public boolean addLast(E e) {
        linkLast(e);
        return true;
    }

    // 根据元素值删除节点，支持 null 值删除
    @Override
    public boolean remove(Object o) {
        if (o == null) {
            for (Node<E> x = first; x != null; x = x.next) {
                if (x.item == null) {
                    unlink(x);
                    return true;
                }
            }
        } else {
            for (Node<E> x = first; x != null; x = x.next) {
                if (o.equals(x.item)) {
                    unlink(x);
                    return true;
                }
            }
        }
        return false;
    }

    // 删除指定节点，处理前后节点的连接关系
    E unlink(Node<E> x) {
        final E element = x.item;
        final Node<E> next = x.next;
        final Node<E> prev = x.prev;

        if (prev == null) {
            first = next; // 当前是头节点
        } else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) {
            last = prev; //当前是尾节点
        } else {
            next.prev = prev;
            x.next = null;
        }

        x.item = null; // 制空
        size--;
        return element;
    }

    // 根据索引获取元素
    @Override
    public E get(int index) {
        return node(index).item;
    }

    // 根据索引获取节点，使用二分查找优化（从中间开始向两端搜索）
    Node<E> node(int index) {
        if (index < (size >> 1)) {
            Node<E> x = first;
            for (int i = 0; i < index; i++)
                x = x.next;
            return x;
        } else {
            Node<E> x = last;
            for (int i = size - 1; i > index; i--)
                x = x.prev;
            return x;
        }
    }

    // 打印链表信息，显示头节点、尾节点和所有元素
    public void printLinkList() {
        if (this.size == 0) {
            System.out.println("链表为空");
        } else {
            Node<E> temp = first;
            System.out.print("目前的列表，头节点：" + first.item + " 尾节点：" + last.item + " 整体：");
            while (temp != null) {
                System.out.print(temp.item + "，");
                temp = temp.next;
            }
            System.out.println();
        }
    }

    // 通用node节点
    protected static class Node<E> {

        E item;
        Node<E> next;
        Node<E> prev;

        public Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }

    }

    public String getName() {
        return name;
    }

}
