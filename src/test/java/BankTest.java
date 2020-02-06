import junit.framework.TestCase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Arrays;

public class BankTest {

    private Bank bankTestAllBalance;
    private Bank bankTestLock;
    private double BIG_TRANSFER_PERCENT;
    private int THREADS_QUANTITY;
    private int ACCOUNTS_QUANTITY;
    private int TRANSFERS_QUANTITY;
    private String[] accNumbersArray = null;
    private ArrayList<Thread> threads;
    private long expected;
    private ThreadMXBean threadMXBean;
    @Before
    public void setUp() {
        bankTestAllBalance = new Bank();
        bankTestLock = new Bank();
    }

    @After
    public void tearDown() {
        ACCOUNTS_QUANTITY = 0;
        THREADS_QUANTITY = 0;
        TRANSFERS_QUANTITY = 0;
        BIG_TRANSFER_PERCENT = 0.0;
        threads.clear();
        accNumbersArray = null;
        expected = 0;
    }

    @Test
    public void bankTestLock() throws InterruptedException {
        //
        ACCOUNTS_QUANTITY = 3;
        THREADS_QUANTITY = 20;
        TRANSFERS_QUANTITY = 10000;
        BIG_TRANSFER_PERCENT = 0.00001;
        threads = new ArrayList<>();
        //
        generateAccounts(ACCOUNTS_QUANTITY, bankTestLock);
        //
        generateThreads(THREADS_QUANTITY,
                TRANSFERS_QUANTITY,
                BIG_TRANSFER_PERCENT,
                accNumbersArray,
                bankTestLock,
                false);


        threadMXBean = ManagementFactory.getThreadMXBean();
        String [] actualReport = reportDeadLock(THREADS_QUANTITY);
        if(actualReport != null){
            Arrays.stream(actualReport).forEach(System.out::println);
            System.out.println();
        }
        Assert.assertNull(actualReport);

    }
    @Test
    public void allBalance() throws InterruptedException {
        //
        ACCOUNTS_QUANTITY = 100;
        THREADS_QUANTITY = 10;
        TRANSFERS_QUANTITY = 10000;
        BIG_TRANSFER_PERCENT = 0.05;
        threads = new ArrayList<>();
        //
        generateAccounts(ACCOUNTS_QUANTITY, bankTestAllBalance);
        //
        expected = bankTestAllBalance.getAllBalance();
        //
        generateThreads(THREADS_QUANTITY,
                TRANSFERS_QUANTITY,
                BIG_TRANSFER_PERCENT,
                accNumbersArray,
                bankTestAllBalance,
                true);
        //
        Assert.assertEquals(expected, bankTestAllBalance.getAllBalance());
    }

    // method for detection deadlock
    private String[] reportDeadLock(int THREADS_QUANTITY){
        int count = 0;
        while (count <= THREADS_QUANTITY){
            long[] threadsIdDeadlock = threadMXBean.findMonitorDeadlockedThreads();
            if(threadsIdDeadlock != null){
                String[] report = new String[threadsIdDeadlock.length];
                for (Thread thread : threads){
                    for (int i = 0; i < threadsIdDeadlock.length; i++) {
                        if(threadsIdDeadlock[i] == thread.getId()){
                            report[i] = threadMXBean.getThreadInfo(threadsIdDeadlock[i]).toString().split("\n")[0] + "\n";
                            for (StackTraceElement traceElement : thread.getStackTrace()){
                                report[i] = report[i].concat(traceElement.toString() + "\n");
                            }
                        }
                    }
                }
                return report;
            }
            for(Thread thread : threads){

                if(!thread.isAlive()){
                    count++;
                    if(count == THREADS_QUANTITY) {
                        break;
                    }
                }
            }
        }
        return null;
    }
    private void generateAccounts(int ACCOUNTS_QUANTITY, Bank bank){

        accNumbersArray = new String[ACCOUNTS_QUANTITY];

        for (int i = 0; i < ACCOUNTS_QUANTITY; i++){
            String accNumber;
            while (true){
                int numAcc = (int)(Math.random() * 1000);
                accNumber = String.format("%03d", numAcc);
                if(bank.existsAccount(accNumber)){
                    continue;
                }
                break;
            }
            Account account = new Account((long)(Math.random() * 100000), accNumber); //(long money, String accNumber)
            bank.putAccount(account);
            accNumbersArray[i] = accNumber;
        }
    }
    private void generateThreads(int THREADS_QUANTITY,
                                 int TRANSFERS_QUANTITY,
                                 double BIG_TRANSFER_PERCENT,
                                 String[] accNumbersArray,
                                 Bank bank,
                                 boolean joinForThreads)
            throws InterruptedException {

        for(int i = 0; i < THREADS_QUANTITY; i++){
            threads.add(new Thread(
                    new Transfer(bank,
                            accNumbersArray,
                            TRANSFERS_QUANTITY,
                            BIG_TRANSFER_PERCENT)));
        }

        for (Thread thread : threads) {
            thread.start();
        }
        if(joinForThreads){
            for(Thread thread : threads){
                thread.join();
            }
        }
    }
}