package com.example.thisi.applicationx;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by thisi on 11/7/2016.
 */

public class Employee implements KvmSerializable {
    public int EMP_CODE;
    public String EMP_NAME;
    public String EMP_PASSWORD;
    public int EMP_LVL;
    public boolean POS_ALLOW;
    public boolean VIEW_COST_ALLOW;
    public String EMP_GROUP_CODE;
    public String EMP_GROUP_NAME;
    public boolean IsActive;

    public Employee(){}

    public Employee(int EMP_CODE,
                        String EMP_NAME,
                        String EMP_PASSWORD,
                        int EMP_LVL,
                        boolean POS_ALLOW,
                        boolean VIEW_COST_ALLOW,
                        String EMP_GROUP_CODE,
                        String EMP_GROUP_NAME,
                        boolean IsActive) {
        this.EMP_CODE = EMP_CODE;
        this.EMP_NAME = EMP_NAME;
        this.EMP_PASSWORD = EMP_PASSWORD;
        this.EMP_LVL = EMP_LVL;
        this.POS_ALLOW = POS_ALLOW;
        this.VIEW_COST_ALLOW = VIEW_COST_ALLOW;
        this.EMP_GROUP_CODE = EMP_GROUP_CODE;
        this.EMP_GROUP_NAME = EMP_GROUP_NAME;
        this.IsActive = IsActive;
    }

    @Override
    public Object getProperty(int i) {
        switch(i) {
            case 0 : return EMP_CODE;
            case 1 : return EMP_NAME;
            case 2 : return EMP_PASSWORD;
            case 3 : return EMP_LVL;
            case 4 : return POS_ALLOW;
            case 5 : return VIEW_COST_ALLOW;
            case 6 : return EMP_GROUP_CODE;
            case 7 : return EMP_GROUP_NAME;
            case 8 : return IsActive;
        }

        return null;
    }

    @Override
    public int getPropertyCount() {
        return 9;
    }

    @Override
    public void setProperty(int i, Object o) {
        switch(i) {
            case 0:
                EMP_CODE = Integer.parseInt(o.toString());
                break;
            case 1:
                EMP_NAME = o.toString();
                break;
            case 2:
                EMP_PASSWORD = o.toString();
                break;
            case 3:
                EMP_LVL = Integer.parseInt(o.toString());
                break;
            case 4:
                POS_ALLOW = Boolean.getBoolean(o.toString());
                break;
            case 5:
                VIEW_COST_ALLOW = Boolean.getBoolean(o.toString());
                break;
            case 6:
                EMP_GROUP_CODE = o.toString();
                break;
            case 7:
                EMP_GROUP_NAME = o.toString();
                break;
            case 8:
                IsActive = Boolean.getBoolean(o.toString());
                break;
            default:
                break;
        }

    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {
        switch(i) {
            case 0 : propertyInfo.type = PropertyInfo.INTEGER_CLASS; propertyInfo.name = "EMP_CODE";
            case 1 : propertyInfo.type = PropertyInfo.STRING_CLASS; propertyInfo.name = "EMP_NAME";
            case 2 : propertyInfo.type = PropertyInfo.STRING_CLASS; propertyInfo.name = "EMP_PASSWORD";
            case 3 : propertyInfo.type = PropertyInfo.INTEGER_CLASS; propertyInfo.name = "EMP_LVL";
            case 4 : propertyInfo.type = PropertyInfo.BOOLEAN_CLASS; propertyInfo.name = "POS_ALLOW";
            case 5 : propertyInfo.type = PropertyInfo.BOOLEAN_CLASS; propertyInfo.name = "VIEW_COST_ALLOW";
            case 6 : propertyInfo.type = PropertyInfo.STRING_CLASS; propertyInfo.name = "EMP_GROUP_CODE";
            case 7 : propertyInfo.type = PropertyInfo.STRING_CLASS; propertyInfo.name = "EMP_GROUP_NAME";
            case 8 : propertyInfo.type = PropertyInfo.BOOLEAN_CLASS; propertyInfo.name = "IsActive";
        }
    }
}
