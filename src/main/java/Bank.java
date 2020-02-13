import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Bank
{
    private ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();
    private final Random random = new Random();

    public synchronized boolean isFraud(String fromAccountNum, String toAccountNum, long amount) throws InterruptedException {
        Thread.sleep(1000);
        return random.nextBoolean();
    }
    public int countAccounts(){
        return accounts.size();
    }

    public void putAccount(Account account){
        accounts.put(account.getAccNumber(), account);
    }

    public boolean existsAccount(String accNumber){
        return accounts.containsKey(accNumber);
    }

    public long getAllBalance() {
        return accounts.values().stream().mapToLong(Account::getMoney).sum();
    }
    Account getAccount(String accNumber){
        return accounts.get(accNumber);
    }

    /**
     * TODO: реализовать метод. Метод переводит деньги между счетами.
     * Если сумма транзакции > 50000, то после совершения транзакции,
     * она отправляется на проверку Службе Безопасности – вызывается
     * метод isFraud. Если возвращается true, то делается блокировка
     * счетов (как – на ваше усмотрение)
     */

    public void transfer(String fromAccountNum, String toAccountNum, long amount) throws InterruptedException {
        Account accFrom = getAccount(fromAccountNum);
        Account accTo = getAccount(toAccountNum);
        synchronized (accFrom.compareTo(accTo) > 0 ? accFrom : accTo){
            synchronized (accFrom.compareTo(accTo) < 0 ? accFrom : accTo){
                if((!accFrom.isBlock() && !accTo.isBlock())
                        && (accFrom.getMoney() >= amount)){
                    accFrom.transferFrom(amount);
                    accTo.transferTo(amount);
                    if (amount > 50000){
                        boolean block = isFraud(accFrom.getAccNumber(), accTo.getAccNumber(), amount);
                        accFrom.setBlock(block);
                        accTo.setBlock(block);
                    }
                }
            }
        }
    }
    /**
     * TODO: реализовать метод. Возвращает остаток на счёте.
     */
    public long getBalance(String accountNum) {
        Account account = getAccount(accountNum);
        return account.getMoney();
    }
}
