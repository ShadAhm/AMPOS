package com.example.thisi.applicationx;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import org.ksoap2.serialization.SoapObject;

import java.math.BigDecimal;

/**
 * Created by thisi on 11/5/2016.
 */

public class DownloadDataService extends Service1 {
    DatabaseHelper dataHelper;

    public DownloadDataService(IWsdl2CodeEvents eventHandler, String url, Context context) {
        super(eventHandler, url);

        dataHelper = DatabaseHelper.getHelper(context);
    }

    public void DownloadDataAsync() throws Exception {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected void onPreExecute() {
                eventHandler.Wsdl2CodeStartedRequest();
            }

            @Override
            protected Object doInBackground(Void... params) {
                Object o = null;
                SQLiteDatabase db = dataHelper.getReadableDatabase();
                db.beginTransaction();

                try {
                    DeleteExistingData(db);

                    DownloadEmployees(db);
                    DownloadCustomers(db);
                    DownloadProduct_Master(db);
                    DownloadPrice_Group(db);
                    DownloadHeaders(db); 
                    DownloadDetail(db);
                    DownloadPayment(db);

                    db.setTransactionSuccessful();
                    o = "success";
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    db.endTransaction();
                    db.close();
                }

                return o;
            }

            @Override
            protected void onPostExecute(Object result) {
                eventHandler.Wsdl2CodeEndedRequest();
                if (result != null) {
                    eventHandler.Wsdl2CodeFinished("SQLResult", result);
                }
            }
        }.execute();
    }

    private void DeleteExistingData(SQLiteDatabase db) {
        dataHelper.clearOldData(db);
    }

    private void DownloadEmployees(SQLiteDatabase db) {
        SoapObject returned = super.SQLResultReturn(
                "SELECT COALESCE(EMP_CODE, 0) AS EMP_CODE,COALESCE(EMP_NAME, '') AS EMP_NAME,COALESCE(EMP_PASSWORD, '') AS EMP_PASSWORD,COALESCE(EMP_LVL, 0) AS EMP_LVL,COALESCE(POS_ALLOW, 0) AS POS_ALLOW,COALESCE(VIEW_COST_ALLOW, 0) AS VIEW_COST_ALLOW,COALESCE(EMP_GROUP_CODE, '') AS EMP_GROUP_CODE,COALESCE(EMP_GROUP_NAME, '') AS EMP_GROUP_NAME,COALESCE(IsActive, 0) AS IsActive FROM Employee"
                , null
        );

        if(returned != null) {
            Employee[] emps = RetrieveEmployeesFromSoap(returned);

            for (int i = 0; i < emps.length; i++) {
                dataHelper.insertEmployee(db, emps[i]);
            }
        }
    }

    private void DownloadCustomers(SQLiteDatabase db) {
        SoapObject returned = super.SQLResultReturn(
                "SELECT COALESCE(CUSTOMER_CODE, '') AS CUSTOMER_CODE,COALESCE(CUSTOMER_NAME, '') AS CUSTOMER_NAME,COALESCE(CUSTOMER_GRP_CODE, '') AS CUSTOMER_GRP_CODE,COALESCE(IC_NO, '') AS IC_NO,COALESCE(ADDRESS1, '') AS ADDRESS1,COALESCE(ADDRESS2, '') AS ADDRESS2,COALESCE(POS_CODE, '') AS POS_CODE,COALESCE(COUNTY, '') AS COUNTY,COALESCE(STATE, '') AS STATE,COALESCE(COUNTRY, '') AS COUNTRY,COALESCE(CONTACT, '') AS CONTACT,COALESCE(MOBILE, '') AS MOBILE,COALESCE(FAX, '') AS FAX,COALESCE(EMAIL, '') AS EMAIL,COALESCE(IsWHOLESALES, 0) AS IsWHOLESALES,COALESCE(IsActive, 0) AS IsActive,COALESCE(IsMember, 0) AS IsMember,COALESCE(POINT_VALUE, 0) AS POINT_VALUE,COALESCE(BYSMS, 0) AS BYSMS,COALESCE(BYEMAIL, 0) AS BYEMAIL,COALESCE(MODIFIED_ID, '') AS MODIFIED_ID,COALESCE(TEMP_CUSTOMER_CODE, '') AS TEMP_CUSTOMER_CODE,COALESCE(GST_REG_NO, '') AS GST_REG_NO,COALESCE(IsEMPLOYEE, 0) AS IsEMPLOYEE,COALESCE(PRICE_GRP_CODE, '') AS PRICE_GRP_CODE,COALESCE(Outlet_Code, '') AS Outlet_Code FROM Customer"
                , null
        );

        if(returned != null) {
            Customer[] custs = RetrieveCustomersFromSoap(returned);

            for (int i = 0; i < custs.length; i++) {
                dataHelper.insertCustomer(db, custs[i]);
            }
        }
    }

    private void DownloadProduct_Master(SQLiteDatabase db) {
        SoapObject returned = super.SQLResultReturn(
            "SELECT COALESCE(PROD_CODE, '') AS PROD_CODE,COALESCE(PRODUCT_SHORT_NAME, '') AS PRODUCT_SHORT_NAME,COALESCE(PROD_NAME, '') AS PROD_NAME,COALESCE(PROD_TYPE_CODE, '') AS PROD_TYPE_CODE,COALESCE(BARCODE, '') AS BARCODE,COALESCE(PROD_GRP_01, '') AS PROD_GRP_01,COALESCE(PROD_GRP_02, '') AS PROD_GRP_02,COALESCE(PROD_GRP_03, '') AS PROD_GRP_03,COALESCE(PROD_GRP_04, '') AS PROD_GRP_04,COALESCE(PROD_GRP_05, '') AS PROD_GRP_05,COALESCE(PROD_DISC_GRP, '') AS PROD_DISC_GRP,COALESCE(TAX_01, 0) AS TAX_01,COALESCE(TAX_02, 0) AS TAX_02,COALESCE(TAX_03, 0) AS TAX_03,COALESCE(TAX_04, 0) AS TAX_04,COALESCE(TAX_05, 0) AS TAX_05,COALESCE(PRICE_01, '0.0') AS PRICE_01,COALESCE(PRICE_02, '0.0') AS PRICE_02,COALESCE(PRICE_03, '0.0') AS PRICE_03,COALESCE(PRICE_04, '0.0') AS PRICE_04,COALESCE(PRICE_05, '0.0') AS PRICE_05,COALESCE(PRICE_06, '0.0') AS PRICE_06,COALESCE(PRICE_07, '0.0') AS PRICE_07,COALESCE(PRICE_08, '0.0') AS PRICE_08,COALESCE(PRICE_09, '0.0') AS PRICE_09,COALESCE(PRICE_10, '0.0') AS PRICE_10,COALESCE(COST, '0.0') AS COST,COALESCE(AVERAGE_COST, '0.0') AS AVERAGE_COST,COALESCE(DISPLAY_ORDER, 0) AS DISPLAY_ORDER,COALESCE(POS_DISPLAY, 0) AS POS_DISPLAY,COALESCE(ALLOW_ZERO_PRICE, 0) AS ALLOW_ZERO_PRICE,COALESCE(IsActive, 0) AS IsActive,COALESCE(ACTIVE_PERIOD, 0) AS ACTIVE_PERIOD,COALESCE(STOCK_UOM, '') AS STOCK_UOM,COALESCE(PURCH_UOM, '') AS PURCH_UOM,COALESCE(USAGE_UOM, '') AS USAGE_UOM,COALESCE(PURCH_CONV, 0) AS PURCH_CONV,COALESCE(USAGE_CONV, 0) AS USAGE_CONV,COALESCE(IsBOM, 0) AS IsBOM,COALESCE(IsSERIAL, 0) AS IsSERIAL,COALESCE(ALLOW_DISC, 0) AS ALLOW_DISC,COALESCE(MULTIPLE_DISC, 0) AS MULTIPLE_DISC,COALESCE(MULTIPLE_UOM, 0) AS MULTIPLE_UOM,COALESCE(TICKET_TYPE, '') AS TICKET_TYPE,COALESCE(STAFF_DISCOUNT_CODE, '') AS STAFF_DISCOUNT_CODE,COALESCE(SUPPLIER_CODE, '') AS SUPPLIER_CODE,COALESCE(BARCODE_01, '') AS BARCODE_01,COALESCE(BARCODE_02, '') AS BARCODE_02,COALESCE(BARCODE_03, '') AS BARCODE_03,COALESCE(BARCODE_04, '') AS BARCODE_04,COALESCE(BARCODE_05, '') AS BARCODE_05,COALESCE(BARCODE_06, '') AS BARCODE_06,COALESCE(BARCODE_07, '') AS BARCODE_07,COALESCE(BARCODE_08, '') AS BARCODE_08,COALESCE(BARCODE_09, '') AS BARCODE_09,COALESCE(BARCODE_10, '') AS BARCODE_10,COALESCE(PRICE_PERCENTAGE_01, 0) AS PRICE_PERCENTAGE_01,COALESCE(PRICE_PERCENTAGE_02, 0) AS PRICE_PERCENTAGE_02,COALESCE(PRICE_PERCENTAGE_03, 0) AS PRICE_PERCENTAGE_03,COALESCE(PRICE_PERCENTAGE_04, 0) AS PRICE_PERCENTAGE_04,COALESCE(PRICE_PERCENTAGE_05, 0) AS PRICE_PERCENTAGE_05,COALESCE(PRICE_PERCENTAGE_06, 0) AS PRICE_PERCENTAGE_06,COALESCE(PRICE_PERCENTAGE_07, 0) AS PRICE_PERCENTAGE_07,COALESCE(PRICE_PERCENTAGE_08, 0) AS PRICE_PERCENTAGE_08,COALESCE(PRICE_PERCENTAGE_09, 0) AS PRICE_PERCENTAGE_09,COALESCE(PRICE_PERCENTAGE_10, 0) AS PRICE_PERCENTAGE_10,COALESCE(STOCK_TAKE_INTERIM, '') AS STOCK_TAKE_INTERIM,COALESCE(MODIFIED_ID, '') AS MODIFIED_ID,COALESCE(IMG_PATH, '') AS IMG_PATH,COALESCE(TAXCODE, '') AS TAXCODE FROM product_master"
        , null);

        if(returned != null) {
            Product_Master[] prods = RetrieveProduct_MasterFromSoap(returned);

            for (int i = 0; i < prods.length; i++) {
                dataHelper.insertProductMaster(db, prods[i]);
            }
        }
    }

    private void DownloadPrice_Group(SQLiteDatabase db) {
        SoapObject returned = super.SQLResultReturn(
                "SELECT COALESCE(PRICE_GRP_CODE, '') AS PRICE_GRP_CODE,COALESCE(PRICE_GRP_NAME, '') AS PRICE_GRP_NAME,COALESCE(PROD_CODE, '') AS PROD_CODE,COALESCE(PRICE, '0.0') AS PRICE FROM price_group"
                , null
        );

        if(returned != null) {
            Price_Group[] pgroups = RetrievePrice_GroupFromSoap(returned);

            for (int i = 0; i < pgroups.length; i++) {
                dataHelper.insertprice_group(db, pgroups[i]);
            }
        }
    }

    private void DownloadSuspend(SQLiteDatabase db) {
        SoapObject returned = super.SQLResultReturn(
                "SELECT COALESCE(COMPANY_CODE, '') AS COMPANY_CODE,COALESCE(OUTLET_CODE, '') AS OUTLET_CODE,COALESCE(POS_NO, '') AS POS_NO,COALESCE(SHIFT_NO, '') AS SHIFT_NO,COALESCE(RCP_NO, '') AS RCP_NO,COALESCE(TRANS_TYPE, '') AS TRANS_TYPE,COALESCE(TRANS_TIME, '') AS TRANS_TIME,COALESCE(ROW_NUMBER, 0) AS ROW_NUMBER,COALESCE(PROD_CODE, '') AS PROD_CODE,COALESCE(BARCODE, '') AS BARCODE,COALESCE(PROD_NAME, '') AS PROD_NAME,COALESCE(PROD_SHORT_NAME, '') AS PROD_SHORT_NAME,COALESCE(PROD_TYPE_CODE, '') AS PROD_TYPE_CODE,COALESCE(USAGE_UOM, '') AS USAGE_UOM,COALESCE(UOM_CONV, '0.0') AS UOM_CONV,COALESCE(QUANTITY, '0.0') AS QUANTITY,COALESCE(PRICE_LVL_CODE, '') AS PRICE_LVL_CODE,COALESCE(UNIT_PRICE, '0.0') AS UNIT_PRICE,COALESCE(TOTAL_PRICE, '0.0') AS TOTAL_PRICE,COALESCE(BOM_PARENT, '') AS BOM_PARENT,COALESCE(TAX_01, '0.0') AS TAX_01,COALESCE(TAX_02, '0.0') AS TAX_02,COALESCE(TAX_03, '0.0') AS TAX_03,COALESCE(TAX_04, '0.0') AS TAX_04,COALESCE(TAX_05, '0.0') AS TAX_05,COALESCE(ALLOW_DISC, 0) AS ALLOW_DISC,COALESCE(MULTIPLE_DISC, 0) AS MULTIPLE_DISC,COALESCE(DISCOUNT_CODE, '') AS DISCOUNT_CODE,COALESCE(ITEM_DISCOUNT_AMOUNT, '0.0') AS ITEM_DISCOUNT_AMOUNT,COALESCE(TOTAL_DICOUNT_CODE, '') AS TOTAL_DICOUNT_CODE,COALESCE(TOTAL_DISCOUNT_AMOUNT, '0.0') AS TOTAL_DISCOUNT_AMOUNT,COALESCE(TICKET_SURCHARGE, '0.0') AS TICKET_SURCHARGE,COALESCE(STAFF_DISCOUNT_CODE, '') AS STAFF_DISCOUNT_CODE,COALESCE(STAFF_DISCOUNT, '0.0') AS STAFF_DISCOUNT,COALESCE(SUSPEND_NUMBER, '') AS SUSPEND_NUMBER,COALESCE(IsRECALL, 0) AS IsRECALL,COALESCE(IS_UPSALES, 0) AS IS_UPSALES,COALESCE(UPSALES_CONV, '0.0') AS UPSALES_CONV,COALESCE(IS_MULTIPLEUOM, 0) AS IS_MULTIPLEUOM,COALESCE(RECALL_BY, '') AS RECALL_BY,COALESCE(APPROVE_BY, '') AS APPROVE_BY,COALESCE(MODIFIED_ID, '') AS MODIFIED_ID,COALESCE(CUSTOMER_CODE, '') AS CUSTOMER_CODE,COALESCE(TAXCODE, '') AS TAXCODE,COALESCE(COST, '0.0') AS COST,COALESCE(PRICE_GRP_CODE, '') AS PRICE_GRP_CODE,COALESCE(TABLE_NO, '') AS TABLE_NO,COALESCE(PROMOSOURCECODE, '') AS PROMOSOURCECODE,COALESCE(PROMOCHANGEPRICE, '0.0') AS PROMOCHANGEPRICE FROM Suspend", null
        );

        if(returned != null) {
            Suspend[] suspends = RetrieveSuspendFromSoap(returned);

            for (int i = 0; i < suspends.length; i++) {
                dataHelper.insertSuspend(db, suspends[i]);
            }
        }
    }

    private void DownloadHeaders(SQLiteDatabase db) {
        SoapObject returned = super.SQLResultReturn(
                "SELECT COALESCE(COMPANY_CODE, '') AS COMPANY_CODE,COALESCE(OUTLET_CODE, '') AS OUTLET_CODE,COALESCE(EMP_CODE, '') AS EMP_CODE,COALESCE(POS_NO, '') AS POS_NO,COALESCE(SHIFT_NO, '') AS SHIFT_NO,COALESCE(RCP_NO, '') AS RCP_NO,COALESCE(TRANS_TYPE, '') AS TRANS_TYPE,COALESCE(TRANS_TIME, '') AS TRANS_TIME,COALESCE(SALES_AMOUNT, '0.0') AS SALES_AMOUNT,COALESCE(TOTAL_TAX, '0.0') AS TOTAL_TAX,COALESCE(TOTAL_DISCOUNT, '0.0') AS TOTAL_DISCOUNT,COALESCE(ROUNDING, '0.0') AS ROUNDING,COALESCE(ROUNDING_ADJ, '0.0') AS ROUNDING_ADJ,COALESCE(APPROVAL_ID, '') AS APPROVAL_ID,COALESCE(CUSTOMER_CODE, '') AS CUSTOMER_CODE,COALESCE(CUSTOMER_POINT, '0.0') AS CUSTOMER_POINT,COALESCE(REFUND_VOUCHER_CODE, '') AS REFUND_VOUCHER_CODE,COALESCE(REFUND_VOUCHER_AMOUNT, '0.0') AS REFUND_VOUCHER_AMOUNT,COALESCE(DRAWER_DECLARE_ID, '') AS DRAWER_DECLARE_ID,COALESCE(BOTRANS_NO, '') AS BOTRANS_NO,COALESCE(MODIFIED_ID, '') AS MODIFIED_ID,COALESCE(ITEM_VOID_COUNT, 0) AS ITEM_VOID_COUNT,COALESCE(REPRINT_COUNT, 0) AS REPRINT_COUNT,COALESCE(ITEM_VOID_AMOUNT, '0.0') AS ITEM_VOID_AMOUNT,COALESCE(REPRINT_AMOUNT, '0.0') AS REPRINT_AMOUNT,COALESCE(PRICE_LEVEL, '') AS PRICE_LEVEL,COALESCE(REFUND_POS_NO, '') AS REFUND_POS_NO,COALESCE(REFUND_RCP_NO, '') AS REFUND_RCP_NO,COALESCE(REFUND_REMARK, '') AS REFUND_REMARK,COALESCE(IsFORCE_REFUND, 0) AS IsFORCE_REFUND,COALESCE(REPRINTCOUNT, 0) AS REPRINTCOUNT,COALESCE(ToSAP, 0) AS ToSAP,COALESCE(MEMBER_IC, '') AS MEMBER_IC,COALESCE(PROTRANS_NO, '') AS PROTRANS_NO FROM header"
                , null
        );

        if(returned != null) {
            Header[] headers = RetrieveHeaderFromSoap(returned);

            for (int i = 0; i < headers.length; i++) {
                dataHelper.insertHeader(db, headers[i]);
            }
        }
    }

    private void DownloadDetail(SQLiteDatabase db) {
        SoapObject returned = super.SQLResultReturn(
                "SELECT COALESCE(COMPANY_CODE, '') AS COMPANY_CODE,COALESCE(OUTLET_CODE, '') AS OUTLET_CODE,COALESCE(EMP_CODE, '') AS EMP_CODE,COALESCE(POS_NO, '') AS POS_NO,COALESCE(SHIFT_NO, '') AS SHIFT_NO,COALESCE(RCP_NO, '') AS RCP_NO,COALESCE(TRANS_TYPE, '') AS TRANS_TYPE,COALESCE(TRANS_TIME, '') AS TRANS_TIME,COALESCE(ROW_NUMBER, 0) AS ROW_NUMBER,COALESCE(PROD_CODE, '') AS PROD_CODE,COALESCE(PROD_NAME, '') AS PROD_NAME,COALESCE(PROD_TYPE_CODE, '') AS PROD_TYPE_CODE,COALESCE(USAGE_UOM, '') AS USAGE_UOM,COALESCE(QUANTITY, 0) AS QUANTITY,COALESCE(UOM_CONV, 0) AS UOM_CONV,COALESCE(PRICE_LVL_CODE, '') AS PRICE_LVL_CODE,COALESCE(UNIT_PRICE, 0) AS UNIT_PRICE,COALESCE(TOTAL_PRICE, 0) AS TOTAL_PRICE,COALESCE(TAX_01, 0) AS TAX_01,COALESCE(TAX_02, 0) AS TAX_02,COALESCE(TAX_03, 0) AS TAX_03,COALESCE(TAX_04, 0) AS TAX_04,COALESCE(TAX_05, 0) AS TAX_05,COALESCE(DISCOUNT_CODE, '') AS DISCOUNT_CODE,COALESCE(ITEM_DISCOUNT_AMOUNT, 0) AS ITEM_DISCOUNT_AMOUNT,COALESCE(TOTAL_DISCOUNT_CODE, '') AS TOTAL_DISCOUNT_CODE,COALESCE(TOTAL_DISCOUNT_AMOUNT, 0) AS TOTAL_DISCOUNT_AMOUNT,COALESCE(TICKET_SURCHARGE, 0) AS TICKET_SURCHARGE,COALESCE(STAFF_DISCOUNT_CODE, '') AS STAFF_DISCOUNT_CODE,COALESCE(STAFF_DISCOUNT, 0) AS STAFF_DISCOUNT,COALESCE(BARCODE, '') AS BARCODE,COALESCE(TAXCODE, '') AS TAXCODE,COALESCE(COST, 0) AS COST,COALESCE(ToSAP, 0) AS ToSAP FROM detail"
                , null
        );

        if(returned != null) {
            Detail[] details = RetrieveDetailFromSoap(returned);

            for (int i = 0; i < details.length; i++) {
                dataHelper.insertDetail(db, details[i]);
            }
        }
    }
    
    private void DownloadPayment(SQLiteDatabase db) {
        SoapObject returned = super.SQLResultReturn(
                "SELECT COALESCE(COMPANY_CODE, '') AS COMPANY_CODE,COALESCE(OUTLET_CODE, '') AS OUTLET_CODE,COALESCE(EMP_CODE, '') AS EMP_CODE,COALESCE(POS_NO, '') AS POS_NO,COALESCE(SHIFT_NO, '') AS SHIFT_NO,COALESCE(RCP_NO, '') AS RCP_NO,COALESCE(TRANS_TYPE, '') AS TRANS_TYPE,COALESCE(TRANS_TIME, '') AS TRANS_TIME,COALESCE(ROW_NUMBER, 0) AS ROW_NUMBER,COALESCE(PAYMENT_CODE, '') AS PAYMENT_CODE,COALESCE(PAYMENT_NAME, '') AS PAYMENT_NAME,COALESCE(PAYMENT_TYPE, '') AS PAYMENT_TYPE,COALESCE(FOREX_CODE, '') AS FOREX_CODE,COALESCE(FOREX_AMOUNT, '0.0') AS FOREX_AMOUNT,COALESCE(CARD_NO, '') AS CARD_NO,COALESCE(CARD_TYPE, '') AS CARD_TYPE,COALESCE(BANK_CODE, '') AS BANK_CODE,COALESCE(PAYMENT_AMOUNT, '0.0') AS PAYMENT_AMOUNT,COALESCE(CHANGE_AMOUNT, '0.0') AS CHANGE_AMOUNT,COALESCE(TENDER_AMOUNT, '0.0') AS TENDER_AMOUNT,COALESCE(PAYMT_REMARK, '') AS PAYMT_REMARK,COALESCE(DRAWER_DECLARE_ID, '') AS DRAWER_DECLARE_ID,COALESCE(MODIFIED_ID, '') AS MODIFIED_ID,COALESCE(ToSAP, 0) AS ToSAP FROM payment"
                , null
        );

        if(returned != null) {
            Payment[] payments = RetrievePaymentFromSoap(returned);

            for (int i = 0; i < payments.length; i++) {
                dataHelper.insertPayment(db, payments[i]);
            }
        }
    }

    public static Suspend[] RetrieveSuspendFromSoap(SoapObject soap) {
        try {
            Suspend[] suspends = new Suspend[soap.getPropertyCount()];
            for (int i = 0; i < suspends.length; i++) {
                SoapObject pii = (SoapObject)soap.getProperty(i);
                Suspend suspend = new Suspend();
                suspend.COMPANY_CODE = pii.getProperty(0).toString();
                suspend.OUTLET_CODE = pii.getProperty(1).toString();
                suspend.POS_NO = pii.getProperty(2).toString();
                suspend.SHIFT_NO = pii.getProperty(3).toString();
                suspend.RCP_NO = pii.getProperty(4).toString();
                suspend.TRANS_TYPE = pii.getProperty(5).toString();
                suspend.TRANS_TIME = pii.getProperty(6).toString();
                suspend.ROW_NUMBER = Integer.parseInt(pii.getProperty(7).toString());
                suspend.PROD_CODE = pii.getProperty(8).toString();
                suspend.BARCODE = pii.getProperty(9).toString();
                suspend.PROD_NAME = pii.getProperty(10).toString();
                suspend.PROD_SHORT_NAME = pii.getProperty(11).toString();
                suspend.PROD_TYPE_CODE = pii.getProperty(12).toString();
                suspend.USAGE_UOM = pii.getProperty(13).toString();
                suspend.UOM_CONV = new BigDecimal(pii.getProperty(14).toString());
                suspend.QUANTITY = new BigDecimal(pii.getProperty(15).toString());
                suspend.PRICE_LVL_CODE = pii.getProperty(16).toString();
                suspend.UNIT_PRICE = new BigDecimal(pii.getProperty(17).toString());
                suspend.TOTAL_PRICE = new BigDecimal(pii.getProperty(18).toString());
                suspend.BOM_PARENT = pii.getProperty(19).toString();
                suspend.TAX_01 = new BigDecimal(pii.getProperty(20).toString());
                suspend.TAX_02 = new BigDecimal(pii.getProperty(21).toString());
                suspend.TAX_03 = new BigDecimal(pii.getProperty(22).toString());
                suspend.TAX_04 = new BigDecimal(pii.getProperty(23).toString());
                suspend.TAX_05 = new BigDecimal(pii.getProperty(24).toString());
                suspend.ALLOW_DISC = Boolean.getBoolean(pii.getProperty(25).toString());
                suspend.MULTIPLE_DISC = Boolean.getBoolean(pii.getProperty(26).toString());
                suspend.DISCOUNT_CODE = pii.getProperty(27).toString();
                suspend.ITEM_DISCOUNT_AMOUNT = new BigDecimal(pii.getProperty(28).toString());
                suspend.TOTAL_DICOUNT_CODE = pii.getProperty(29).toString();
                suspend.TOTAL_DISCOUNT_AMOUNT = new BigDecimal(pii.getProperty(30).toString());
                suspend.TICKET_SURCHARGE = new BigDecimal(pii.getProperty(31).toString());
                suspend.STAFF_DISCOUNT_CODE = pii.getProperty(32).toString();
                suspend.STAFF_DISCOUNT = new BigDecimal(pii.getProperty(33).toString());
                suspend.SUSPEND_NUMBER = pii.getProperty(34).toString();
                suspend.IsRECALL = Boolean.getBoolean(pii.getProperty(35).toString());
                suspend.IS_UPSALES = Boolean.getBoolean(pii.getProperty(36).toString());
                suspend.UPSALES_CONV = new BigDecimal(pii.getProperty(37).toString());
                suspend.IS_MULTIPLEUOM = Boolean.getBoolean(pii.getProperty(38).toString());
                suspend.RECALL_BY = pii.getProperty(39).toString();
                suspend.APPROVE_BY = pii.getProperty(40).toString();
                suspend.MODIFIED_ID = pii.getProperty(41).toString();
                suspend.CUSTOMER_CODE = pii.getProperty(42).toString();
                suspend.TAXCODE = pii.getProperty(43).toString();
                suspend.COST = new BigDecimal(pii.getProperty(44).toString());
                suspend.PRICE_GRP_CODE = pii.getProperty(45).toString();
                suspend.TABLE_NO = pii.getProperty(46).toString();
                suspend.PROMOSOURCECODE = pii.getProperty(47).toString();
                suspend.PROMOCHANGEPRICE = new BigDecimal(pii.getProperty(48).toString());
                suspends[i] = suspend;
            }
            return suspends;
        } catch (IndexOutOfBoundsException ex) {
            throw ex;
        }
    }

    private Product_Master[] RetrieveProduct_MasterFromSoap(SoapObject soap) {
        try {
            Product_Master[] productMasters = new Product_Master[soap.getPropertyCount()];
            for (int i = 0; i < productMasters.length; i++) {
                SoapObject pii = (SoapObject) soap.getProperty(i);
                Product_Master prod = new Product_Master();

                prod.PROD_CODE = pii.getProperty(0).toString();
                prod.PRODUCT_SHORT_NAME = pii.getProperty(1).toString();
                prod.PROD_NAME = pii.getProperty(2).toString();
                prod.PROD_TYPE_CODE = pii.getProperty(3).toString();
                prod.BARCODE = pii.getProperty(4).toString();
                prod.PROD_GRP_01 = pii.getProperty(5).toString();
                prod.PROD_GRP_02 = pii.getProperty(6).toString();
                prod.PROD_GRP_03 = pii.getProperty(7).toString();
                prod.PROD_GRP_04 = pii.getProperty(8).toString();
                prod.PROD_GRP_05 = pii.getProperty(9).toString();
                prod.PROD_DISC_GRP = pii.getProperty(10).toString();
                prod.TAX_01 = Boolean.getBoolean(pii.getProperty(11).toString());
                prod.TAX_02 = Boolean.getBoolean(pii.getProperty(12).toString());
                prod.TAX_03 = Boolean.getBoolean(pii.getProperty(13).toString());
                prod.TAX_04 = Boolean.getBoolean(pii.getProperty(14).toString());
                prod.TAX_05 = Boolean.getBoolean(pii.getProperty(15).toString());
                prod.PRICE_01 = new BigDecimal(pii.getProperty(16).toString());
                prod.PRICE_02 = new BigDecimal(pii.getProperty(17).toString());
                prod.PRICE_03 = new BigDecimal(pii.getProperty(18).toString());
                prod.PRICE_04 = new BigDecimal(pii.getProperty(19).toString());
                prod.PRICE_05 = new BigDecimal(pii.getProperty(20).toString());
                prod.PRICE_06 = new BigDecimal(pii.getProperty(21).toString());
                prod.PRICE_07 = new BigDecimal(pii.getProperty(22).toString());
                prod.PRICE_08 = new BigDecimal(pii.getProperty(23).toString());
                prod.PRICE_09 = new BigDecimal(pii.getProperty(24).toString());
                prod.PRICE_10 = new BigDecimal(pii.getProperty(25).toString());
                prod.COST = new BigDecimal(pii.getProperty(26).toString());
                prod.AVERAGE_COST = new BigDecimal(pii.getProperty(27).toString());
                prod.DISPLAY_ORDER = Integer.parseInt(pii.getProperty(28).toString());
                prod.POS_DISPLAY = Boolean.getBoolean(pii.getProperty(29).toString());
                prod.ALLOW_ZERO_PRICE = Boolean.getBoolean(pii.getProperty(30).toString());
                prod.IsActive = Boolean.getBoolean(pii.getProperty(31).toString());
                prod.ACTIVE_PERIOD = Boolean.getBoolean(pii.getProperty(32).toString());
                prod.STOCK_UOM = pii.getProperty(33).toString();
                prod.PURCH_UOM = pii.getProperty(34).toString();
                prod.USAGE_UOM = pii.getProperty(35).toString();
                prod.PURCH_CONV = Double.parseDouble(pii.getProperty(36).toString());
                prod.USAGE_CONV = Double.parseDouble(pii.getProperty(37).toString());
                prod.IsBOM = Boolean.getBoolean(pii.getProperty(38).toString());
                prod.IsSERIAL = Boolean.getBoolean(pii.getProperty(39).toString());
                prod.ALLOW_DISC = Boolean.getBoolean(pii.getProperty(40).toString());
                prod.MULTIPLE_DISC = Boolean.getBoolean(pii.getProperty(41).toString());
                prod.MULTIPLE_UOM = Boolean.getBoolean(pii.getProperty(42).toString());
                prod.TICKET_TYPE = pii.getProperty(43).toString();
                prod.STAFF_DISCOUNT_CODE = pii.getProperty(44).toString();
                prod.SUPPLIER_CODE = pii.getProperty(45).toString();
                prod.BARCODE_01 = pii.getProperty(46).toString();
                prod.BARCODE_02 = pii.getProperty(47).toString();
                prod.BARCODE_03 = pii.getProperty(48).toString();
                prod.BARCODE_04 = pii.getProperty(49).toString();
                prod.BARCODE_05 = pii.getProperty(50).toString();
                prod.BARCODE_06 = pii.getProperty(51).toString();
                prod.BARCODE_07 = pii.getProperty(52).toString();
                prod.BARCODE_08 = pii.getProperty(53).toString();
                prod.BARCODE_09 = pii.getProperty(54).toString();
                prod.BARCODE_10 = pii.getProperty(55).toString();
                prod.PRICE_PERCENTAGE_01 = Double.parseDouble(pii.getProperty(56).toString());
                prod.PRICE_PERCENTAGE_02 = Double.parseDouble(pii.getProperty(57).toString());
                prod.PRICE_PERCENTAGE_03 = Double.parseDouble(pii.getProperty(58).toString());
                prod.PRICE_PERCENTAGE_04 = Double.parseDouble(pii.getProperty(59).toString());
                prod.PRICE_PERCENTAGE_05 = Double.parseDouble(pii.getProperty(60).toString());
                prod.PRICE_PERCENTAGE_06 = Double.parseDouble(pii.getProperty(61).toString());
                prod.PRICE_PERCENTAGE_07 = Double.parseDouble(pii.getProperty(62).toString());
                prod.PRICE_PERCENTAGE_08 = Double.parseDouble(pii.getProperty(63).toString());
                prod.PRICE_PERCENTAGE_09 = Double.parseDouble(pii.getProperty(64).toString());
                prod.PRICE_PERCENTAGE_10 = Double.parseDouble(pii.getProperty(65).toString());
                prod.STOCK_TAKE_INTERIM = pii.getProperty(66).toString();
                prod.MODIFIED_ID = pii.getProperty(67).toString();
                prod.IMG_PATH = pii.getProperty(68).toString();
                prod.TAXCODE = pii.getProperty(69).toString();

                productMasters[i] = prod;
            }
            return productMasters;
        } catch (IndexOutOfBoundsException ex) {
            throw ex;
        }
    }

    public static Employee[] RetrieveEmployeesFromSoap(SoapObject soap) {
        try {
            Employee[] employees = new Employee[soap.getPropertyCount()];
            for (int i = 0; i < employees.length; i++) {
                SoapObject pii = (SoapObject) soap.getProperty(i);
                Employee employee = new Employee();
                employee.EMP_CODE = Integer.parseInt(pii.getProperty(0).toString());
                employee.EMP_NAME = pii.getProperty(1).toString();
                employee.EMP_PASSWORD = pii.getProperty(2).toString();
                employee.EMP_LVL = Integer.parseInt(pii.getProperty(3).toString());
                employee.POS_ALLOW = Boolean.getBoolean(pii.getProperty(4).toString());
                employee.VIEW_COST_ALLOW = Boolean.getBoolean(pii.getProperty(5).toString());
                employee.EMP_GROUP_CODE = pii.getProperty(6).toString();
                employee.EMP_GROUP_NAME = pii.getProperty(7).toString();
                employee.IsActive = Boolean.getBoolean(pii.getProperty(8).toString());

                employees[i] = employee;
            }
            return employees;
        } catch (IndexOutOfBoundsException ex) {
            throw ex;
        }
    }

    public static Customer[] RetrieveCustomersFromSoap(SoapObject soap) {
        try {
            Customer[] customers = new Customer[soap.getPropertyCount()];
            for (int i = 0; i < customers.length; i++) {
                SoapObject pii = (SoapObject) soap.getProperty(i);

                Customer customer = new Customer();

                customer.CUSTOMER_CODE = pii.getProperty(0).toString();
                customer.CUSTOMER_NAME = pii.getProperty(1).toString();
                customer.CUSTOMER_GRP_CODE = pii.getProperty(2).toString();
                customer.IC_NO = pii.getProperty(3).toString();
                customer.ADDRESS1 = pii.getProperty(4).toString();
                customer.ADDRESS2 = pii.getProperty(5).toString();
                customer.POS_CODE = pii.getProperty(6).toString();
                customer.COUNTY = pii.getProperty(7).toString();
                customer.STATE = pii.getProperty(8).toString();
                customer.COUNTRY = pii.getProperty(9).toString();
                customer.CONTACT = pii.getProperty(10).toString();
                customer.MOBILE = pii.getProperty(11).toString();
                customer.FAX = pii.getProperty(12).toString();
                customer.EMAIL = pii.getProperty(13).toString();
                customer.IsWHOLESALES = Boolean.getBoolean(pii.getProperty(14).toString());
                customer.IsActive = Boolean.getBoolean(pii.getProperty(15).toString());
                customer.IsMember = Boolean.getBoolean(pii.getProperty(16).toString());
                customer.BYSMS = Boolean.getBoolean(pii.getProperty(17).toString());
                customer.BYEMAIL = Boolean.getBoolean(pii.getProperty(18).toString());
                customer.MODIFIED_ID = pii.getProperty(19).toString();
                customer.TEMP_CUSTOMER_CODE = pii.getProperty(20).toString();
                customer.GST_REG_NO = pii.getProperty(21).toString();
                customer.IsEMPLOYEE = Boolean.getBoolean(pii.getProperty(22).toString());
                customer.PRICE_GRP_CODE = pii.getProperty(23).toString();
                customer.Outlet_Code = pii.getProperty(24).toString();

                customers[i] = customer;
            }
            return customers;
        } catch (IndexOutOfBoundsException ex) {
            throw ex;
        }
    }

    public static Price_Group[] RetrievePrice_GroupFromSoap(SoapObject soap) {
        try {
            Price_Group[] priceGroups = new Price_Group[soap.getPropertyCount()];
            for (int i = 0; i < priceGroups.length; i++) {
                SoapObject pii = (SoapObject) soap.getProperty(i);
                Price_Group priceGroup = new Price_Group();
                priceGroup.PRICE_GRP_CODE = pii.getProperty(0).toString();
                priceGroup.PRICE_GRP_NAME = pii.getProperty(1).toString();
                priceGroup.PROD_CODE = pii.getProperty(2).toString();
                priceGroup.PRICE = new BigDecimal(pii.getProperty(3).toString());

                priceGroups[i] = priceGroup;
            }
            return priceGroups;
        } catch (IndexOutOfBoundsException ex) {
            throw ex;
        }
    }

    public static Payment[] RetrievePaymentFromSoap(SoapObject soap) {
        try {
            Payment[] payments = new Payment[soap.getPropertyCount()];
            for (int i = 0; i < payments.length; i++) {
                SoapObject pii = (SoapObject)soap.getProperty(i);
                Payment payment = new Payment();
                payment.COMPANY_CODE = pii.getProperty(0).toString();
                payment.OUTLET_CODE = pii.getProperty(1).toString();
                payment.EMP_CODE = pii.getProperty(2).toString();
                payment.POS_NO = pii.getProperty(3).toString();
                payment.SHIFT_NO = pii.getProperty(4).toString();
                payment.RCP_NO = pii.getProperty(5).toString();
                payment.TRANS_TYPE = pii.getProperty(6).toString();
                payment.TRANS_TIME = pii.getProperty(7).toString();
                payment.ROW_NUMBER = Integer.parseInt(pii.getProperty(8).toString());
                payment.PAYMENT_CODE = pii.getProperty(9).toString();
                payment.PAYMENT_NAME = pii.getProperty(10).toString();
                payment.PAYMENT_TYPE = pii.getProperty(11).toString();
                payment.FOREX_CODE = pii.getProperty(12).toString();
                payment.FOREX_AMOUNT = new BigDecimal(pii.getProperty(13).toString());
                payment.CARD_NO = pii.getProperty(14).toString();
                payment.CARD_TYPE = pii.getProperty(15).toString();
                payment.BANK_CODE = pii.getProperty(16).toString();
                payment.PAYMENT_AMOUNT = new BigDecimal(pii.getProperty(17).toString());
                payment.CHANGE_AMOUNT = new BigDecimal(pii.getProperty(18).toString());
                payment.TENDER_AMOUNT = new BigDecimal(pii.getProperty(19).toString());
                payment.PAYMT_REMARK = pii.getProperty(20).toString();
                payment.DRAWER_DECLARE_ID = pii.getProperty(21).toString();
                payment.MODIFIED_ID = pii.getProperty(22).toString();
                payment.ToSAP = Boolean.getBoolean(pii.getProperty(23).toString());
                payments[i] = payment;
            }
            return payments;
        } catch (IndexOutOfBoundsException ex) {
            throw ex;
        }
    }

