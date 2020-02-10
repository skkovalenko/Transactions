import java.util.concurrent.atomic.AtomicLong;

public class Account implements Comparable<Account>
{
    private AtomicLong money = new AtomicLong();
    private String accNumber;
    private boolean block;

    public Account(long money, String accNumber) {
        this.money.set(money);
        this.accNumber = accNumber;
        block = false;
    }

    public String getAccNumber() {
        return accNumber;
    }

    public long getMoney() {
        return money.get();
    }

    public boolean isBlock() {
        //System.out.printf("account %s blocked: %s\n", accNumber, block);
        return block;
    }

    public void setBlock(boolean block) {
       //if (block) System.out.printf("account %s blocking now\n", accNumber);
        this.block = block;
    }

    public void transferTo(long amount) {
        //System.out.printf("account %s receive amount: %s\n", accNumber, amount);
        money.addAndGet(amount);
    }

    public void transferFrom(long amount) {
        //System.out.printf("account %s sent amount: %s\n", accNumber, amount);
        money.addAndGet(-amount);
    }

    @Override
    public int compareTo(Account o) {
        return this.getAccNumber().compareTo(o.accNumber);
    }
}
