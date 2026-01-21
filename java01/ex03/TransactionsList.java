
import java.util.UUID;

public interface TransactionsList {
    public void add(Transaction user);
    public Transaction removeById(UUID id);
    public Transaction[] toArray();
}
