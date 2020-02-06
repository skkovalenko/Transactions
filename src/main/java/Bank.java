import java.util.HashMap;
import java.util.Random;

public class Bank
{
    private HashMap<String, Account> accounts = new HashMap<>();
    private final Random random = new Random();

    public /*synchronized*/ boolean isFraud(String fromAccountNum, String toAccountNum, long amount) throws InterruptedException {
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
    public Account getAccount(String accNumber){
        return accounts.get(accNumber);
    }


    /**
     * TODO: реализовать метод. Метод переводит деньги между счетами.
     * Если сумма транзакции > 50000, то после совершения транзакции,
     * она отправляется на проверку Службе Безопасности – вызывается
     * метод isFraud. Если возвращается true, то делается блокировка
     * счетов (как – на ваше усмотрение)
     */
    public void  transfer(String fromAccountNum, String toAccountNum, long amount) throws InterruptedException {

        if((!getAccount(fromAccountNum).isBlock() && !getAccount(toAccountNum).isBlock())
                && (getBalance(fromAccountNum) >= amount)){
            transferFromTo(fromAccountNum, toAccountNum, amount);
            if (amount > 50000){
                checkForFraud(fromAccountNum, toAccountNum, amount);
            }
        }
    }
    private void checkForFraud(String fromAccountNum, String toAccountNum, long amount) throws InterruptedException {
        if(Integer.parseInt(fromAccountNum) < Integer.parseInt(toAccountNum)){
            synchronized (getAccount(fromAccountNum)){
                synchronized (getAccount(toAccountNum)){
                    boolean block = isFraud(fromAccountNum, toAccountNum, amount);
                    getAccount(fromAccountNum).setBlock(block);
                    getAccount(toAccountNum).setBlock(block);
                }
            }
        }else {
            synchronized (getAccount(toAccountNum)){
                synchronized (getAccount(fromAccountNum)){
                    boolean block = isFraud(fromAccountNum, toAccountNum, amount);
                    getAccount(fromAccountNum).setBlock(block);
                    getAccount(toAccountNum).setBlock(block);
                }
            }

        }
    }
    private void transferFromTo(String fromAccountNum, String toAccountNum, long amount){
        if(Integer.parseInt(fromAccountNum) < Integer.parseInt(toAccountNum)){
            synchronized (getAccount(fromAccountNum)){
                synchronized (getAccount(toAccountNum)){
                    getAccount(fromAccountNum).transferFrom(amount);
                    getAccount(toAccountNum).transferTo(amount);
                }
            }
        }else{
            synchronized (getAccount(toAccountNum)){
                synchronized (getAccount(fromAccountNum)){
                    getAccount(fromAccountNum).transferFrom(amount);
                    getAccount(toAccountNum).transferTo(amount);
                }
            }
        }
        //
    }

    /**
     * TODO: реализовать метод. Возвращает остаток на счёте.
     */
    public long getBalance(String accountNum){

        return getAccount(accountNum).getMoney();
    }
}
