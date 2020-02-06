public class Transfer implements Runnable {

    Bank bank;
    String[] accNumbersArray;
    int TRANSFERS_QUANTITY;
    double BIG_TRANSFERS_PERCENT;
    int controlCountForBigTransfer = 0;

    public Transfer(Bank bank, String[] accNumbersArray, int transfersQuantity, double bigTransfersPercent){
        this.bank = bank;
        this.accNumbersArray = accNumbersArray;
        TRANSFERS_QUANTITY = transfersQuantity;
        BIG_TRANSFERS_PERCENT = bigTransfersPercent;
    }

    private long generateAmount(){
        long amount = (long)(Math.random() * 55000);
        if(amount > 50000){
            controlCountForBigTransfer++;
            if(controlCountForBigTransfer >= TRANSFERS_QUANTITY * BIG_TRANSFERS_PERCENT){
                while (amount > 50000){
                    amount = (long)(Math.random() * 55000);
                }
            }
        }
        return amount;
    }

    @Override
    public void run() {

        for (int i = 0; i < TRANSFERS_QUANTITY; i++){
            //generate random account number for transfer : indexFrom and indexTo
            int indexFrom = (int)(Math.random() * (double)accNumbersArray.length);
            int indexTo = (int)(Math.random() * (double)accNumbersArray.length);
            while (indexFrom == indexTo){
                indexTo = (int)(Math.random() * (double)accNumbersArray.length);
            }
            try {
                bank.transfer(accNumbersArray[indexFrom], accNumbersArray[indexTo], generateAmount());
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
        }
    }
}
