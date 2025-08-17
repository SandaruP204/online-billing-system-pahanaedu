// src/main/java/dao/BillItemDAOimpl.java
package dao;

import model.BillItem;
import model.BillItemDetails;
import java.util.List;

public interface BillItemDAO {
    List<BillItemDetails> getBillItems(int billId) throws Exception;
    void addItems(int billId, List<BillItem> items) throws Exception; // optional: used when not batching in BillDAO
}
