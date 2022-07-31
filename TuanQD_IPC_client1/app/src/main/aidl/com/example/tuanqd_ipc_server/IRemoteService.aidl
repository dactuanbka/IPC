// IRemoteService.aidl
package com.example.tuanqd_ipc_server;
import com.example.tuanqd_ipc_server.MyParcel;
import com.example.tuanqd_ipc_server.IAsyncListener;
import com.example.tuanqd_ipc_server.ISyncListener;

// Declare any non-default types here with import statements

  interface IRemoteService {
    int getPid();
    void getNumberSync(in int[] itemSync,in ISyncListener SyncLis);
    oneway void getNumberAsync(in int[] itemAsync,in IAsyncListener AsyncLis);
            void sendParcel(in MyParcel parcel);
}