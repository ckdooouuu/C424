package com.example.c424.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import com.example.c424.App;

import java.util.concurrent.LinkedBlockingQueue;

public class GoogleIdUtil {
    public static void getGoogleAdId(Context context) throws Exception {
        PackageManager pm = context.getPackageManager();
        pm.getPackageInfo(App.app.getPackageName(), 0);
        AdvertisingConnection connection = new AdvertisingConnection();
        Intent intent = new Intent(
                "com.google.android.gms.ads.identifier.service.START");
        intent.setPackage("com.google.android.gms");
        if (context.bindService(intent, connection, Context.BIND_AUTO_CREATE)) {
            try {
                AdvertisingInterface adInterface = new AdvertisingInterface(
                        connection.getBinder());
                App.app.googleID = adInterface.getId();
                LogUtil.d("googleAdId:" + adInterface.getId());
                return;
            } finally {
                context.unbindService(connection);
            }
        }
        App.app.googleID = "";
    }

    private static final class AdvertisingConnection implements ServiceConnection {
        boolean retrieved = false;
        private final LinkedBlockingQueue<IBinder> queue = new LinkedBlockingQueue<>(1);
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                this.queue.put(service);
            } catch (InterruptedException localInterruptedException) {
            }
        }
        public void onServiceDisconnected(ComponentName name) {
        }
        public IBinder getBinder() throws InterruptedException {
            if (this.retrieved)
                throw new IllegalStateException();
            this.retrieved = true;
            return this.queue.take();
        }
    }

    private static final class AdvertisingInterface implements IInterface {
        private IBinder binder;
        public AdvertisingInterface(IBinder pBinder) {
            binder = pBinder;
        }
        public IBinder asBinder() {
            return binder;
        }
        public String getId() throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            String id;
            try {
                data.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                binder.transact(1, data, reply, 0);
                reply.readException();
                id = reply.readString();
            } finally {
                reply.recycle();
                data.recycle();
            }
            return id;
        }
    }
}
