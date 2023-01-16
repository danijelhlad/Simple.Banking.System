package banking;

public class Functions {
    Database database;
    public Functions(String databaseUrl) {
        database = new Database(databaseUrl);
    }
    private String generateTempCardVal(){
        String cardBin = "400000";
        long number = (long) (Math.random() * 100000) + 33333000L;
        String lastPart = String.format("%d14",number);
        return cardBin+lastPart;
    }
    public int generatePin(){
        int randomPIN = (int)(Math.random()*9000)+1000;
        String val = ""+randomPIN;
        return Integer.parseInt(val);
    }
    public boolean findSame(String numCheckFor){
        return database.checkIfExist(numCheckFor);
    }

    public boolean findCorrectnessNumAndPin(String num,String pin) {
        return database.checkNumPin(num,pin);
    }
    public String createNewCard(){
        String tempCardVal;

        do {
            tempCardVal = generateCardNum();
        } while (findSame(tempCardVal));
        return tempCardVal;
    }

    public boolean checksum(String generatedNum) {
        int sum = 0;
        boolean alternate = false;
        for (int i = generatedNum.length() - 1; i >= 0; i--)
        {
            int n = Integer.parseInt(generatedNum.substring(i, i + 1));
            if (alternate)
            {
                n *= 2;
                if (n > 9)
                {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }
    private String generateCardNum() {
        String temp;
        do {
            temp = generateTempCardVal();
        } while (!checksum(temp));
        return temp;
    }


}