public static Header[] RetrieveHeaderFromSoap(SoapObject soap) {
    try {
        Header[] headers = new Header[soap.getPropertyCount()];
        for (int i = 0; i < headers.length; i++) {
            SoapObject pii = (SoapObject)soap.getProperty(i);
            Header header = new Header();
            header.COMPANY_CODE = pii.getProperty(0).toString();
            header.OUTLET_CODE = pii.getProperty(1).toString();
            header.EMP_CODE = pii.getProperty(2).toString();
            header.POS_NO = pii.getProperty(3).toString();
            header.SHIFT_NO = pii.getProperty(4).toString();
            header.RCP_NO = pii.getProperty(5).toString();
            header.TRANS_TYPE = pii.getProperty(6).toString();
            header.TRANS_TIME = pii.getProperty(7).toString();
            header.SALES_AMOUNT = new BigDecimal(pii.getProperty(8).toString());
            header.TOTAL_TAX = new BigDecimal(pii.getProperty(9).toString());
            header.TOTAL_DISCOUNT = new BigDecimal(pii.getProperty(10).toString());
            header.ROUNDING = new BigDecimal(pii.getProperty(11).toString());
            header.ROUNDING_ADJ = new BigDecimal(pii.getProperty(12).toString());
            header.APPROVAL_ID = pii.getProperty(13).toString();
            header.CUSTOMER_CODE = pii.getProperty(14).toString();
            header.CUSTOMER_POINT = new BigDecimal(pii.getProperty(15).toString());
            header.REFUND_VOUCHER_CODE = pii.getProperty(16).toString();
            header.REFUND_VOUCHER_AMOUNT = new BigDecimal(pii.getProperty(17).toString());
            header.DRAWER_DECLARE_ID = pii.getProperty(18).toString();
            header.BOTRANS_NO = pii.getProperty(19).toString();
            header.MODIFIED_ID = pii.getProperty(20).toString();
            header.ITEM_VOID_COUNT = Integer.parseInt(pii.getProperty(21).toString());
            header.REPRINT_COUNT = Integer.parseInt(pii.getProperty(22).toString());
            header.ITEM_VOID_AMOUNT = new BigDecimal(pii.getProperty(23).toString());
            header.REPRINT_AMOUNT = new BigDecimal(pii.getProperty(24).toString());
            header.PRICE_LEVEL = pii.getProperty(25).toString();
            header.REFUND_POS_NO = pii.getProperty(26).toString();
            header.REFUND_RCP_NO = pii.getProperty(27).toString();
            header.REFUND_REMARK = pii.getProperty(28).toString();
            header.IsFORCE_REFUND = Boolean.getBoolean(pii.getProperty(29).toString());
            header.REPRINTCOUNT = Integer.parseInt(pii.getProperty(30).toString());
            header.ToSAP = Boolean.getBoolean(pii.getProperty(31).toString());
            header.MEMBER_IC = pii.getProperty(32).toString();
            header.PROTRANS_NO = pii.getProperty(33).toString();
            headers[i] = header;
        }
        return headers;
        } catch (IndexOutOfBoundsException ex) {
            throw ex;
        }
    }

    public static Detail[] RetrieveDetailFromSoap(SoapObject soap) {
        try {
            Detail[] details = new Detail[soap.getPropertyCount()];
            for (int i = 0; i < details.length; i++) {
                SoapObject pii = (SoapObject)soap.getProperty(i);
                Detail detail = new Detail();
                detail.COMPANY_CODE = pii.getProperty(0).toString();
                detail.OUTLET_CODE = pii.getProperty(1).toString();
                detail.EMP_CODE = pii.getProperty(2).toString();
                detail.POS_NO = pii.getProperty(3).toString();
                detail.SHIFT_NO = pii.getProperty(4).toString();
                detail.RCP_NO = pii.getProperty(5).toString();
                detail.TRANS_TYPE = pii.getProperty(6).toString();
                detail.TRANS_TIME = pii.getProperty(7).toString();
                detail.ROW_NUMBER = Integer.parseInt(pii.getProperty(8).toString());
                detail.PROD_CODE = pii.getProperty(9).toString();
                detail.PROD_NAME = pii.getProperty(10).toString();
                detail.PROD_TYPE_CODE = pii.getProperty(11).toString();
                detail.USAGE_UOM = pii.getProperty(12).toString();
                detail.QUANTITY = new BigDecimal(pii.getProperty(13).toString());
                detail.UOM_CONV = new BigDecimal(pii.getProperty(14).toString());
                detail.PRICE_LVL_CODE = pii.getProperty(15).toString();
                detail.UNIT_PRICE = new BigDecimal(pii.getProperty(16).toString());
                detail.TOTAL_PRICE = new BigDecimal(pii.getProperty(17).toString());
                detail.TAX_01 = new BigDecimal(pii.getProperty(18).toString());
                detail.TAX_02 = new BigDecimal(pii.getProperty(19).toString());
                detail.TAX_03 = new BigDecimal(pii.getProperty(20).toString());
                detail.TAX_04 = new BigDecimal(pii.getProperty(21).toString());
                detail.TAX_05 = new BigDecimal(pii.getProperty(22).toString());
                detail.DISCOUNT_CODE = pii.getProperty(23).toString();
                detail.ITEM_DISCOUNT_AMOUNT = new BigDecimal(pii.getProperty(24).toString());
                detail.TOTAL_DISCOUNT_CODE = pii.getProperty(25).toString();
                detail.TOTAL_DISCOUNT_AMOUNT = new BigDecimal(pii.getProperty(26).toString());
                detail.TICKET_SURCHARGE = new BigDecimal(pii.getProperty(27).toString());
                detail.STAFF_DISCOUNT_CODE = pii.getProperty(28).toString();
                detail.STAFF_DISCOUNT = new BigDecimal(pii.getProperty(29).toString());
                detail.BARCODE = pii.getProperty(30).toString();
                detail.TAXCODE = pii.getProperty(31).toString();
                detail.COST = new BigDecimal(pii.getProperty(32).toString());
                detail.ToSAP = Boolean.getBoolean(pii.getProperty(33).toString());
                details[i] = detail;    
            }
            return details;
        } catch (IndexOutOfBoundsException ex) {
        throw ex;
        }
    }
}

