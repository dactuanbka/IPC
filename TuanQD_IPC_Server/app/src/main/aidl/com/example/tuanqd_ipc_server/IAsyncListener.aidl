// IAsyncListener.aidl
package com.example.tuanqd_ipc_server;

// Declare any non-default types here with import statements

interface IAsyncListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    oneway void OnResponse(int itemAsync);
}