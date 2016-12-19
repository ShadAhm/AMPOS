package com.example.thisi.applicationx.util;

public interface IDownloadPosControlEvents {
    void StartedRequest();
    void Finished(String methodName, Object Data);
    void FinishedWithException(Exception ex);
    void EndedRequest();
}
