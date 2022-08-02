// IBlockConsumeListener.aidl
package com.example.tuanqd_ipc_server;

// Declare any non-default types here with import statements

interface IBlockConsumeListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
       void OnResponse(int consumeBlockItem);
}