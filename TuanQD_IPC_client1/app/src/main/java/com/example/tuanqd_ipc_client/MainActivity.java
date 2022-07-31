package com.example.tuanqd_ipc_client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tuanqd_ipc_server.IAsyncListener;
import com.example.tuanqd_ipc_server.IRemoteService;
import com.example.tuanqd_ipc_server.ISyncListener;

public class MainActivity extends AppCompatActivity {
    Button btnGetAsyncNumber, btnGetSyncNumber;
    TextView txtItemAsyncNumber, txtItemSyncNumber;
    private static final String TAG = "MainActivity";
    IRemoteService iRemoteService;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iRemoteService = IRemoteService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "Service has unexpectedly disconnected");
            iRemoteService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGetAsyncNumber = findViewById(R.id.btnGetAsyncNumber);
        btnGetSyncNumber = findViewById(R.id.btnGetSyncNumber);
        txtItemAsyncNumber = findViewById(R.id.txtNumberAsync);
        txtItemSyncNumber = findViewById(R.id.txtNumberSync);

        Intent intent = new Intent("connect_to_aidl_service");
        intent.setPackage("com.example.tuanqd_ipc_server");
        bindService(intent, mConnection, BIND_AUTO_CREATE);

        // Client waiting for Server looping, if have product will retrieve.
        btnGetAsyncNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    iRemoteService.getPid();
                    Log.i("Client1 call Remote", "Called Process: " + iRemoteService.getPid());
                    Log.i("CLIENT 1 CALLING ", "LOADING");
                    iRemoteService.getNumberAsync(new int[]{1, 2, 3},
                            new IAsyncListener.Stub() {
                                @Override
                                public void OnResponse(int itemAsync) throws RemoteException {
                                    txtItemAsyncNumber.setText(String.valueOf(itemAsync));
                                    Log.i("ASYNC 1 CONSUMER: ", "" + itemAsync);
                                }

                            });

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        // Directly call no use oneway method, cause Block Main Thread;
        btnGetSyncNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    iRemoteService.getNumberSync(new int[]{1, 2, 3},
                            new ISyncListener.Stub() {
                                @Override
                                public void OnResponse(int itemSync) throws RemoteException {
                                    txtItemSyncNumber.setText(String.valueOf(itemSync));
                                    Log.i("SYNC 1 CONSUMER: ", "" + itemSync);
                                }
                            });
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }
}