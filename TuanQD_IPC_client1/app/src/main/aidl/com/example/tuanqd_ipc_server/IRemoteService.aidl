// IRemoteService.aidl
package com.example.tuanqd_ipc_server;
import com.example.tuanqd_ipc_server.MyParcel;
import com.example.tuanqd_ipc_server.INonBlockConsumeListener;
import com.example.tuanqd_ipc_server.IBlockConsumeListener;

// Declare any non-default types here with import statements

 interface IRemoteService {
 void produce();
 oneway void nBConsume(in INonBlockConsumeListener nBConsumelis);
 void bConsume(in IBlockConsumeListener bConsumelis);
            void sendParcel(in MyParcel parcel);

}