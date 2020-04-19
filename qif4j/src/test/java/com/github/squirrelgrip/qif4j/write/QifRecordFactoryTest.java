package com.github.squirrelgrip.qif4j.write;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.github.squirrelgrip.qif4j.QifAccount;

import com.github.squirrelgrip.qif4j.QifInvestment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QifRecordFactoryTest {

    @Test
    public void testInvestment() {
        QifInvestment txn = new QifInvestment();
        txn.setAction("Buy");
        txn.setSecurity("Foo Industries");
        txn.setDate(LocalDate.of(2014, 3, 21));
        txn.setPrice(BigDecimal.valueOf(25.66));
        txn.setQuantity(BigDecimal.valueOf(100L));
        txn.setCommission(BigDecimal.valueOf(29.95));
        txn.setMemo("Memo text");
        txn.setTotal(BigDecimal.valueOf(2566.0d + 29.95d));

        QifRecordFactory factory = new QifRecordFactory(TestUtils.buildPortfolioAccount());
        QifRecord invstRecord = factory.forTransaction(txn);
        assertEquals(
                "!Type:Invst\nD3/21/14\nNBuy\nYFoo Industries\nI25.66\nQ100\nT2595.95\nO29.95\nMMemo text\n^\n",
                invstRecord.asFormattedRecord(null), "Invst record format");
    }

    @Test
    public void testAccountRecord() {
        String name = "My Portfolio";
        String desc = "Shares held in XYZ Broker account";
        BigDecimal balance = BigDecimal.valueOf(10507.33d);
        QifAccount acc = new QifAccount(QifAccountTypeEnum.PORTFOLIO, name, desc,
                balance);
        QifAccountRecord record = new QifAccountRecord(acc);
        assertEquals("!Type:Account\nN" + name
                + "\nD" + desc + "\nT" + QifAccountTypeEnum.PORTFOLIO.getLabel()
                + "\nB" + balance + "\n^\n", record.asFormattedRecord(), "Account record format");
    }

}
