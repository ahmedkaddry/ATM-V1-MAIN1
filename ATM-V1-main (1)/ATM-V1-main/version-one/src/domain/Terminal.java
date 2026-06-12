package domain;

public class Terminal {

    private int cash20;
    private int cash50;
    private int cash100;
    private int cash200;
    private int receiptPaper;

    public Terminal(int cash20, int cash50, int cash100, int cash200, int receiptPaper) {
        this.cash20 = cash20;
        this.cash50 = cash50;
        this.cash100 = cash100;
        this.cash200 = cash200;
        this.receiptPaper = receiptPaper;
    }

    public int getCash() {
        return (cash20 * 20) + (cash50 * 50) + (cash100 * 100) + (cash200 * 200);
    }

    public int getCash20() {
        return cash20;
    }

    public int getCash50() {
        return cash50;
    }

    public int getCash100() {
        return cash100;
    }

    public int getCash200() {
        return cash200;
    }

    public void setCash20(int cash20) {
        this.cash20 = cash20;
    }

    public void setCash50(int cash50) {
        this.cash50 = cash50;
    }

    public void setCash100(int cash100) {
        this.cash100 = cash100;
    }

    public void setCash200(int cash200) {
        this.cash200 = cash200;
    }

    public int getReceiptPaper() {
        return receiptPaper;
    }

    public void setReceiptPaper(int receiptPaper) {
        this.receiptPaper = receiptPaper;
    }
}
