package com.example.thisi.applicationx;

//------------------------------------------------------------------------------
// <wsdl2code-generated>
//    This code was generated by http://www.wsdl2code.com version  2.6
//
// Date Of Creation: 10/29/2016 5:45:32 PM
//    Please dont change this code, regeneration will override your changes
//</wsdl2code-generated>
//
//------------------------------------------------------------------------------
//
//This source code was auto-generated by Wsdl2Code  Version
//
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import java.util.Hashtable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

public class CompositeType implements KvmSerializable {
    
    public boolean boolValue;
    public boolean boolValueSpecified;
    public String stringValue;
    
    public CompositeType(){}
    
    public CompositeType(SoapObject soapObject)
    {
        if (soapObject == null)
            return;
        if (soapObject.hasProperty("BoolValue"))
        {
            Object obj = soapObject.getProperty("BoolValue");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                boolValue = Boolean.parseBoolean(j.toString());
            }else if (obj!= null && obj instanceof Boolean){
                boolValue = (Boolean) obj;
            }
        }
        if (soapObject.hasProperty("BoolValueSpecified"))
        {
            Object obj = soapObject.getProperty("BoolValueSpecified");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                boolValueSpecified = Boolean.parseBoolean(j.toString());
            }else if (obj!= null && obj instanceof Boolean){
                boolValueSpecified = (Boolean) obj;
            }
        }
        if (soapObject.hasProperty("StringValue"))
        {
            Object obj = soapObject.getProperty("StringValue");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                stringValue = j.toString();
            }else if (obj!= null && obj instanceof String){
                stringValue = (String) obj;
            }
        }
    }
    @Override
    public Object getProperty(int arg0) {
        switch(arg0){
            case 0:
                return boolValue;
            case 1:
                return boolValueSpecified;
            case 2:
                return stringValue;
        }
        return null;
    }
    
    @Override
    public int getPropertyCount() {
        return 3;
    }
    
    @Override
    public void getPropertyInfo(int index, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info) {
        switch(index){
            case 0:
                info.type = PropertyInfo.BOOLEAN_CLASS;
                info.name = "BoolValue";
                break;
            case 1:
                info.type = PropertyInfo.BOOLEAN_CLASS;
                info.name = "BoolValueSpecified";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "StringValue";
                break;
        }
    }
    
//    @Override
//    public String getInnerText() {
//        return null;
//    }
//
//
//    @Override
//    public void setInnerText(String s) {
//    }
    
    
    @Override
    public void setProperty(int arg0, Object arg1) {
    }
    
}