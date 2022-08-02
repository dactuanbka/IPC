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

import com.example.tuanqd_ipc_server.IBlockConsumeListener;
import com.example.tuanqd_ipc_server.INonBlockConsumeListener;
import com.example.tuanqd_ipc_server.IRemoteService;

public class MainActivity extends AppCompatActivity {
    Button btnGetNBConsume, btnGetBConsume;
    TextView txtNBConsume, txtBConsume;
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
        btnGetNBConsume = findViewById(R.id.btnGetNBConsume);
        btnGetBConsume = findViewById(R.id.btnGetBConsume);
        txtNBConsume = findViewById(R.id.txtNBConsume);
        txtBConsume = findViewById(R.id.txtBConsume);

        Intent intent = new Intent("connect_to_aidl_service");
        intent.setPackage("com.example.tuanqd_ipc_server");
        bindService(intent, mConnection, BIND_AUTO_CREATE);
        // Client waiting for Server looping, if have product will retrieve.
        btnGetNBConsume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    iRemoteService.produce();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                Log.i("CLIENT 1 CALLING ", "LOADING NonBlocking Consume");
                try {
                    iRemoteService.nBConsume(new INonBlockConsumeListener.Stub() {
                        @Override
                        public void OnResponse(int consumeNonBlockItem) throws RemoteException {
                            txtNBConsume.setText(String.valueOf(consumeNonBlockItem));
                            Log.i("Non Block 1 CONSUMER: ", "" + consumeNonBlockItem);
                        }
                    });
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        btnGetBConsume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    iRemoteService.produce();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                Log.i("CLIENT 1 CALLING ", "LOADING Blocking mode");
                try {
                    iRemoteService.bConsume(new IBlockConsumeListener.Stub() {
                        @Override
                        public void OnResponse(int consumeBlockItem) throws RemoteException {
                            txtBConsume.setText(String.valueOf(consumeBlockItem));
                            Log.i(" Block 1 CONSUMER: ", "" + consumeBlockItem);
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