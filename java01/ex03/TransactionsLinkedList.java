import java.util.UUID;

public class TransactionsLinkedList implements TransactionsList {
    private int size = 0;
    private TransactionNode first = null;
    private TransactionNode last = null;

    private static class TransactionNode {
        Transaction item;
        TransactionNode next;
        TransactionNode prev;

        TransactionNode(Transaction item, TransactionNode next, TransactionNode prev) {
            this.item = item;
            this.next = next;
            this.prev = prev;
        }
    }

    @Override
    public void add(Transaction user) {
        TransactionNode node = new TransactionNode(user, null, last);
        if (last != null) {
            node.prev = last;
            last.next = node;
        }
        else {
            first = node;
        }
        last = node;
        this.size++;
    }

    @Override
    public Transaction removeById(UUID id) {
        TransactionNode iter = first;
        while (iter != null) {
            if (iter.item.getId().equals(id)) {
                if (iter.prev != null) {
                    iter.prev.next = iter.next;
                } else {
                    first = iter.next;
                }

                if (iter.next != null) {
                    iter.next.prev = iter.prev;
                } else {
                    last = iter.prev;
                }
                size--;
                return iter.item;
            }
            iter = iter.next;
        }
        throw new TransactionNotFoundException();
    }

    @Override
    public Transaction[] toArray() {
        Transaction[] result = new Transaction[size];

        int index = 0;
        TransactionNode iter = first;
        while (iter != null) {
            result[index++] = iter.item;
            iter = iter.next;
        }
        return result;
    }
}
