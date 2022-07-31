package com.example.tuanqd_ipc_server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Random;

public class RemoteService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // return the interface
        return binder;
    }

    private final IRemoteService.Stub binder = new IRemoteService.Stub() {
        @Override
        public int getPid() throws RemoteException {
            return 1;
        }

        @Override
        public void getNumberSync(int[] itemSync, ISyncListener SyncLis) throws RemoteException {
            for (int k = 0; k < 1000000; k++) {
                //do nothing
                Log.i("SERVER is LOOPING", "");
            }
            ;
            int resSync = 0;
            for (int i = 0; i < itemSync.length; i++) {
                resSync = resSync + itemSync[i];
                SyncLis.OnResponse(resSync);
                Log.i("SYNC PRODUCER: ", "" + resSync);
            }
        }

        @Override
        public void getNumberAsync(int[] itemAsync, IAsyncListener AsyncLis) throws RemoteException {
            for (int k = 0; k < 1000000; k++) {
                Log.i("SERVER is LOOPING", "");
            }
            ;
            int resAsync = 0;
            for (int i = 0; i < itemAsync.length; i++) {
                resAsync = resAsync + itemAsync[i];
                AsyncLis.OnResponse(resAsync);
                Log.i("ASYNC PRODUCER: ", "" + resAsync);
            }
        }

        @Override
        public void sendParcel(MyParcel parcel) throws RemoteException {

        }
    };


}
