package com.example.thisi.applicationx.util;

public interface IWsdl2CodeEvents {
    void Wsdl2CodeStartedRequest();
    void Wsdl2CodeFinished(String methodName, Object Data);
    void Wsdl2CodeFinishedWithException(Exception ex);
    void Wsdl2CodeEndedRequest();

    void UploadDataStartedRequest();
    void UploadDataFinished(String methodName, Object Data);
    void UploadDataFinishedWithException(Exception ex);
    void UploadDataEndedRequest();
}
