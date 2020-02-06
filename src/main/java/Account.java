public class Account
{
    private long money;
    private String accNumber;

    public Account(long money, String accNumber) {
        this.money = money;
        this.accNumber = accNumber;
    }

    public String getAccNumber() {
        return accNumber;
    }
}
