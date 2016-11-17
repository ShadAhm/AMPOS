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
            "EMP_CODE TEXT primary key, " +
            "EMP_NAME TEXT," +
            "EMP_PASSWORD TEXT," +
            "EMP_LVL boolean," +
            "POS_ALLOW boolean," +
            "VIEW_COST_ALLOW boolean," +
            "EMP_GROUP_CODE TEXT," +
            "EMP_GROUP_NAME TEXT," +
            "IsActive boolean," +
            "MODIFIED_DATE TEXT," +
            "MODIFIED_ID TEXT" +
            ");";

    public static final String CUSTOMER_TABLE_NAME = "customer";
    public static final String CUSTOMER_TABLE_CREATE = "CREATE TABLE " + CUSTOMER_TABLE_NAME + " ( " +
            "CUSTOMER_CODE TEXT primary key, " +
            "CUSTOMER_NAME TEXT," +
            "CUSTOMER_GRP_CODE TEXT," +
            "IC_NO TEXT," +
            "ADDRESS1 TEXT," +
            "ADDRESS2 TEXT," +
            "POS_CODE TEXT," +
            "COUNTY	TEXT," +
            "STATE TEXT," +
            "COUNTRY TEXT," +
            "CONTACT TEXT," +
            "MOBILE	TEXT," +
            "FAX TEXT," +
            "EMAIL TEXT," +
            "IsWHOLESALES boolean," +
            "IsActive boolean, " +
            "JOIN_DATE TEXT," +
            "EXPIRE_DATE TEXT," +
            "IsMember boolean, " +
            "POINT_VALUE numeric(18, 2)," +
            "BYSMS boolean," +
            "BYEMAIL boolean," +
            "MODIFIED_DATE TEXT," +
            "MODIFIED_ID TEXT," +
            "TEMP_CUSTOMER_CODE	TEXT," +
            "GST_REG_NO	TEXT," +
            "IsEMPLOYEE	boolean," +
            "PRICE_GRP_CODE	TEXT," +
            "Outlet_Code TEXT" +
            ");";

    public static final String PRODUCT_MASTER_TABLE_NAME = "product_master";
    public static final String PRODUCT_MASTER_TABLE_CREATE = "CREATE TABLE " + PRODUCT_MASTER_TABLE_NAME + " ( " +
            "PROD_CODE TEXT primary key," +
            "PRODUCT_SHORT_NAME	TEXT," +
            "PROD_NAME TEXT," +
            "PROD_TYPE_CODE	TEXT," +
            "BARCODE TEXT," +
            "PROD_GRP_01 TEXT," +
            "PROD_GRP_02 TEXT," +
            "PROD_GRP_03 TEXT," +
            "PROD_GRP_04 TEXT," +
            "PROD_GRP_05 TEXT," +
            "PROD_DISC_GRP TEXT," +
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
            "ACTIVE_DATE TEXT," +
            "EXPIRE_DATE TEXT," +
            "STOCK_UOM TEXT," +
            "PURCH_UOM TEXT," +
            "USAGE_UOM TEXT," +
            "PURCH_CONV	numeric(18, 4)," +
            "USAGE_CONV	numeric(18, 4)," +
            "IsBOM boolean," +
            "IsSERIAL boolean," +
            "ALLOW_DISC	boolean," +
            "MULTIPLE_DISC boolean," +
            "MULTIPLE_UOM boolean," +
            "TICKET_TYPE TEXT," +
            "STAFF_DISCOUNT_CODE TEXT," +
            "SUPPLIER_CODE TEXT," +
            "BARCODE_01	TEXT," +
            "BARCODE_02	TEXT," +
            "BARCODE_03	TEXT," +
            "BARCODE_04	TEXT," +
            "BARCODE_05	TEXT," +
            "BARCODE_06	TEXT," +
            "BARCODE_07	TEXT," +
            "BARCODE_08	TEXT," +
            "BARCODE_09	TEXT," +
            "BARCODE_10	TEXT," +
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
            "STOCK_TAKE_INTERIM	TEXT," +
            "MODIFIED_DATE TEXT," +
            "MODIFIED_ID TEXT," +
            "IMG_PATH TEXT," +
            "TAXCODE TEXT" +
            ");";

    public static final String PRICE_GROUP_TABLE_NAME = "price_group";
    public static final String PRICE_GROUP_TABLE_CREATE = "CREATE TABLE " + PRICE_GROUP_TABLE_NAME + " ( " +
            "PRICE_GRP_CODE	TEXT," +
            "PRICE_GRP_NAME	TEXT," +
            "PROD_CODE TEXT," +
            "PRICE numeric(18, 2)," +
            "MODIFIED_DATE TEXT," +
            "MODIFIED_ID TEXT" +
            "); ";

    public static final String SUSPEND_TABLE_NAME = "suspend";
    public static final String SUSPEND_TABLE_CREATE = "CREATE TABLE " + SUSPEND_TABLE_NAME + " ( " +
            "COMPANY_CODE TEXT," +
            "OUTLET_CODE TEXT," +
            "POS_NO	TEXT," +
            "SHIFT_NO TEXT," +
            "RCP_NO	TEXT," +
            "TRANS_TYPE	TEXT," +
            "BUS_DATE TEXT," +
            "TRANS_DATE	TEXT," +
            "TRANS_TIME	TEXT," +
            "ROW_NUMBER	int," +
            "PROD_CODE TEXT," +
            "BARCODE TEXT," +
            "PROD_NAME TEXT," +
            "PROD_SHORT_NAME TEXT," +
            "PROD_TYPE_CODE	TEXT," +
            "USAGE_UOM TEXT," +
            "UOM_CONV numeric(18, 2)," +
            "QUANTITY numeric(18, 4)," +
            "PRICE_LVL_CODE	TEXT," +
            "UNIT_PRICE	numeric(18, 2)," +
            "TOTAL_PRICE numeric(18, 2)," +
            "BOM_PARENT	TEXT," +
            "TAX_01	numeric(18, 2)," +
            "TAX_02	numeric(18, 2)," +
            "TAX_03	numeric(18, 2)," +
            "TAX_04	numeric(18, 2)," +
            "TAX_05	numeric(18, 2)," +
            "ALLOW_DISC	boolean," +
            "MULTIPLE_DISC boolean," +
            "DISCOUNT_CODE TEXT," +
            "ITEM_DISCOUNT_AMOUNT numeric(18, 2)," +
            "TOTAL_DICOUNT_CODE	TEXT," +
            "TOTAL_DISCOUNT_AMOUNT numeric(18, 2)," +
            "TICKET_SURCHARGE numeric(18, 2)," +
            "STAFF_DISCOUNT_CODE TEXT," +
            "STAFF_DISCOUNT	numeric(18, 2)," +
            "SUSPEND_NUMBER	TEXT," +
            "IsRECALL boolean," +
            "IS_UPSALES	boolean," +
            "UPSALES_CONV numeric(18, 2)," +
            "IS_MULTIPLEUOM	boolean," +
            "RECALL_BY TEXT," +
            "APPROVE_BY	TEXT," +
            "MODIFIED_DATE TEXT," +
            "MODIFIED_ID TEXT," +
            "CUSTOMER_CODE TEXT," +
            "TAXCODE TEXT," +
            "COST numeric(18, 2)," +
            "PRICE_GRP_CODE	TEXT," +
            "TABLE_NO TEXT," +
            "PROMOSOURCECODE TEXT," +
            "PROMOCHANGEPRICE numeric(18, 2)," +
            "IsNewInDevice int" +
            ");";

    public static final String HEADER_TABLE_NAME = "Header";
    public static final String HEADER_TABLE_CREATE = "CREATE TABLE " + HEADER_TABLE_NAME + " ( " +
            "COMPANY_CODE TEXT," +
            "OUTLET_CODE TEXT," +
            "EMP_CODE TEXT," +
            "POS_NO	TEXT," +
            "SHIFT_NO TEXT," +
            "RCP_NO	TEXT," +
            "TRANS_TYPE	TEXT," +
            "BUS_DATE TEXT," +
            "TRANS_DATE	TEXT," +
            "TRANS_TIME	TEXT," +
            "SALES_AMOUNT numeric(18, 2)," +
            "TOTAL_TAX numeric(18, 2)," +
            "TOTAL_DISCOUNT	numeric(18, 2)," +
            "ROUNDING numeric(18, 2)," +
            "ROUNDING_ADJ numeric(18, 2)," +
            "APPROVAL_ID TEXT," +
            "CUSTOMER_CODE TEXT," +
            "CUSTOMER_POINT	numeric(18, 2)," +
            "REFUND_VOUCHER_CODE TEXT," +
            "REFUND_VOUCHER_AMOUNT numeric(18, 2)," +
            "REFUND_VOUCHER_EXPIRE_DATE	TEXT," +
            "DRAWER_DECLARE_ID TEXT," +
            "BOTRANS_NO	TEXT," +
            "MODIFIED_DATE TEXT," +
            "MODIFIED_ID TEXT," +
            "ITEM_VOID_COUNT int," +
            "REPRINT_COUNT int," +
            "ITEM_VOID_AMOUNT numeric(18, 2)," +
            "REPRINT_AMOUNT numeric(18, 2)," +
            "PRICE_LEVEL TEXT," +
            "REFUND_POS_NO TEXT," +
            "REFUND_RCP_NO TEXT," +
            "REFUND_REMARK TEXT," +
            "REFUND_RCP_BUS_DATE TEXT," +
            "IsFORCE_REFUND	bit," +
            "REPRINTCOUNT int," +
            "ToSAP	bit," +
            "MEMBER_IC TEXT," +
            "PROTRANS_NO TEXT," +
            "IsNewInDevice int" +
            ");";

    public static final String DETAIL_TABLE_NAME = "detail";
    public static final String DETAIL_TABLE_CREATE = "CREATE TABLE " + DETAIL_TABLE_NAME + " ( " +
            "COMPANY_CODE TEXT," +
            "OUTLET_CODE TEXT," +
            "EMP_CODE TEXT," +
            "POS_NO	TEXT," +
            "SHIFT_NO TEXT," +
            "RCP_NO	TEXT," +
            "TRANS_TYPE	TEXT," +
            "BUS_DATE TEXT," +
            "TRANS_DATE	TEXT," +
            "TRANS_TIME	TEXT," +
            "ROW_NUMBER	int," +
            "PROD_CODE TEXT," +
            "PROD_NAME TEXT," +
            "PROD_TYPE_CODE	TEXT," +
            "USAGE_UOM TEXT," +
            "QUANTITY numeric(18, 4)," +
            "UOM_CONV numeric(18, 4)," +
            "PRICE_LVL_CODE	TEXT," +
            "UNIT_PRICE	numeric(18, 2)," +
            "TOTAL_PRICE numeric(18, 2)," +
            "TAX_01	numeric(18, 4)," +
            "TAX_02	numeric(18, 4)," +
            "TAX_03	numeric(18, 4)," +
            "TAX_04	numeric(18, 4)," +
            "TAX_05	numeric(18, 4)," +
            "DISCOUNT_CODE TEXT," +
            "ITEM_DISCOUNT_AMOUNT numeric(18, 2)," +
            "TOTAL_DISCOUNT_CODE TEXT," +
            "TOTAL_DISCOUNT_AMOUNT numeric(18, 2)," +
            "TICKET_SURCHARGE numeric(18, 2)," +
            "STAFF_DISCOUNT_CODE TEXT," +
            "STAFF_DISCOUNT	numeric(18, 2)," +
            "BARCODE TEXT," +
            "TAXCODE TEXT," +
            "COST numeric(18, 2)," +
            "ToSAP bit," +
            "IsNewInDevice int" +
            ");";

    public static final String PAYMENT_TABLE_NAME = "payment";
    public static final String PAYMENT_TABLE_CREATE = "CREATE TABLE " + PAYMENT_TABLE_NAME + " ( " +
            "COMPANY_CODE TEXT," +
            "OUTLET_CODE TEXT," +
            "EMP_CODE TEXT," +
            "POS_NO	TEXT," +
            "SHIFT_NO TEXT," +
            "RCP_NO	TEXT," +
            "TRANS_TYPE	TEXT," +
            "BUS_DATE TEXT," +
            "TRANS_DATE	TEXT," +
            "TRANS_TIME	TEXT," +
            "ROW_NUMBER	int," +
            "PAYMENT_CODE TEXT," +
            "PAYMENT_NAME TEXT," +
            "PAYMENT_TYPE TEXT," +
            "FOREX_CODE	TEXT," +
            "FOREX_AMOUNT numeric(18, 2)," +
            "CARD_NO TEXT," +
            "CARD_TYPE TEXT," +
            "BANK_CODE TEXT," +
            "PAYMENT_AMOUNT	numeric(18, 2)," +
            "CHANGE_AMOUNT numeric(18, 2)," +
            "TENDER_AMOUNT numeric(18, 2)," +
            "PAYMT_REMARK TEXT," +
            "DRAWER_DECLARE_ID TEXT," +
            "MODIFIED_DATE TEXT," +
            "MODIFIED_ID TEXT," +
            "ToSAP bit," +
            "IsNewInDevice int" +
            ");";

    public static final String POS_CONTROL_TABLE_NAME = "POS_control"; 
    public static final String POS_CONTROL_TABLE_CREATE =  "CREATE TABLE " + POS_CONTROL_TABLE_NAME + "( " +
            "COMPANY_CODE TEXT NOT NULL,  " +
            "OUTLET_CODE TEXT NOT NULL,  " +
            "POS_NO TEXT NOT NULL,  " +
            "BUS_DATE TEXT NOT NULL, " +
            "SHIFT_NUMBER INT,  " +
            "EMP_CD TEXT, " +
            "LAST_RCP TEXT,  " +
            "LAST_SUSPEND_NUMBER TEXT,  " +
            "REPRINT_COUNT INT NOT NULL,  " +
            "DAYEND INT NOT NULL  " +
            ");";

    public static final String SHIFT_MASTER_TABLE_NAME = "SHIFT_MASTER";
    public static final String SHIFT_MASTER_TABLE_CREATE = "CREATE TABLE " + SHIFT_MASTER_TABLE_NAME + "( " +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " + 
            "COMPANY_CODE TEXT,  " +
            "OUTLET_CODE TEXT,  " +
            "POS_NO TEXT,  " +
            "BUS_DATE TEXT,  " +
            "SHIFT_NUMBER int, " +
            "SHIFT_STATUS TEXT,  " +
            "SHIFT_START_AMT REAL, " +
            "SHIFT_END_AMT REAL " +
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
        db.execSQL(POS_CONTROL_TABLE_CREATE);
        db.execSQL(SHIFT_MASTER_TABLE_CREATE);

        insertAdminUser(db);
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
        db.execSQL("DROP TABLE IF EXIST " + SHIFT_MASTER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXIST " + POS_CONTROL_TABLE_NAME);        

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

    public boolean insertHeader(SQLiteDatabase db, Header heada) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("COMPANY_CODE", heada.COMPANY_CODE);
        contentValues.put("OUTLET_CODE", heada.OUTLET_CODE);
        contentValues.put("EMP_CODE", heada.EMP_CODE);
        contentValues.put("POS_NO", heada.POS_NO);
        contentValues.put("SHIFT_NO", heada.SHIFT_NO);
        contentValues.put("RCP_NO", heada.RCP_NO);
        contentValues.put("TRANS_TYPE", heada.TRANS_TYPE);
        contentValues.put("TRANS_TIME", heada.TRANS_TIME);
        contentValues.put("SALES_AMOUNT", heada.SALES_AMOUNT.doubleValue());
        contentValues.put("TOTAL_TAX", heada.TOTAL_TAX.doubleValue());
        contentValues.put("TOTAL_DISCOUNT", heada.TOTAL_DISCOUNT.doubleValue());
        contentValues.put("ROUNDING", heada.ROUNDING.doubleValue());
        contentValues.put("ROUNDING_ADJ", heada.ROUNDING_ADJ.doubleValue());
        contentValues.put("APPROVAL_ID", heada.APPROVAL_ID);
        contentValues.put("CUSTOMER_CODE", heada.CUSTOMER_CODE);
        contentValues.put("CUSTOMER_POINT", heada.CUSTOMER_POINT.doubleValue());
        contentValues.put("REFUND_VOUCHER_CODE", heada.REFUND_VOUCHER_CODE);
        contentValues.put("REFUND_VOUCHER_AMOUNT", heada.REFUND_VOUCHER_AMOUNT.doubleValue());
        contentValues.put("DRAWER_DECLARE_ID", heada.DRAWER_DECLARE_ID);
        contentValues.put("BOTRANS_NO", heada.BOTRANS_NO);
        contentValues.put("MODIFIED_ID", heada.MODIFIED_ID);
        contentValues.put("ITEM_VOID_COUNT", heada.ITEM_VOID_COUNT);
        contentValues.put("REPRINT_COUNT", heada.REPRINT_COUNT);
        contentValues.put("ITEM_VOID_AMOUNT", heada.ITEM_VOID_AMOUNT.doubleValue());
        contentValues.put("REPRINT_AMOUNT", heada.REPRINT_AMOUNT.doubleValue());
        contentValues.put("PRICE_LEVEL", heada.PRICE_LEVEL);
        contentValues.put("REFUND_POS_NO", heada.REFUND_POS_NO);
        contentValues.put("REFUND_RCP_NO", heada.REFUND_RCP_NO);
        contentValues.put("REFUND_REMARK", heada.REFUND_REMARK);
        contentValues.put("IsFORCE_REFUND", heada.IsFORCE_REFUND);
        contentValues.put("REPRINTCOUNT", heada.REPRINTCOUNT);
        contentValues.put("ToSAP", heada.ToSAP);
        contentValues.put("MEMBER_IC", heada.MEMBER_IC);
        contentValues.put("PROTRANS_NO", heada.PROTRANS_NO);
        long result = db.insertOrThrow("Header", null, contentValues);
        return result > -1;
    }

    public boolean insertDetail(SQLiteDatabase db, Detail deta) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("COMPANY_CODE", deta.COMPANY_CODE);
        contentValues.put("OUTLET_CODE", deta.OUTLET_CODE);
        contentValues.put("EMP_CODE", deta.EMP_CODE);
        contentValues.put("POS_NO", deta.POS_NO);
        contentValues.put("SHIFT_NO", deta.SHIFT_NO);
        contentValues.put("RCP_NO", deta.RCP_NO);
        contentValues.put("TRANS_TYPE", deta.TRANS_TYPE);
        contentValues.put("TRANS_TIME", deta.TRANS_TIME);
        contentValues.put("ROW_NUMBER", deta.ROW_NUMBER);
        contentValues.put("PROD_CODE", deta.PROD_CODE);
        contentValues.put("PROD_NAME", deta.PROD_NAME);
        contentValues.put("PROD_TYPE_CODE", deta.PROD_TYPE_CODE);
        contentValues.put("USAGE_UOM", deta.USAGE_UOM);
        contentValues.put("QUANTITY", deta.QUANTITY.doubleValue());
        contentValues.put("UOM_CONV", deta.UOM_CONV.doubleValue());
        contentValues.put("PRICE_LVL_CODE", deta.PRICE_LVL_CODE);
        contentValues.put("UNIT_PRICE", deta.UNIT_PRICE.doubleValue());
        contentValues.put("TOTAL_PRICE", deta.TOTAL_PRICE.doubleValue());
        contentValues.put("TAX_01", deta.TAX_01.doubleValue());
        contentValues.put("TAX_02", deta.TAX_02.doubleValue());
        contentValues.put("TAX_03", deta.TAX_03.doubleValue());
        contentValues.put("TAX_04", deta.TAX_04.doubleValue());
        contentValues.put("TAX_05", deta.TAX_05.doubleValue());
        contentValues.put("DISCOUNT_CODE", deta.DISCOUNT_CODE);
        contentValues.put("ITEM_DISCOUNT_AMOUNT", deta.ITEM_DISCOUNT_AMOUNT.doubleValue());
        contentValues.put("TOTAL_DISCOUNT_CODE", deta.TOTAL_DISCOUNT_CODE);
        contentValues.put("TOTAL_DISCOUNT_AMOUNT", deta.TOTAL_DISCOUNT_AMOUNT.doubleValue());
        contentValues.put("TICKET_SURCHARGE", deta.TICKET_SURCHARGE.doubleValue());
        contentValues.put("STAFF_DISCOUNT_CODE", deta.STAFF_DISCOUNT_CODE);
        contentValues.put("STAFF_DISCOUNT", deta.STAFF_DISCOUNT.doubleValue());
        contentValues.put("BARCODE", deta.BARCODE);
        contentValues.put("TAXCODE", deta.TAXCODE);
        contentValues.put("COST", deta.COST.doubleValue());
        contentValues.put("ToSAP", deta.ToSAP);
        long result = db.insertOrThrow("Detail", null, contentValues);
        return result > -1;
    }

    public boolean insertPayment(SQLiteDatabase db, Payment paym) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("COMPANY_CODE", paym.COMPANY_CODE);
        contentValues.put("OUTLET_CODE", paym.OUTLET_CODE);
        contentValues.put("EMP_CODE", paym.EMP_CODE);
        contentValues.put("POS_NO", paym.POS_NO);
        contentValues.put("SHIFT_NO", paym.SHIFT_NO);
        contentValues.put("RCP_NO", paym.RCP_NO);
        contentValues.put("TRANS_TYPE", paym.TRANS_TYPE);
        contentValues.put("TRANS_TIME", paym.TRANS_TIME);
        contentValues.put("ROW_NUMBER", paym.ROW_NUMBER);
        contentValues.put("PAYMENT_CODE", paym.PAYMENT_CODE);
        contentValues.put("PAYMENT_NAME", paym.PAYMENT_NAME);
        contentValues.put("PAYMENT_TYPE", paym.PAYMENT_TYPE);
        contentValues.put("FOREX_CODE", paym.FOREX_CODE);
        contentValues.put("FOREX_AMOUNT", paym.FOREX_AMOUNT.doubleValue());
        contentValues.put("CARD_NO", paym.CARD_NO);
        contentValues.put("CARD_TYPE", paym.CARD_TYPE);
        contentValues.put("BANK_CODE", paym.BANK_CODE);
        contentValues.put("PAYMENT_AMOUNT", paym.PAYMENT_AMOUNT.doubleValue());
        contentValues.put("CHANGE_AMOUNT", paym.CHANGE_AMOUNT.doubleValue());
        contentValues.put("TENDER_AMOUNT", paym.TENDER_AMOUNT.doubleValue());
        contentValues.put("PAYMT_REMARK", paym.PAYMT_REMARK);
        contentValues.put("DRAWER_DECLARE_ID", paym.DRAWER_DECLARE_ID);
        contentValues.put("MODIFIED_ID", paym.MODIFIED_ID);
        contentValues.put("ToSAP", paym.ToSAP);
        long result = db.insertOrThrow("Payment", null, contentValues);
        return result > -1;
    }

    public boolean insertShiftMaster(SQLiteDatabase db, Shift_Master shmst) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("COMPANY_CODE", shmst.COMPANY_CODE);
        contentValues.put("OUTLET_CODE", shmst.OUTLET_CODE);
        contentValues.put("POS_NO", shmst.POS_NO);
        contentValues.put("BUS_DATE", shmst.BUS_DATE);
        contentValues.put("SHIFT_NUMBER", shmst.SHIFT_NUMBER);
        contentValues.put("SHIFT_STATUS", shmst.SHIFT_STATUS);
        contentValues.put("SHIFT_START_AMT", shmst.SHIFT_START_AMT.doubleValue());
        contentValues.put("SHIFT_END_AMT", shmst.SHIFT_END_AMT.doubleValue());
        long result = db.insertOrThrow("Shift_Master", null, contentValues);
        return result > -1;
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

    public boolean thereExistSuspends() {
        SQLiteDatabase db = this.getReadableDatabase();

        db.beginTransaction();
        try {
            Cursor res = db.rawQuery("SELECT * FROM " + SUSPEND_TABLE_NAME + ";", null);

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

    public boolean deleteAndInsertPOSControl(SQLiteDatabase db, POS_Control pctrl) {
        db.execSQL("DELETE FROM POS_Control");

        ContentValues contentValues = new ContentValues();
        contentValues.put("COMPANY_CODE", pctrl.COMPANY_CODE);
        contentValues.put("OUTLET_CODE", pctrl.OUTLET_CODE);
        contentValues.put("POS_NO", pctrl.POS_NO);
        contentValues.put("BUS_DATE", pctrl.BUS_DATE);
        contentValues.put("SHIFT_NUMBER", pctrl.SHIFT_NUMBER);
        contentValues.put("EMP_CD", pctrl.EMP_CD);
        contentValues.put("LAST_RCP", pctrl.LAST_RCP);
        contentValues.put("LAST_SUSPEND_NUMBER", pctrl.LAST_SUSPEND_NUMBER);
        contentValues.put("REPRINT_COUNT", pctrl.REPRINT_COUNT);

        int dayend = pctrl.DAYEND ? 1 : 0; 
        contentValues.put("DAYEND", dayend);

        long result = db.insertOrThrow("POS_Control", null, contentValues);
        return result > -1;
    }

    public void clearOldData(SQLiteDatabase db) {
        db.execSQL("DELETE FROM customer");
        db.execSQL("DELETE FROM product_master");
        db.execSQL("DELETE FROM price_group");
    }

    public void clearEmployeeTable(SQLiteDatabase db) {
        db.execSQL("DELETE FROM employee"); 
    }

    public POS_Control getPOSControl(SQLiteDatabase db) {
        Cursor res = db.rawQuery("SELECT * FROM " + POS_CONTROL_TABLE_NAME + " LIMIT 1;", null);

        if(res.getCount() > 0) {
            res.moveToFirst(); 

            POS_Control pctrl = new POS_Control();
            pctrl.COMPANY_CODE = res.getString(res.getColumnIndex("COMPANY_CODE")); 
            pctrl.OUTLET_CODE = res.getString(res.getColumnIndex("OUTLET_CODE"));  
            pctrl.POS_NO = res.getString(res.getColumnIndex("POS_NO"));
            pctrl.BUS_DATE = res.getString(res.getColumnIndex("BUS_DATE"));
            pctrl.SHIFT_NUMBER = res.getInt(res.getColumnIndex("SHIFT_NUMBER"));
            pctrl.EMP_CD = res.getString(res.getColumnIndex("EMP_CD"));
            pctrl.LAST_RCP = res.getString(res.getColumnIndex("LAST_RCP"));
            pctrl.LAST_SUSPEND_NUMBER = res.getString(res.getColumnIndex("LAST_SUSPEND_NUMBER"));
            pctrl.REPRINT_COUNT = res.getString(res.getColumnIndex("REPRINT_COUNT"));
            
            int dayend = res.getInt(res.getColumnIndex("DAYEND"));
            pctrl.DAYEND = dayend == 1; 
            
            res.close(); 

            return pctrl; 
        } else {
            return null; 
        }
    }

    public Shift_Master lookForOpenShiftsAtDate(SQLiteDatabase db, String todaysDate) {
        String qry = "SELECT * FROM " SHIFT_MASTER_TABLE_NAME + " " +
            "WHERE BUS_DATE = '" + todaysDate + "' AND " +
            "SHIFT_STATUS = 'O' ORDER BY SHIFT_NUMBER DESC LIMIT 1;";

        Cursor res = db.rawQuery(qry, null);

        if(res.getCount() > 0) {
            res.moveToFirst(); 

            Shift_Master smst = new Shift_Master();
            smst.ID = res.getInt(res.getColumnIndex("ID"));            
            smst.COMPANY_CODE = res.getString(res.getColumnIndex("COMPANY_CODE")); 
            smst.OUTLET_CODE = res.getString(res.getColumnIndex("OUTLET_CODE"));  
            smst.POS_NO = res.getString(res.getColumnIndex("POS_NO"));
            smst.BUS_DATE = res.getString(res.getColumnIndex("BUS_DATE"));
            smst.SHIFT_NUMBER = res.getInt(res.getColumnIndex("SHIFT_NUMBER"));
            smst.SHIFT_STATUS = res.getString(res.getColumnIndex("SHIFT_STATUS"));
            smst.SHIFT_START_AMT = res.getDouble(res.getColumnIndex("SHIFT_START_AMT"));
            smst.SHIFT_END_AMT = res.getDouble(res.getColumnIndex("SHIFT_END_AMT"));
            
            res.close(); 

            return smst; 
        } else {
            return null; 
        }
    }

    public Shift_Master lookForLatestShiftAtDate(SQLiteDatabase db, String todaysDate) {
        String qry = "SELECT * FROM " SHIFT_MASTER_TABLE_NAME + " " +
            "WHERE BUS_DATE = '" + todaysDate + "' " +
            "ORDER BY SHIFT_NUMBER DESC LIMIT 1;";

        Cursor res = db.rawQuery(qry, null);

        if(res.getCount() > 0) {
            res.moveToFirst(); 

            Shift_Master smst = new Shift_Master();
            smst.ID = res.getInt(res.getColumnIndex("ID"));   
            smst.COMPANY_CODE = res.getString(res.getColumnIndex("COMPANY_CODE")); 
            smst.OUTLET_CODE = res.getString(res.getColumnIndex("OUTLET_CODE"));  
            smst.POS_NO = res.getString(res.getColumnIndex("POS_NO"));
            smst.BUS_DATE = res.getString(res.getColumnIndex("BUS_DATE"));
            smst.SHIFT_NUMBER = res.getInt(res.getColumnIndex("SHIFT_NUMBER"));
            smst.SHIFT_STATUS = res.getString(res.getColumnIndex("SHIFT_STATUS"));
            smst.SHIFT_START_AMT = res.getDouble(res.getColumnIndex("SHIFT_START_AMT"));
            smst.SHIFT_END_AMT = res.getDouble(res.getColumnIndex("SHIFT_END_AMT"));
            
            res.close(); 

            return smst; 
        } else {
            return null; 
        }
    }

    public BigDecimal supposedEndShiftMoney(SQLiteDatabase db, BigDecimal shiftStartAmount, int shiftNumber, String busDate, String posNo) {
         String qry = "SELECT SUM(PAYMENT_AMOUNT) as SumPayment, SUM(CHANGE_AMOUNT) as SumChange FROM " + PAYMENT_TABLE_NAME + " " +
             "WHERE BUS_DATE = '" + busDate + "' " +
             "AND SHIFT_NO = '" + shiftNumber.toString() + "' " +
             "AND PAYMENT_CODE = 'CASH';";

        Cursor res = db.rawQuery(qry, null);

        double sumPayment = res.getDouble(res.getColumnIndex("SumPayment"));
        double sumChange = res.getDouble(res.getColumnIndex("SumChange"));

        BigDecimal bigdecSumPayment = BigDecimal.valueOf(sumPayment);
        BigDecimal bigdecSumChange = BigDecimal.valueOf(sumChange); 
        BigDecimal collections = bigdecSumPayment.subtract(bigdecSumChange); 
        BigDecimal supposedEndShiftAmount = collections.add(shiftStartAmount);

        return supposedEndShiftAmount; 
    }
}
