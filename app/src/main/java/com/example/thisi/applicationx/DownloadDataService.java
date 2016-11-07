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
                android.os.Debug.waitForDebugger();

                Object o = null;
                SQLiteDatabase db = dataHelper.getReadableDatabase();
                db.beginTransaction();

                try {
                    o = DownloadEmployees(db);
                    DownloadCustomers(db);
                    DownloadProduct_Master(db);

                    db.setTransactionSuccessful();
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

    private void DownloadCustomers(SQLiteDatabase db) {
        SoapObject returned = super.SQLResultReturn(
                "SELECT CUSTOMER_CODE, CUSTOMER_NAME, CUSTOMER_GRP_CODE, IC_NO, ADDRESS1, ADDRESS2, POS_CODE, COUNTY, STATE, COUNTRY, CONTACT, MOBILE, FAX, EMAIL, IsWHOLESALES, IsActive, IsMember, BYSMS, BYEMAIL, MODIFIED_ID, TEMP_CUSTOMER_CODE, GST_REG_NO, IsEMPLOYEE, PRICE_GRP_CODE, Outlet_Code FROM customer", null
        );

        if(returned != null) {
            Customer[] custs = RetrieveCustomersFromSoap(returned);


            for (int i = 0; i < custs.length; i++) {
                dataHelper.insertCustomer(db, custs[i]);
            }
        }
    }

    private SoapObject DownloadEmployees(SQLiteDatabase db) {
        SoapObject returned = super.SQLResultReturn(
                "SELECT emp_code, emp_name, emp_password, emp_lvl, pos_allow, view_cost_allow, emp_group_code, emp_group_name, isactive FROM employee", null
        );

        if(returned != null) {
            Employee[] emps = RetrieveEmployeesFromSoap(returned);

            for (int i = 0; i < emps.length; i++) {
                dataHelper.insertEmployee(db, emps[i]);
            }
        }
        return returned;
    }

    private void DownloadProduct_Master(SQLiteDatabase db) {
        SoapObject returned = super.SQLResultReturn("SELECT PROD_CODE, PRODUCT_SHORT_NAME, PROD_NAME, PROD_TYPE_CODE, BARCODE, PROD_GRP_01, PROD_GRP_02, PROD_GRP_03, PROD_GRP_04, PROD_GRP_05, PROD_DISC_GRP, TAX_01, TAX_02, TAX_03, TAX_04, TAX_05, PRICE_01, PRICE_02, PRICE_03, PRICE_04, PRICE_05, PRICE_06, PRICE_07, PRICE_08, PRICE_09, PRICE_10, COST, AVERAGE_COST, DISPLAY_ORDER, POS_DISPLAY, ALLOW_ZERO_PRICE, IsActive, ACTIVE_PERIOD, STOCK_UOM, PURCH_UOM, USAGE_UOM, PURCH_CONV, USAGE_CONV, IsBOM, IsSERIAL, ALLOW_DISC, MULTIPLE_DISC, MULTIPLE_UOM, TICKET_TYPE, STAFF_DISCOUNT_CODE, SUPPLIER_CODE, BARCODE_01, BARCODE_02, BARCODE_03, BARCODE_04, BARCODE_05, BARCODE_06, BARCODE_07, BARCODE_08, BARCODE_09, BARCODE_10, PRICE_PERCENTAGE_01, PRICE_PERCENTAGE_02, PRICE_PERCENTAGE_03, PRICE_PERCENTAGE_04, PRICE_PERCENTAGE_05, PRICE_PERCENTAGE_06, PRICE_PERCENTAGE_07, PRICE_PERCENTAGE_08, PRICE_PERCENTAGE_09, PRICE_PERCENTAGE_10, STOCK_TAKE_INTERIM, MODIFIED_ID, IMG_PATH, TAXCODE FROM product_master", null);

        if(returned != null) {
            Product_Master[] prods = RetrieveProduct_MasterFromSoap(returned);

            for (int i = 0; i < prods.length; i++) {
                dataHelper.insertProductMaster(db, prods[i]);
            }
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
}

