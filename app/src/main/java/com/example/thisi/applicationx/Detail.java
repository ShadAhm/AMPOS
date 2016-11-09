package com.example.thisi.applicationx;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Hashtable;

public class Detail {
    public String COMPANY_CODE;
    public String OUTLET_CODE;
    public String EMP_CODE;
    public String POS_NO;
    public String SHIFT_NO;
    public String RCP_NO;
    public String TRANS_TYPE;
    public Date BUS_DATE;
    public Date TRANS_DATE;
    public String TRANS_TIME;
    public int ROW_NUMBER;
    public String PROD_CODE;
    public String PROD_NAME;
    public String PROD_TYPE_CODE;
    public String USAGE_UOM;
    public BigDecimal QUANTITY;
    public BigDecimal UOM_CONV;
    public String PRICE_LVL_CODE;
    public BigDecimal UNIT_PRICE;
    public BigDecimal TOTAL_PRICE;
    public BigDecimal TAX_01;
    public BigDecimal TAX_02;
    public BigDecimal TAX_03;
    public BigDecimal TAX_04;
    public BigDecimal TAX_05;
    public String DISCOUNT_CODE;
    public BigDecimal ITEM_DISCOUNT_AMOUNT;
    public String TOTAL_DISCOUNT_CODE;
    public BigDecimal TOTAL_DISCOUNT_AMOUNT;
    public BigDecimal TICKET_SURCHARGE;
    public String STAFF_DISCOUNT_CODE;
    public BigDecimal STAFF_DISCOUNT;
    public String BARCODE;
    public String TAXCODE;
    public BigDecimal COST;
    public boolean ToSAP;
}
