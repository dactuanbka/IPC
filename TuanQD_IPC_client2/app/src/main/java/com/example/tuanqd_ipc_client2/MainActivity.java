package com.example.tuanqd_ipc_client2;

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
    Button btnGetNBConsume2, btnBConsume2;
    TextView txtNBConsume2, txtBConsume2;
    private static final String TAG = "MainActivity";
    IRemoteService iRemoteService2;
    private ServiceConnection mConnection2 = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iRemoteService2 = IRemoteService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "Service has unexpectedly disconnected");
            iRemoteService2 = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGetNBConsume2 = findViewById(R.id.btnGetNBConsume2);
        txtNBConsume2 = findViewById(R.id.txtNBConsume2);
        txtBConsume2 = findViewById(R.id.txtBConsume2);
        btnBConsume2 = findViewById(R.id.btnBConsume2);

        Intent intent = new Intent("connect_to_aidl_service");
        intent.setPackage("com.example.tuanqd_ipc_server");
        bindService(intent, mConnection2, BIND_AUTO_CREATE);
        // Client waiting for Server looping, if have product will retrieve.
        btnGetNBConsume2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    iRemoteService2.produce();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                Log.i("CLIENT 2 CALLING ", "LOADING NonBlocking Consume");
                try {
                    iRemoteService2.nBConsume(new INonBlockConsumeListener.Stub() {
                        @Override
                        public void OnResponse(int consumeNonBlockItem) throws RemoteException {
                            txtNBConsume2.setText(String.valueOf(consumeNonBlockItem));
                            Log.i("Non Block 2 CONSUMER: ", "" + consumeNonBlockItem);
                        }
                    });
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
btnBConsume2.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        try {
            iRemoteService2.produce();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            iRemoteService2.bConsume(new IBlockConsumeListener.Stub() {
                @Override
                public void OnResponse(int consumeBlockItem) throws RemoteException {
                    txtBConsume2.setText(String.valueOf(consumeBlockItem));
                    Log.i(" Block 2 CONSUMER: ", "" + consumeBlockItem);
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
        unbindService(mConnection2);
    }
}