package com.example.tuanqd_ipc_server;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayDeque;
import java.util.Queue;

public class RemoteService extends Service {
    private HandlerThread mHandlerThread, mHandlerThread2, mHandlerThread3;
    private Handler mHandler, mHandler2, mHandler3;
    Queue<Integer> queue = new ArrayDeque<Integer>();
    int capacity = 5, item = 0;

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
        public void produce() throws RemoteException {
            mHandlerThread = new HandlerThread("Server Handler Thread");
            mHandlerThread.start();
            mHandler = new Handler(mHandlerThread.getLooper());
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        synchronized (this) {
                            while (queue.size() == capacity)
                                try {
                                    wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            item = item + 1;
                            Log.i("PRODUCER", "" + item);
                            queue.add(item);
                            notify();
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

            });

        }

        @Override
        public void nBConsume(INonBlockConsumeListener nBConsumelis) throws RemoteException {

            mHandlerThread2 = new HandlerThread("Server Handler Thread");
            mHandlerThread2.start();
            mHandler2 = new Handler(mHandlerThread2.getLooper());
            mHandler2.post(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        synchronized (this) {
                            while (queue.isEmpty())
                                try {
                                    wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            int value = queue.remove();
                            Log.i("CONSUMER", "" + value);
                            try {
                                nBConsumelis.OnResponse(value);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                            notify();
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

        }

        @Override
        public void bConsume(IBlockConsumeListener bConsumelis) throws RemoteException {

            mHandlerThread3 = new HandlerThread("Server Handler Thread");
            mHandlerThread3.start();
            mHandler3 = new Handler(mHandlerThread3.getLooper());
            mHandler3.post(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        synchronized (this) {
                            while (queue.isEmpty())
                                try {
                                    wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            int value = queue.remove();
                            Log.i("CONSUMER", "" + value);
                            try {
                                bConsumelis.OnResponse(value);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                            notify();
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });


        }

        @Override
        public void sendParcel(MyParcel parcel) throws RemoteException {

        }
    };

}
