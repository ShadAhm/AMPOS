package com.example.thisi.applicationx.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.example.thisi.applicationx.data.DatabaseHelper;
import com.example.thisi.applicationx.model.DownloadDataResult;
import com.example.thisi.applicationx.util.IWsdl2CodeEvents;
import com.example.thisi.applicationx.model.Customer;
import com.example.thisi.applicationx.model.Employee;
import com.example.thisi.applicationx.model.Price_Group;
import com.example.thisi.applicationx.model.Product_Master;

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
        new AsyncTask<Void, Void, DownloadDataResult>() {
            @Override
            protected void onPreExecute() {
                eventHandler.Wsdl2CodeStartedRequest();
            }

            @Override
            protected DownloadDataResult doInBackground(Void... params) {
                DownloadDataResult ddr = new DownloadDataResult();
                SQLiteDatabase db = dataHelper.getReadableDatabase();
                db.beginTransaction();

                try {
                    DeleteExistingData(db);
                    int customersDownloaded = DownloadCustomers(db);
                    int productsDownloaded = DownloadProduct_Master(db);
                    int priceGroupsDownloaded = DownloadPrice_Group(db);
                    db.setTransactionSuccessful();

                    ddr.isSuccessful = true;
                    ddr.customersDownloaded = customersDownloaded;
                    ddr.productsDownloaded = productsDownloaded;
                    ddr.priceGroupsDownloaded = priceGroupsDownloaded;
                    ddr.message = "Successfully downloaded data";
                } catch (Exception e) {
                    e.printStackTrace();
                    ddr.isSuccessful = false;
                    ddr.message = e.getMessage();
                } finally {
                    db.endTransaction();
                    db.close();
                    return ddr;
                }
            }

            @Override
            protected void onPostExecute(DownloadDataResult result) {
                eventHandler.Wsdl2CodeEndedRequest();
                if (result != null) {
                    eventHandler.Wsdl2CodeFinished("SQLResult", result);
                }
            }
        }.execute();
    }

    public void DownloadPosControlAsync() throws Exception {
        new AsyncTask<POS_Control, Void, DownloadDataResult>() {
            @Override
            protected void onPreExecute() {
                dpcEventHandler.StartedRequest();
            }

            @Override
            protected DownloadDataResult doInBackground(POS_Control... params) {
                DownloadDataResult ddr = new DownloadDataResult();
                SQLiteDatabase db = dataHelper.getReadableDatabase();
                db.beginTransaction();

                try {
                    if(params == null || params.length <= 0) {
                        throw new IllegalArgumentException("Params cannot be null");
                    }

                    POS_Control pctrl = params[0]; 

                    int posControlsReturned = DownloadPosControl(db, pctrl.POS_NO, pctrl.OUTLET_CODE);
                    ddr.isSuccessful = true; 
                    ddr.posControlsDownloaded = posControlsReturned; 
                    ddr.message = "Successfully downloaded data";
                } catch (Exception e) {
                    e.printStackTrace();
                    ddr.isSuccessful = false;
                    ddr.message = e.getMessage();
                } finally {
                    db.endTransaction();
                    db.close();
                    return ddr;
                }
            }

            @Override
            protected void onPostExecute(DownloadDataResult result) {
                dpcEventHandler.EndedRequest();
                if (result != null) {
                    dpcEventHandler.Finished("SQLResult", result);
                }
            }
        }.execute();
    }

    public void DownloadEmployeesAsync() throws Exception {
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
                    dataHelper.clearEmployeeTable(db); 
                    o = DownloadEmployees(db);

                    db.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                    o = "failure";
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
        }.execute().get();
    }

    private void DeleteExistingData(SQLiteDatabase db) {
        dataHelper.clearOldData(db);
    }

    private String DownloadEmployees(SQLiteDatabase db) {
        SoapObject returned = super.SQLResultReturn(
                "SELECT COALESCE(EMP_CODE, 0) AS EMP_CODE,COALESCE(EMP_NAME, '') AS EMP_NAME,COALESCE(EMP_PASSWORD, '') AS EMP_PASSWORD,COALESCE(EMP_LVL, 0) AS EMP_LVL,COALESCE(POS_ALLOW, 0) AS POS_ALLOW,COALESCE(VIEW_COST_ALLOW, 0) AS VIEW_COST_ALLOW,COALESCE(EMP_GROUP_CODE, '') AS EMP_GROUP_CODE,COALESCE(EMP_GROUP_NAME, '') AS EMP_GROUP_NAME,COALESCE(IsActive, 0) AS IsActive FROM Employee"
                , null
        );

        if(returned != null) {
            Employee[] emps = RetrieveEmployeesFromSoap(returned);

            for (int i = 0; i < emps.length; i++) {
                dataHelper.insertEmployee(db, emps[i]);
            }

            return "success";
        }
        else {
            return "employeenull";
        }
    }

    private int DownloadPosControl(SQLiteDatabase db, String posno, String outcode) {
        String sb = "SELECT COALESCE(COMPANY_CODE, '') AS COMPANY_CODE, " +
            "COALESCE(OUTLET_CODE, '') AS OUTLET_CODE, " +
            "COALESCE(POS_NO, '') AS POS_NO, " +
            "COALESCE(BUS_DATE, '') AS BUS_DATE, " +
            "COALESCE(SHIFT_NUMBER, 0) AS SHIFT_NUMBER, " +
            "COALESCE(EMP_CD, '') AS EMP_CD, " +
            "COALESCE(LAST_RCP, '') AS LAST_RCP, " +
            "COALESCE(LAST_SUSPEND_NUMBER, '') AS LAST_SUSPEND_NUMBER, " +
            "COALESCE(REPRINT_COUNT, 0) AS REPRINT_COUNT,  " +
            "COALESCE(DAYEND, 0) AS DAYEND " +
            "FROM POS_Control " +
            "WHERE POS_NO = '" + posno + "' " +
            "AND OUTLET_CODE = '" + outcode + "' ";
		
        SoapObject returned = super.SQLResultReturn(sb, null);

        if(returned != null) {
            POS_Control[] pctrls = RetrievePosControlsFromSoap(returned);

            // there should only be one pos control per device
            if(pctrls.length == 1) {
                dataHelper.deleteAndInsertPOSControl(db, pctrls[0]); 
            }
            return pctrls.length; 
        }
        else {
            return 0; 
        }
    }

    private int DownloadCustomers(SQLiteDatabase db) {
        SoapObject returned = super.SQLResultReturn(
                "SELECT COALESCE(CUSTOMER_CODE, '') AS CUSTOMER_CODE,COALESCE(CUSTOMER_NAME, '') AS CUSTOMER_NAME,COALESCE(CUSTOMER_GRP_CODE, '') AS CUSTOMER_GRP_CODE,COALESCE(IC_NO, '') AS IC_NO,COALESCE(ADDRESS1, '') AS ADDRESS1,COALESCE(ADDRESS2, '') AS ADDRESS2,COALESCE(POS_CODE, '') AS POS_CODE,COALESCE(COUNTY, '') AS COUNTY,COALESCE(STATE, '') AS STATE,COALESCE(COUNTRY, '') AS COUNTRY,COALESCE(CONTACT, '') AS CONTACT,COALESCE(MOBILE, '') AS MOBILE,COALESCE(FAX, '') AS FAX,COALESCE(EMAIL, '') AS EMAIL,COALESCE(IsWHOLESALES, 0) AS IsWHOLESALES,COALESCE(IsActive, 0) AS IsActive,COALESCE(IsMember, 0) AS IsMember,COALESCE(POINT_VALUE, 0) AS POINT_VALUE,COALESCE(BYSMS, 0) AS BYSMS,COALESCE(BYEMAIL, 0) AS BYEMAIL,COALESCE(MODIFIED_ID, '') AS MODIFIED_ID,COALESCE(TEMP_CUSTOMER_CODE, '') AS TEMP_CUSTOMER_CODE,COALESCE(GST_REG_NO, '') AS GST_REG_NO,COALESCE(IsEMPLOYEE, 0) AS IsEMPLOYEE,COALESCE(PRICE_GRP_CODE, '') AS PRICE_GRP_CODE,COALESCE(Outlet_Code, '') AS Outlet_Code FROM Customer_data"
                , null
        );

        if(returned != null) {
            Customer[] custs = RetrieveCustomersFromSoap(returned);

            for (int i = 0; i < custs.length; i++) {
                dataHelper.insertCustomer(db, custs[i]);
            }

            return custs.length;
        }
        return 0;
    }

    private int DownloadProduct_Master(SQLiteDatabase db) {
        SoapObject returned = super.SQLResultReturn(
            "SELECT COALESCE(PROD_CODE, '') AS PROD_CODE,COALESCE(PRODUCT_SHORT_NAME, '') AS PRODUCT_SHORT_NAME,COALESCE(PROD_NAME, '') AS PROD_NAME,COALESCE(PROD_TYPE_CODE, '') AS PROD_TYPE_CODE,COALESCE(BARCODE, '') AS BARCODE,COALESCE(PROD_GRP_01, '') AS PROD_GRP_01,COALESCE(PROD_GRP_02, '') AS PROD_GRP_02,COALESCE(PROD_GRP_03, '') AS PROD_GRP_03,COALESCE(PROD_GRP_04, '') AS PROD_GRP_04,COALESCE(PROD_GRP_05, '') AS PROD_GRP_05,COALESCE(PROD_DISC_GRP, '') AS PROD_DISC_GRP,COALESCE(TAX_01, 0) AS TAX_01,COALESCE(TAX_02, 0) AS TAX_02,COALESCE(TAX_03, 0) AS TAX_03,COALESCE(TAX_04, 0) AS TAX_04,COALESCE(TAX_05, 0) AS TAX_05,COALESCE(PRICE_01, '0.0') AS PRICE_01,COALESCE(PRICE_02, '0.0') AS PRICE_02,COALESCE(PRICE_03, '0.0') AS PRICE_03,COALESCE(PRICE_04, '0.0') AS PRICE_04,COALESCE(PRICE_05, '0.0') AS PRICE_05,COALESCE(PRICE_06, '0.0') AS PRICE_06,COALESCE(PRICE_07, '0.0') AS PRICE_07,COALESCE(PRICE_08, '0.0') AS PRICE_08,COALESCE(PRICE_09, '0.0') AS PRICE_09,COALESCE(PRICE_10, '0.0') AS PRICE_10,COALESCE(COST, '0.0') AS COST,COALESCE(AVERAGE_COST, '0.0') AS AVERAGE_COST,COALESCE(DISPLAY_ORDER, 0) AS DISPLAY_ORDER,COALESCE(POS_DISPLAY, 0) AS POS_DISPLAY,COALESCE(ALLOW_ZERO_PRICE, 0) AS ALLOW_ZERO_PRICE,COALESCE(IsActive, 0) AS IsActive,COALESCE(ACTIVE_PERIOD, 0) AS ACTIVE_PERIOD,COALESCE(STOCK_UOM, '') AS STOCK_UOM,COALESCE(PURCH_UOM, '') AS PURCH_UOM,COALESCE(USAGE_UOM, '') AS USAGE_UOM,COALESCE(PURCH_CONV, 0) AS PURCH_CONV,COALESCE(USAGE_CONV, 0) AS USAGE_CONV,COALESCE(IsBOM, 0) AS IsBOM,COALESCE(IsSERIAL, 0) AS IsSERIAL,COALESCE(ALLOW_DISC, 0) AS ALLOW_DISC,COALESCE(MULTIPLE_DISC, 0) AS MULTIPLE_DISC,COALESCE(MULTIPLE_UOM, 0) AS MULTIPLE_UOM,COALESCE(TICKET_TYPE, '') AS TICKET_TYPE,COALESCE(STAFF_DISCOUNT_CODE, '') AS STAFF_DISCOUNT_CODE,COALESCE(SUPPLIER_CODE, '') AS SUPPLIER_CODE,COALESCE(BARCODE_01, '') AS BARCODE_01,COALESCE(BARCODE_02, '') AS BARCODE_02,COALESCE(BARCODE_03, '') AS BARCODE_03,COALESCE(BARCODE_04, '') AS BARCODE_04,COALESCE(BARCODE_05, '') AS BARCODE_05,COALESCE(BARCODE_06, '') AS BARCODE_06,COALESCE(BARCODE_07, '') AS BARCODE_07,COALESCE(BARCODE_08, '') AS BARCODE_08,COALESCE(BARCODE_09, '') AS BARCODE_09,COALESCE(BARCODE_10, '') AS BARCODE_10,COALESCE(PRICE_PERCENTAGE_01, 0) AS PRICE_PERCENTAGE_01,COALESCE(PRICE_PERCENTAGE_02, 0) AS PRICE_PERCENTAGE_02,COALESCE(PRICE_PERCENTAGE_03, 0) AS PRICE_PERCENTAGE_03,COALESCE(PRICE_PERCENTAGE_04, 0) AS PRICE_PERCENTAGE_04,COALESCE(PRICE_PERCENTAGE_05, 0) AS PRICE_PERCENTAGE_05,COALESCE(PRICE_PERCENTAGE_06, 0) AS PRICE_PERCENTAGE_06,COALESCE(PRICE_PERCENTAGE_07, 0) AS PRICE_PERCENTAGE_07,COALESCE(PRICE_PERCENTAGE_08, 0) AS PRICE_PERCENTAGE_08,COALESCE(PRICE_PERCENTAGE_09, 0) AS PRICE_PERCENTAGE_09,COALESCE(PRICE_PERCENTAGE_10, 0) AS PRICE_PERCENTAGE_10,COALESCE(STOCK_TAKE_INTERIM, '') AS STOCK_TAKE_INTERIM,COALESCE(MODIFIED_ID, '') AS MODIFIED_ID,COALESCE(IMG_PATH, '') AS IMG_PATH,COALESCE(TAXCODE, '') AS TAXCODE FROM product_master"
        , null);

        if(returned != null) {
            Product_Master[] prods = RetrieveProduct_MasterFromSoap(returned);

            for (int i = 0; i < prods.length; i++) {
                dataHelper.insertProductMaster(db, prods[i]);
            }

            return prods.length;
        }
        return 0;
    }

    private int DownloadPrice_Group(SQLiteDatabase db) {
        SoapObject returned = super.SQLResultReturn(
                "SELECT COALESCE(PRICE_GRP_CODE, '') AS PRICE_GRP_CODE,COALESCE(PRICE_GRP_NAME, '') AS PRICE_GRP_NAME,COALESCE(PROD_CODE, '') AS PROD_CODE,COALESCE(PRICE, '0.0') AS PRICE FROM price_group"
                , null
        );

        if(returned != null) {
            Price_Group[] pgroups = RetrievePrice_GroupFromSoap(returned);

            for (int i = 0; i < pgroups.length; i++) {
                dataHelper.insertprice_group(db, pgroups[i]);
            }
            return pgroups.length;
        }
        return 0;
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

                String taxohone = pii.getProperty(11).toString();
                if(taxohone.equalsIgnoreCase("true")
                        || taxohone.equalsIgnoreCase("1")) {
                    prod.TAX_01 = true;
                }
                else {
                    prod.TAX_01 = false;
                }

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
                employee.EMP_CODE = pii.getProperty(0).toString();
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
                customer.BYSMS = Boolean.getBoolean(pii.getProperty(18).toString());
                customer.BYEMAIL = Boolean.getBoolean(pii.getProperty(19).toString());
                customer.MODIFIED_ID = pii.getProperty(20).toString();
                customer.TEMP_CUSTOMER_CODE = pii.getProperty(21).toString();
                customer.GST_REG_NO = pii.getProperty(22).toString();
                customer.IsEMPLOYEE = Boolean.getBoolean(pii.getProperty(23).toString());
                customer.PRICE_GRP_CODE = pii.getProperty(24).toString();
                customer.Outlet_Code = pii.getProperty(25).toString();

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

    public static POS_Control[] RetrievePosControlsFromSoap(SoapObject soap) {
        try {
            POS_Control[] posControls = new POS_Control[soap.getPropertyCount()];

            for (int i = 0; i < posControls.length; i++) {
                SoapObject pii = (SoapObject) soap.getProperty(i);
                POS_Control posControl = new POS_Control();

                posControl.COMPANY_CODE = pii.getProperty(0).toString(); 
                posControl.Outlet_Code = pii.getProperty(0).toString(); 
                posControl.POS_NO = pii.getProperty(0).toString(); 
                posControl.BUS_DATE = pii.getProperty(0).toString(); 
                posControl.SHIFT_NUMBER = Integer.parseInt(pii.getProperty(0).toString()); 
                posControl.EMP_CD = pii.getProperty(0).toString(); 
                posControl.LAST_RCP = pii.getProperty(0).toString(); 
                posControl.LAST_SUSPEND_NUMBER = pii.getProperty(0).toString(); 
                posControl.REPRINT_COUNT = Integer.parseInt(pii.getProperty(0).toString()); 
                posControl.DAYEND = Boolean.getBoolean(pii.getProperty(0).toString()); 

                posControls[i] = posControl;
            }
            return posControls;
        } catch (IndexOutOfBoundsException ex) {
            throw ex;
        }
    }
}

