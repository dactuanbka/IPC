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

import com.example.tuanqd_ipc_client2.R;
import com.example.tuanqd_ipc_server.IAsyncListener;
import com.example.tuanqd_ipc_server.IRemoteService;

public class MainActivity extends AppCompatActivity {
    Button btnGetAsyncNumber2;
    TextView txtItemAsyncNumber2;
    private static final String TAG = "MainActivity";
    IRemoteService iRemoteService2;
    private ServiceConnection mConnection = new ServiceConnection() {
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
        btnGetAsyncNumber2 = findViewById(R.id.btnGetAsyncNumber2);
        txtItemAsyncNumber2 = findViewById(R.id.txtNumberAsync2);

        Intent intent = new Intent("connect_to_aidl_service");
        intent.setPackage("com.example.tuanqd_ipc_server");
        bindService(intent, mConnection, BIND_AUTO_CREATE);

        // Client waiting for Server looping, if have product will retrieve.
        btnGetAsyncNumber2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.i("Client 2 call Remote", "Called Process");
                    Log.i(" CLIENT 2 CALLING ", "LOADING");
                    iRemoteService2.getNumberAsync(new int[]{1,2,3}, new IAsyncListener.Stub() {
                        @Override
                        public void OnResponse(int itemAsync) throws RemoteException {
                            txtItemAsyncNumber2.setText(String.valueOf(itemAsync));
                            Log.i("ASYNC 2 CONSUMER: ", "" + itemAsync);
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