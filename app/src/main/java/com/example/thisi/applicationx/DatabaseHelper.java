package com.example.thisi.applicationx;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by thisi on 10/19/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "androidpos.db";

    public static final String EMPLOYEE_TABLE_NAME = "employee";
    public static final String EMPLOYEE_TABLE_CREATE = "CREATE TABLE " + EMPLOYEE_TABLE_NAME + " (" +
            "EMP_CODE nvarchar(20) primary key, " +
            "EMP_NAME nvarchar(100)," +
            "EMP_PASSWORD nvarchar(100)," +
            "EMP_LVL boolean," +
            "POS_ALLOW boolean," +
            "VIEW_COST_ALLOW boolean," +
            "EMP_GROUP_CODE nvarchar(20)," +
            "EMP_GROUP_NAME nvarchar(100)," +
            "IsActive boolean," +
            "MODIFIED_DATE datetime," +
            "MODIFIED_ID nvarchar(20)" +
            ");";

    public static final String CUSTOMER_TABLE_NAME = "customer";
    public static final String CUSTOMER_TABLE_CREATE = "CREATE TABLE " + CUSTOMER_TABLE_NAME + " ( " +
            "CUSTOMER_CODE nvarchar(20) primary key, " +
            "CUSTOMER_NAME nvarchar(100)," +
            "CUSTOMER_GRP_CODE nvarchar(20)," +
            "IC_NO nvarchar(50)," +
            "ADDRESS1 nvarchar(100)," +
            "ADDRESS2 nvarchar(100)," +
            "POS_CODE nvarchar(50)," +
            "COUNTY	nvarchar(100)," +
            "STATE nvarchar(100)," +
            "COUNTRY nvarchar(100)," +
            "CONTACT nvarchar(50)," +
            "MOBILE	nvarchar(50)," +
            "FAX nvarchar(50)," +
            "EMAIL nvarchar(100)," +
            "IsWHOLESALES boolean," +
            "IsActive boolean, " +
            "JOIN_DATE datetime," +
            "EXPIRE_DATE datetime," +
            "IsMember boolean, " +
            "POINT_VALUE numeric(18, 2)," +
            "BYSMS boolean," +
            "BYEMAIL boolean," +
            "MODIFIED_DATE datetime," +
            "MODIFIED_ID nvarchar(20)," +
            "TEMP_CUSTOMER_CODE	nvarchar(20)," +
            "GST_REG_NO	nvarchar(20)," +
            "IsEMPLOYEE	boolean," +
            "PRICE_GRP_CODE	nvarchar(20)," +
            "Outlet_Code nvarchar(20)" +
            ");";

    public static final String PRODUCT_MASTER_TABLE_NAME = "product_master";
    public static final String PRODUCT_MASTER_TABLE_CREATE = "CREATE TABLE " + PRODUCT_MASTER_TABLE_NAME + " ( " +
            "PROD_CODE nvarchar(20) primary key," +
            "PRODUCT_SHORT_NAME	nvarchar(100)," +
            "PROD_NAME nvarchar(500)," +
            "PROD_TYPE_CODE	nvarchar(20)," +
            "BARCODE nvarchar(130)," +
            "PROD_GRP_01 nvarchar(20)," +
            "PROD_GRP_02 nvarchar(20)," +
            "PROD_GRP_03 nvarchar(20)," +
            "PROD_GRP_04 nvarchar(20)," +
            "PROD_GRP_05 nvarchar(20)," +
            "PROD_DISC_GRP nvarchar(20)," +
            "TAX_01	boolean," +
            "TAX_02	boolean," +
            "TAX_03	boolean," +
            "TAX_04	boolean," +
            "TAX_05	boolean," +
            "PRICE_01 numeric(18, 2)," +
            "PRICE_02 numeric(18, 2)," +
            "PRICE_03 numeric(18, 2)," +
            "PRICE_04 numeric(18, 2)," +
            "PRICE_05 numeric(18, 2)," +
            "PRICE_06 numeric(18, 2)," +
            "PRICE_07 numeric(18, 2)," +
            "PRICE_08 numeric(18, 2)," +
            "PRICE_09 numeric(18, 2)," +
            "PRICE_10 numeric(18, 2)," +
            "COST numeric(18, 2)," +
            "AVERAGE_COST numeric(18, 2)," +
            "DISPLAY_ORDER int," +
            "POS_DISPLAY boolean," +
            "ALLOW_ZERO_PRICE boolean," +
            "IsActive boolean," +
            "ACTIVE_PERIOD boolean," +
            "ACTIVE_DATE datetime," +
            "EXPIRE_DATE datetime," +
            "STOCK_UOM nvarchar(50)," +
            "PURCH_UOM nvarchar(50)," +
            "USAGE_UOM nvarchar(50)," +
            "PURCH_CONV	numeric(18, 4)," +
            "USAGE_CONV	numeric(18, 4)," +
            "IsBOM boolean," +
            "IsSERIAL boolean," +
            "ALLOW_DISC	boolean," +
            "MULTIPLE_DISC boolean," +
            "MULTIPLE_UOM boolean," +
            "TICKET_TYPE nvarchar(2)," +
            "STAFF_DISCOUNT_CODE nvarchar(20)," +
            "SUPPLIER_CODE nvarchar(20)," +
            "BARCODE_01	nvarchar(130)," +
            "BARCODE_02	nvarchar(130)," +
            "BARCODE_03	nvarchar(130)," +
            "BARCODE_04	nvarchar(130)," +
            "BARCODE_05	nvarchar(130)," +
            "BARCODE_06	nvarchar(130)," +
            "BARCODE_07	nvarchar(130)," +
            "BARCODE_08	nvarchar(130)," +
            "BARCODE_09	nvarchar(130)," +
            "BARCODE_10	nvarchar(130)," +
            "PRICE_PERCENTAGE_01 numeric(18, 2)," +
            "PRICE_PERCENTAGE_02 numeric(18, 2)," +
            "PRICE_PERCENTAGE_03 numeric(18, 2)," +
            "PRICE_PERCENTAGE_04 numeric(18, 2)," +
            "PRICE_PERCENTAGE_05 numeric(18, 2)," +
            "PRICE_PERCENTAGE_06 numeric(18, 2)," +
            "PRICE_PERCENTAGE_07 numeric(18, 2)," +
            "PRICE_PERCENTAGE_08 numeric(18, 2)," +
            "PRICE_PERCENTAGE_09 numeric(18, 2)," +
            "PRICE_PERCENTAGE_10 numeric(18, 2)," +
            "STOCK_TAKE_INTERIM	nvarchar(50)," +
            "MODIFIED_DATE datetime," +
            "MODIFIED_ID nvarchar(20)," +
            "IMG_PATH nvarchar(4000)," +
            "TAXCODE nvarchar(6)" +
            ");";

    public static final String PRICE_GROUP_TABLE_NAME = "price_group";
    public static final String PRICE_GROUP_TABLE_CREATE = "CREATE TABLE " + PRICE_GROUP_TABLE_NAME + " ( " +
            "PRICE_GRP_CODE	nvarchar(20)," +
            "PRICE_GRP_NAME	nvarchar(500)," +
            "PROD_CODE nvarchar(20)," +
            "PRICE numeric(18, 2)," +
            "MODIFIED_DATE datetime," +
            "MODIFIED_ID nvarchar(20)" +
            "); ";

    public static final String SUSPEND_TABLE_NAME = "suspend";
    public static final String SUSPEND_TABLE_CREATE = "CREATE TABLE " + SUSPEND_TABLE_NAME + " ( " +
            "COMPANY_CODE nvarchar(20)," +
            "OUTLET_CODE nvarchar(20)," +
            "POS_NO	nvarchar(10)," +
            "SHIFT_NO nvarchar(10)," +
            "RCP_NO	nvarchar(10)," +
            "TRANS_TYPE	nvarchar(2)," +
            "BUS_DATE datetime," +
            "TRANS_DATE	datetime," +
            "TRANS_TIME	nvarchar(5)," +
            "ROW_NUMBER	int," +
            "PROD_CODE nvarchar(20)," +
            "BARCODE nvarchar(130)," +
            "PROD_NAME nvarchar(500)," +
            "PROD_SHORT_NAME nvarchar(30)," +
            "PROD_TYPE_CODE	nvarchar(20)," +
            "USAGE_UOM nvarchar(50)," +
            "UOM_CONV numeric(18, 2)," +
            "QUANTITY numeric(18, 4)," +
            "PRICE_LVL_CODE	nvarchar(20)," +
            "UNIT_PRICE	numeric(18, 2)," +
            "TOTAL_PRICE numeric(18, 2)," +
            "BOM_PARENT	nvarchar(20)," +
            "TAX_01	numeric(18, 2)," +
            "TAX_02	numeric(18, 2)," +
            "TAX_03	numeric(18, 2)," +
            "TAX_04	numeric(18, 2)," +
            "TAX_05	numeric(18, 2)," +
            "ALLOW_DISC	boolean," +
            "MULTIPLE_DISC boolean," +
            "DISCOUNT_CODE nvarchar(20)," +
            "ITEM_DISCOUNT_AMOUNT numeric(18, 2)," +
            "TOTAL_DICOUNT_CODE	nvarchar(20)," +
            "TOTAL_DISCOUNT_AMOUNT numeric(18, 2)," +
            "TICKET_SURCHARGE numeric(18, 2)," +
            "STAFF_DISCOUNT_CODE nvarchar(20)," +
            "STAFF_DISCOUNT	numeric(18, 2)," +
            "SUSPEND_NUMBER	nvarchar(20)," +
            "IsRECALL boolean," +
            "IS_UPSALES	boolean," +
            "UPSALES_CONV numeric(18, 2)," +
            "IS_MULTIPLEUOM	boolean," +
            "RECALL_BY nvarchar(20)," +
            "APPROVE_BY	nvarchar(20)," +
            "MODIFIED_DATE datetime," +
            "MODIFIED_ID nvarchar(20)," +
            "CUSTOMER_CODE nvarchar(20)," +
            "TAXCODE nvarchar(6)," +
            "COST numeric(18, 2)," +
            "PRICE_GRP_CODE	nvarchar(20)," +
            "TABLE_NO nvarchar(20)," +
            "PROMOSOURCECODE nvarchar(20)," +
            "PROMOCHANGEPRICE numeric(18, 2)" +
            ");";

    public static final String HEADER_TABLE_NAME = "header";
    public static final String HEADER_TABLE_CREATE = "CREATE TABLE " + HEADER_TABLE_NAME + " ( " +
            "COMPANY_CODE nvarchar(20)," +
            "OUTLET_CODE nvarchar(20)," +
            "EMP_CODE nvarchar(20)," +
            "POS_NO	nvarchar(10)," +
            "SHIFT_NO nvarchar(10)," +
            "RCP_NO	nvarchar(10)," +
            "TRANS_TYPE	nvarchar(2)," +
            "BUS_DATE datetime," +
            "TRANS_DATE	datetime," +
            "TRANS_TIME	nvarchar(5)," +
            "SALES_AMOUNT numeric(18, 2)," +
            "TOTAL_TAX numeric(18, 2)," +
            "TOTAL_DISCOUNT	numeric(18, 2)," +
            "ROUNDING numeric(18, 2)," +
            "ROUNDING_ADJ numeric(18, 2)," +
            "APPROVAL_ID nvarchar(20)," +
            "CUSTOMER_CODE nvarchar(20)," +
            "CUSTOMER_POINT	numeric(18, 2)," +
            "REFUND_VOUCHER_CODE nvarchar(100)," +
            "REFUND_VOUCHER_AMOUNT numeric(18, 2)," +
            "REFUND_VOUCHER_EXPIRE_DATE	datetime," +
            "DRAWER_DECLARE_ID nvarchar(100)," +
            "BOTRANS_NO	nvarchar(20)," +
            "MODIFIED_DATE datetime," +
            "MODIFIED_ID nvarchar(20)," +
            "ITEM_VOID_COUNT int," +
            "REPRINT_COUNT int," +
            "ITEM_VOID_AMOUNT numeric(18, 2)," +
            "REPRINT_AMOUNT numeric(18, 2)," +
            "PRICE_LEVEL nvarchar(20)," +
            "REFUND_POS_NO nvarchar(10)," +
            "REFUND_RCP_NO nvarchar(10)," +
            "REFUND_REMARK nvarchar(4000)," +
            "REFUND_RCP_BUS_DATE datetime," +
            "IsFORCE_REFUND	bit," +
            "REPRINTCOUNT int," +
            "ToSAP	bit," +
            "MEMBER_IC nvarchar(20)," +
            "PROTRANS_NO nvarchar(20)" +
            ");";

    public static final String DETAIL_TABLE_NAME = "detail";
    public static final String DETAIL_TABLE_CREATE = "CREATE TABLE " + DETAIL_TABLE_NAME + " ( " +
            "COMPANY_CODE nvarchar(20)," +
            "OUTLET_CODE nvarchar(20)," +
            "EMP_CODE nvarchar(20)," +
            "POS_NO	nvarchar(10)," +
            "SHIFT_NO nvarchar(10)," +
            "RCP_NO	nvarchar(10)," +
            "TRANS_TYPE	nvarchar(2)," +
            "BUS_DATE datetime," +
            "TRANS_DATE	datetime," +
            "TRANS_TIME	nvarchar(5)," +
            "ROW_NUMBER	int," +
            "PROD_CODE nvarchar(20)," +
            "PROD_NAME nvarchar(30)," +
            "PROD_TYPE_CODE	nvarchar(20)," +
            "USAGE_UOM nvarchar(50)," +
            "QUANTITY numeric(18, 4)," +
            "UOM_CONV numeric(18, 4)," +
            "PRICE_LVL_CODE	nvarchar(20)," +
            "UNIT_PRICE	numeric(18, 2)," +
            "TOTAL_PRICE numeric(18, 2)," +
            "TAX_01	numeric(18, 4)," +
            "TAX_02	numeric(18, 4)," +
            "TAX_03	numeric(18, 4)," +
            "TAX_04	numeric(18, 4)," +
            "TAX_05	numeric(18, 4)," +
            "DISCOUNT_CODE nvarchar(20)," +
            "ITEM_DISCOUNT_AMOUNT numeric(18, 2)," +
            "TOTAL_DISCOUNT_CODE nvarchar(20)," +
            "TOTAL_DISCOUNT_AMOUNT numeric(18, 2)," +
            "TICKET_SURCHARGE numeric(18, 2)," +
            "STAFF_DISCOUNT_CODE nvarchar(20)," +
            "STAFF_DISCOUNT	numeric(18, 2)," +
            "BARCODE nvarchar(130)," +
            "TAXCODE nvarchar(6)," +
            "COST numeric(18, 2)," +
            "ToSAP bit" +
            ");";

    public static final String PAYMENT_TABLE_NAME = "payment";
    public static final String PAYMENT_TABLE_CREATE = "CREATE TABLE " + PAYMENT_TABLE_NAME + " ( " +
            "COMPANY_CODE nvarchar(20)," +
            "OUTLET_CODE nvarchar(20)," +
            "EMP_CODE nvarchar(20)," +
            "POS_NO	nvarchar(10)," +
            "SHIFT_NO nvarchar(10)," +
            "RCP_NO	nvarchar(10)," +
            "TRANS_TYPE	nvarchar(2)," +
            "BUS_DATE datetime," +
            "TRANS_DATE	datetime," +
            "TRANS_TIME	nvarchar(5)," +
            "ROW_NUMBER	int," +
            "PAYMENT_CODE nvarchar(20)," +
            "PAYMENT_NAME nvarchar(100)," +
            "PAYMENT_TYPE nvarchar(20)," +
            "FOREX_CODE	nvarchar(20)," +
            "FOREX_AMOUNT numeric(18, 2)," +
            "CARD_NO nvarchar(20)," +
            "CARD_TYPE nvarchar(100)," +
            "BANK_CODE nvarchar(20)," +
            "PAYMENT_AMOUNT	numeric(18, 2)," +
            "CHANGE_AMOUNT numeric(18, 2)," +
            "TENDER_AMOUNT numeric(18, 2)," +
            "PAYMT_REMARK nvarchar(4000)," +
            "DRAWER_DECLARE_ID nvarchar(100)," +
            "MODIFIED_DATE datetime," +
            "MODIFIED_ID nvarchar(20)," +
            "ToSAP bit" +
            ");";

    public DatabaseHelper(Context context, int version) {
        super(context, DATABASE_NAME, null, version);

    }

    public static DatabaseHelper instance;

    public static synchronized DatabaseHelper getHelper(Context context) {
        if(instance == null)
            instance = new DatabaseHelper(context, 1);

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(EMPLOYEE_TABLE_CREATE);
        db.execSQL(CUSTOMER_TABLE_CREATE);
        db.execSQL(PRODUCT_MASTER_TABLE_CREATE);
        db.execSQL(PRICE_GROUP_TABLE_CREATE);
        db.execSQL(SUSPEND_TABLE_CREATE);
        db.execSQL(HEADER_TABLE_CREATE);
        db.execSQL(DETAIL_TABLE_CREATE);
        db.execSQL(PAYMENT_TABLE_CREATE);

        insertSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + EMPLOYEE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXIST " + CUSTOMER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXIST " + PRODUCT_MASTER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXIST " + PRICE_GROUP_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXIST " + SUSPEND_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXIST " + HEADER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXIST " + DETAIL_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXIST " + PAYMENT_TABLE_NAME);

        onCreate(db);
    }

    public boolean insertAdminUser(SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("EMP_CODE", "sa");
        contentValues.put("EMP_NAME", "Administrator");
        contentValues.put("EMP_PASSWORD", "123");

        long result = db.insert("employee", null, contentValues);

        return result > -1;
    }

    public boolean insertEmployee(SQLiteDatabase db, Employee employee) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("EMP_CODE", employee.EMP_CODE);
        contentValues.put("EMP_NAME", employee.EMP_NAME);
        contentValues.put("EMP_PASSWORD", employee.EMP_PASSWORD);
        contentValues.put("EMP_LVL", employee.EMP_LVL);
        contentValues.put("POS_ALLOW", employee.POS_ALLOW);
        contentValues.put("VIEW_COST_ALLOW", employee.VIEW_COST_ALLOW);
        contentValues.put("EMP_GROUP_CODE", employee.EMP_GROUP_CODE);
        contentValues.put("EMP_GROUP_NAME", employee.EMP_GROUP_NAME);
        contentValues.put("IsActive", employee.IsActive);

        long result = db.insert("employee", null, contentValues);

        return result > -1;
    }

    public boolean insertCustomer(SQLiteDatabase db, Customer customer) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("CUSTOMER_CODE", customer.CUSTOMER_CODE);
        contentValues.put("CUSTOMER_NAME", customer.CUSTOMER_NAME);
        contentValues.put("CUSTOMER_GRP_CODE", customer.CUSTOMER_GRP_CODE);
        contentValues.put("IC_NO", customer.IC_NO);
        contentValues.put("ADDRESS1", customer.ADDRESS1);
        contentValues.put("ADDRESS2", customer.ADDRESS2);
        contentValues.put("POS_CODE", customer.POS_CODE);
        contentValues.put("COUNTY", customer.COUNTY);
        contentValues.put("STATE", customer.STATE);
        contentValues.put("COUNTRY", customer.COUNTRY);
        contentValues.put("CONTACT", customer.CONTACT);
        contentValues.put("MOBILE", customer.MOBILE);
        contentValues.put("FAX", customer.FAX);
        contentValues.put("EMAIL", customer.EMAIL);
        contentValues.put("IsWHOLESALES", customer.IsWHOLESALES);
        contentValues.put("IsActive", customer.IsActive);
        contentValues.put("IsMember", customer.IsMember);
        contentValues.put("BYSMS", customer.BYSMS);
        contentValues.put("BYEMAIL", customer.BYEMAIL);
        contentValues.put("MODIFIED_ID", customer.MODIFIED_ID);
        contentValues.put("TEMP_CUSTOMER_CODE", customer.TEMP_CUSTOMER_CODE);
        contentValues.put("GST_REG_NO", customer.GST_REG_NO);
        contentValues.put("IsEMPLOYEE", customer.IsEMPLOYEE);
        contentValues.put("PRICE_GRP_CODE", customer.PRICE_GRP_CODE);
        contentValues.put("Outlet_Code", customer.Outlet_Code);

        long result = db.insertOrThrow("customer", null, contentValues);

        return result > -1;
    }

    public boolean insertProductMaster(SQLiteDatabase db, Product_Master prod) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("PROD_CODE", prod.PROD_CODE);
        contentValues.put("PRODUCT_SHORT_NAME", prod.PRODUCT_SHORT_NAME);
        contentValues.put("PROD_NAME", prod.PROD_NAME);
        contentValues.put("PROD_TYPE_CODE", prod.PROD_TYPE_CODE);
        contentValues.put("BARCODE", prod.BARCODE);
        contentValues.put("PROD_GRP_01", prod.PROD_GRP_01);
        contentValues.put("PROD_GRP_02", prod.PROD_GRP_02);
        contentValues.put("PROD_GRP_03", prod.PROD_GRP_03);
        contentValues.put("PROD_GRP_04", prod.PROD_GRP_04);
        contentValues.put("PROD_GRP_05", prod.PROD_GRP_05);
        contentValues.put("PROD_DISC_GRP", prod.PROD_DISC_GRP);
        contentValues.put("TAX_01", prod.TAX_01);
        contentValues.put("TAX_02", prod.TAX_02);
        contentValues.put("TAX_03", prod.TAX_03);
        contentValues.put("TAX_04", prod.TAX_04);
        contentValues.put("TAX_05", prod.TAX_05);
        contentValues.put("PRICE_01", prod.PRICE_01.doubleValue());
        contentValues.put("PRICE_02", prod.PRICE_02.doubleValue());
        contentValues.put("PRICE_03", prod.PRICE_03.doubleValue());
        contentValues.put("PRICE_04", prod.PRICE_04.doubleValue());
        contentValues.put("PRICE_05", prod.PRICE_05.doubleValue());
        contentValues.put("PRICE_06", prod.PRICE_06.doubleValue());
        contentValues.put("PRICE_07", prod.PRICE_07.doubleValue());
        contentValues.put("PRICE_08", prod.PRICE_08.doubleValue());
        contentValues.put("PRICE_09", prod.PRICE_09.doubleValue());
        contentValues.put("PRICE_10", prod.PRICE_10.doubleValue());
        contentValues.put("COST", prod.COST.doubleValue());
        contentValues.put("AVERAGE_COST", prod.AVERAGE_COST.doubleValue());
        contentValues.put("DISPLAY_ORDER", prod.DISPLAY_ORDER);
        contentValues.put("POS_DISPLAY", prod.POS_DISPLAY);
        contentValues.put("ALLOW_ZERO_PRICE", prod.ALLOW_ZERO_PRICE);
        contentValues.put("IsActive", prod.IsActive);
        contentValues.put("ACTIVE_PERIOD", prod.ACTIVE_PERIOD);
        contentValues.put("STOCK_UOM", prod.STOCK_UOM);
        contentValues.put("PURCH_UOM", prod.PURCH_UOM);
        contentValues.put("USAGE_UOM", prod.USAGE_UOM);
        contentValues.put("PURCH_CONV", prod.PURCH_CONV);
        contentValues.put("USAGE_CONV", prod.USAGE_CONV);
        contentValues.put("IsBOM", prod.IsBOM);
        contentValues.put("IsSERIAL", prod.IsSERIAL);
        contentValues.put("ALLOW_DISC", prod.ALLOW_DISC);
        contentValues.put("MULTIPLE_DISC", prod.MULTIPLE_DISC);
        contentValues.put("MULTIPLE_UOM", prod.MULTIPLE_UOM);
        contentValues.put("TICKET_TYPE", prod.TICKET_TYPE);
        contentValues.put("STAFF_DISCOUNT_CODE", prod.STAFF_DISCOUNT_CODE);
        contentValues.put("SUPPLIER_CODE", prod.SUPPLIER_CODE);
        contentValues.put("BARCODE_01", prod.BARCODE_01);
        contentValues.put("BARCODE_02", prod.BARCODE_02);
        contentValues.put("BARCODE_03", prod.BARCODE_03);
        contentValues.put("BARCODE_04", prod.BARCODE_04);
        contentValues.put("BARCODE_05", prod.BARCODE_05);
        contentValues.put("BARCODE_06", prod.BARCODE_06);
        contentValues.put("BARCODE_07", prod.BARCODE_07);
        contentValues.put("BARCODE_08", prod.BARCODE_08);
        contentValues.put("BARCODE_09", prod.BARCODE_09);
        contentValues.put("BARCODE_10", prod.BARCODE_10);
        contentValues.put("PRICE_PERCENTAGE_01", prod.PRICE_PERCENTAGE_01);
        contentValues.put("PRICE_PERCENTAGE_02", prod.PRICE_PERCENTAGE_02);
        contentValues.put("PRICE_PERCENTAGE_03", prod.PRICE_PERCENTAGE_03);
        contentValues.put("PRICE_PERCENTAGE_04", prod.PRICE_PERCENTAGE_04);
        contentValues.put("PRICE_PERCENTAGE_05", prod.PRICE_PERCENTAGE_05);
        contentValues.put("PRICE_PERCENTAGE_06", prod.PRICE_PERCENTAGE_06);
        contentValues.put("PRICE_PERCENTAGE_07", prod.PRICE_PERCENTAGE_07);
        contentValues.put("PRICE_PERCENTAGE_08", prod.PRICE_PERCENTAGE_08);
        contentValues.put("PRICE_PERCENTAGE_09", prod.PRICE_PERCENTAGE_09);
        contentValues.put("PRICE_PERCENTAGE_10", prod.PRICE_PERCENTAGE_10);
        contentValues.put("STOCK_TAKE_INTERIM", prod.STOCK_TAKE_INTERIM);
        contentValues.put("MODIFIED_ID", prod.MODIFIED_ID);
        contentValues.put("IMG_PATH", prod.IMG_PATH);
        contentValues.put("TAXCODE", prod.TAXCODE);

        long result = db.insertOrThrow("product_master", null, contentValues);

        return result > -1;
    }

    public boolean insertprice_group(SQLiteDatabase db, Price_Group pric) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("PRICE_GRP_CODE", pric.PRICE_GRP_CODE);
        contentValues.put("PRICE_GRP_NAME", pric.PRICE_GRP_NAME);
        contentValues.put("PROD_CODE", pric.PROD_CODE);
        contentValues.put("PRICE", pric.PRICE.doubleValue());
        long result = db.insertOrThrow("price_group", null, contentValues);
        return result > -1;
    }

    public boolean insertSuspend(SQLiteDatabase db, Suspend Susp) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("COMPANY_CODE", Susp.COMPANY_CODE);
        contentValues.put("OUTLET_CODE", Susp.OUTLET_CODE);
        contentValues.put("POS_NO", Susp.POS_NO);
        contentValues.put("SHIFT_NO", Susp.SHIFT_NO);
        contentValues.put("RCP_NO", Susp.RCP_NO);
        contentValues.put("TRANS_TYPE", Susp.TRANS_TYPE);
        contentValues.put("TRANS_TIME", Susp.TRANS_TIME);
        contentValues.put("ROW_NUMBER", Susp.ROW_NUMBER);
        contentValues.put("PROD_CODE", Susp.PROD_CODE);
        contentValues.put("BARCODE", Susp.BARCODE);
        contentValues.put("PROD_NAME", Susp.PROD_NAME);
        contentValues.put("PROD_SHORT_NAME", Susp.PROD_SHORT_NAME);
        contentValues.put("PROD_TYPE_CODE", Susp.PROD_TYPE_CODE);
        contentValues.put("USAGE_UOM", Susp.USAGE_UOM);
        contentValues.put("UOM_CONV", Susp.UOM_CONV.doubleValue());
        contentValues.put("QUANTITY", Susp.QUANTITY.doubleValue());
        contentValues.put("PRICE_LVL_CODE", Susp.PRICE_LVL_CODE);
        contentValues.put("UNIT_PRICE", Susp.UNIT_PRICE.doubleValue());
        contentValues.put("TOTAL_PRICE", Susp.TOTAL_PRICE.doubleValue());
        contentValues.put("BOM_PARENT", Susp.BOM_PARENT);
        contentValues.put("TAX_01", Susp.TAX_01.doubleValue());
        contentValues.put("TAX_02", Susp.TAX_02.doubleValue());
        contentValues.put("TAX_03", Susp.TAX_03.doubleValue());
        contentValues.put("TAX_04", Susp.TAX_04.doubleValue());
        contentValues.put("TAX_05", Susp.TAX_05.doubleValue());
        contentValues.put("ALLOW_DISC", Susp.ALLOW_DISC);
        contentValues.put("MULTIPLE_DISC", Susp.MULTIPLE_DISC);
        contentValues.put("DISCOUNT_CODE", Susp.DISCOUNT_CODE);
        contentValues.put("ITEM_DISCOUNT_AMOUNT", Susp.ITEM_DISCOUNT_AMOUNT.doubleValue());
        contentValues.put("TOTAL_DICOUNT_CODE", Susp.TOTAL_DICOUNT_CODE);
        contentValues.put("TOTAL_DISCOUNT_AMOUNT", Susp.TOTAL_DISCOUNT_AMOUNT.doubleValue());
        contentValues.put("TICKET_SURCHARGE", Susp.TICKET_SURCHARGE.doubleValue());
        contentValues.put("STAFF_DISCOUNT_CODE", Susp.STAFF_DISCOUNT_CODE);
        contentValues.put("STAFF_DISCOUNT", Susp.STAFF_DISCOUNT.doubleValue());
        contentValues.put("SUSPEND_NUMBER", Susp.SUSPEND_NUMBER);
        contentValues.put("IsRECALL", Susp.IsRECALL);
        contentValues.put("IS_UPSALES", Susp.IS_UPSALES);
        contentValues.put("UPSALES_CONV", Susp.UPSALES_CONV.doubleValue());
        contentValues.put("IS_MULTIPLEUOM", Susp.IS_MULTIPLEUOM);
        contentValues.put("RECALL_BY", Susp.RECALL_BY);
        contentValues.put("APPROVE_BY", Susp.APPROVE_BY);
        contentValues.put("MODIFIED_ID", Susp.MODIFIED_ID);
        contentValues.put("CUSTOMER_CODE", Susp.CUSTOMER_CODE);
        contentValues.put("TAXCODE", Susp.TAXCODE);
        contentValues.put("COST", Susp.COST.doubleValue());
        contentValues.put("PRICE_GRP_CODE", Susp.PRICE_GRP_CODE);
        contentValues.put("TABLE_NO", Susp.TABLE_NO);
        contentValues.put("PROMOSOURCECODE", Susp.PROMOSOURCECODE);
        contentValues.put("PROMOCHANGEPRICE", Susp.PROMOCHANGEPRICE.doubleValue());
        long result = db.insertOrThrow("Suspend", null, contentValues);
        return result > -1;
    }

    public void insertSampleData(SQLiteDatabase db) {
        insertAdminUser(db);

        ContentValues contentValues = new ContentValues();
        contentValues.put("CUSTOMER_CODE", "123");
        contentValues.put("PRICE_GRP_CODE", "333");

        db.insert("customer", null, contentValues);

        ContentValues contentValues2 = new ContentValues();
        contentValues2.put("PRICE_GRP_CODE", "333");
        contentValues2.put("PROD_CODE", "1CAMEL");
        contentValues2.put("PRICE", "300");

        db.insert("price_group", null, contentValues2);

        ContentValues contentValues3 = new ContentValues();
        contentValues3.put("PRICE_GRP_CODE", "333");
        contentValues3.put("PROD_CODE", "2");
        contentValues3.put("PRICE", "450");

        db.insertOrThrow("price_group", null, contentValues3);

        ContentValues contentValues4 = new ContentValues();
        contentValues4.put("CUSTOMER_CODE", "123");
        contentValues4.put("PRICE_GRP_CODE", "333");
        contentValues4.put("PROD_NAME", "Dunhill Ice Menthol Black");
        contentValues4.put("PROD_CODE", "2DUNHILL");

        db.insertOrThrow("suspend", null, contentValues4);

        ContentValues contentValues5 = new ContentValues();
        contentValues5.put("CUSTOMER_CODE", "123");
        contentValues5.put("PRICE_GRP_CODE", "333");
        contentValues5.put("PROD_NAME", "Camel Filters");
        contentValues5.put("PROD_CODE", "1CAMEL");

        db.insertOrThrow("suspend", null, contentValues5);

        ContentValues contentValues5point1 = new ContentValues();
        contentValues5point1.put("CUSTOMER_CODE", "123");
        contentValues5point1.put("PRICE_GRP_CODE", "okc");
        contentValues5point1.put("PROD_NAME", "Winston Reds");
        contentValues5point1.put("PROD_CODE", "3WINSTON");

        db.insertOrThrow("suspend", null, contentValues5point1);

        ContentValues contentValues6 = new ContentValues();
        contentValues6.put("PROD_CODE", "No.123456");
        contentValues6.put("PROD_NAME", "Product from master");
        contentValues6.put("PRICE_01", "1");
        contentValues6.put("PRICE_02", "2");
        contentValues6.put("PRICE_03", "3");
        contentValues6.put("PRICE_04", "4");

        db.insertOrThrow("product_master", null, contentValues6);

        ContentValues contentValues7 = new ContentValues();
        contentValues7.put("PROD_CODE", "95508788");
        contentValues7.put("PROD_NAME", "Product from master 2");
        contentValues6.put("PRICE_01", "1");
        contentValues6.put("PRICE_02", "2");
        contentValues6.put("PRICE_03", "3");
        contentValues6.put("PRICE_04", "4");

        db.insertOrThrow("product_master", null, contentValues7);

        ContentValues contentValues8 = new ContentValues();
        contentValues8.put("PRICE_GRP_CODE", "333");
        contentValues8.put("PROD_CODE", "No.123456");
        contentValues8.put("PRICE", "17");

        db.insertOrThrow("price_group", null, contentValues8);

        ContentValues contentValues9 = new ContentValues();
        contentValues9.put("PROD_CODE", "1CAMEL");
        contentValues9.put("PROD_NAME", "Camel Filters");
        contentValues9.put("PRICE_01", "1");
        contentValues9.put("PRICE_02", "2");
        contentValues9.put("PRICE_03", "3");
        contentValues9.put("PRICE_04", "4");

        db.insertOrThrow("product_master", null, contentValues9);

        ContentValues contentValues10 = new ContentValues();
        contentValues10.put("PROD_CODE", "3WINSTON");
        contentValues10.put("PROD_NAME", "Winston Reds");
        contentValues10.put("PRICE_01", "1");
        contentValues10.put("PRICE_02", "2");
        contentValues10.put("PRICE_03", "3");
        contentValues10.put("PRICE_04", "4");

        db.insertOrThrow("product_master", null, contentValues10);

        ContentValues contentValues11 = new ContentValues();
        contentValues11.put("PROD_CODE", "2DUNHILL");
        contentValues11.put("PROD_NAME", "Dunhill Ice Menthol Black");
        contentValues11.put("PRICE_01", "1");
        contentValues11.put("PRICE_02", "2");
        contentValues11.put("PRICE_03", "3");
        contentValues11.put("PRICE_04", "4");

        db.insertOrThrow("product_master", null, contentValues11);
    }

    public boolean loginEmployee(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        db.beginTransaction();
        try {
            Cursor res = db.rawQuery("SELECT * FROM " + EMPLOYEE_TABLE_NAME + " WHERE EMP_CODE = '" + username + "' AND EMP_PASSWORD = '" + password + "';", null);

            db.setTransactionSuccessful();

            return res.getCount() > 0;
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }

        return false;
    }
}
