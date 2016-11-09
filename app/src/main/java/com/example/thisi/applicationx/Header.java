package com.example.thisi.applicationx;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Date;
import java.util.Hashtable;

/**
 * Created by thisi on 11/7/2016.
 */

public class Header {
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
    public BigDecimal SALES_AMOUNT;
    public BigDecimal TOTAL_TAX;
    public BigDecimal TOTAL_DISCOUNT;
    public BigDecimal ROUNDING;
    public BigDecimal ROUNDING_ADJ;
    public String APPROVAL_ID;
    public String CUSTOMER_CODE;
    public BigDecimal CUSTOMER_POINT;
    public String REFUND_VOUCHER_CODE;
    public BigDecimal REFUND_VOUCHER_AMOUNT;
    public date REFUND_VOUCHER_EXPIRE_DATE;
    public String DRAWER_DECLARE_ID;
    public String BOTRANS_NO;
    public date MODIFIED_DATE;
    public String MODIFIED_ID;
    public int ITEM_VOID_COUNT;
    public int REPRINT_COUNT;
    public BigDecimal ITEM_VOID_AMOUNT;
    public BigDecimal REPRINT_AMOUNT;
    public String PRICE_LEVEL;
    public String REFUND_POS_NO;
    public String REFUND_RCP_NO;
    public String REFUND_REMARK;
    public date REFUND_RCP_BUS_DATE;
    public booelan IsFORCE_REFUND;
    public int REPRINTCOUNT;
    public booelan ToSAP;
    public String MEMBER_IC;
    public String PROTRANS_NO;
}
