package service;

import java.util.List;

public interface BillingService {

    /** Immutable line item */
    final class Line {
        public final int productNo;
        public final int quantity;
        public Line(int productNo, int quantity) {
            this.productNo = productNo; this.quantity = quantity;
        }
    }

    /** Thrown when any line would make stock go negative */
    class InsufficientStockException extends Exception {
        public final int productNo, requested, available;
        public InsufficientStockException(int productNo, int requested, int available) {
            super("Insufficient stock for product #" + productNo +
                    " (requested " + requested + ", available " + available + ")");
            this.productNo = productNo; this.requested = requested; this.available = available;
        }
    }

    /** Creates a bill atomically and returns new bill_id. */
    int createBill(int accountNo, List<Line> lines)
            throws InsufficientStockException, Exception;
}
