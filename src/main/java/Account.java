public class Account
{
    private long money;
    private String accNumber;
    private boolean block;

    public Account(long money, String accNumber) {
        this.money = money;
        this.accNumber = accNumber;
        block = false;
    }

    public void setBlock(boolean block) {
       //if (block) System.out.printf("account %s blocking now\n", accNumber);
        this.block = block;
    }

    public String getAccNumber() {
        return accNumber;
    }

    public synchronized long getMoney() {
        return money;
    }

    public synchronized boolean isBlock() {
        //System.out.printf("account %s blocked: %s\n", accNumber, block);
        return block;
    }
    public void transferTo(long amount) {
        //System.out.printf("account %s receive amount: %s\n", accNumber, amount);
        money = money + amount;
    }
    public void transferFrom(long amount) {
        //System.out.printf("account %s sent amount: %s\n", accNumber, amount);
        money = money - amount;
    }
}
