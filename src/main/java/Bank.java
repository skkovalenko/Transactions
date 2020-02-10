import java.util.HashMap;
import java.util.Random;

public class Bank
{
    private HashMap<String, Account> accounts = new HashMap<>();
    private final Random random = new Random();

    public synchronized boolean isFraud(String fromAccountNum, String toAccountNum, long amount) throws InterruptedException {
        Thread.sleep(1000);
        return random.nextBoolean();
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
    private Account getAccount(String accNumber){
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
        synchronized (getAccount(fromAccountNum).compareTo(getAccount(toAccountNum)) > 0 ? getAccount(fromAccountNum) : getAccount(toAccountNum)){
            synchronized (getAccount(fromAccountNum).compareTo(getAccount(toAccountNum)) < 0 ? getAccount(fromAccountNum) : getAccount(toAccountNum)){
                if((!getAccount(fromAccountNum).isBlock() && !getAccount(toAccountNum).isBlock())
                        && (getAccount(fromAccountNum).getMoney() >= amount)){
                    getAccount(fromAccountNum).transferFrom(amount);
                    getAccount(toAccountNum).transferTo(amount);
                    if (amount > 50000){
                        boolean block = isFraud(getAccount(fromAccountNum).getAccNumber(), getAccount(toAccountNum).getAccNumber(), amount);
                        getAccount(fromAccountNum).setBlock(block);
                        getAccount(toAccountNum).setBlock(block);
                    }
                }
            }
        }
    }
    /**
     * TODO: реализовать метод. Возвращает остаток на счёте.
     */
    public  long getBalance(String accountNum) {
        synchronized (getAccount(accountNum)){
            return getAccount(accountNum).getMoney();
        }
    }
}
