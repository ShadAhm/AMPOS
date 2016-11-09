package com.example.thisi.applicationx;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Date;
import java.util.Hashtable;

public class Payment {
    public String COMPANY_CODE;
    public String OUTLET_CODE;
    public String EMP_CODE;
    public String POS_NO;
    public String SHIFT_NO;
    public String RCP_NO;
    public String TRANS_TYPE;
    public date BUS_DATE;
    public date TRANS_DATE;
    public String TRANS_TIME;
    public int ROW_NUMBER;
    public String PAYMENT_CODE;
    public String PAYMENT_NAME;
    public String PAYMENT_TYPE;
    public String FOREX_CODE;
    public BigDecimal FOREX_AMOUNT;
    public String CARD_NO;
    public String CARD_TYPE;
    public String BANK_CODE;
    public BigDecimal PAYMENT_AMOUNT;
    public BigDecimal CHANGE_AMOUNT;
    public BigDecimal TENDER_AMOUNT;
    public String PAYMT_REMARK;
    public String DRAWER_DECLARE_ID;
    public date MODIFIED_DATE;
    public String MODIFIED_ID;
    public boolean ToSAP;
}
