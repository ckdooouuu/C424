/*
 * Copyright (c) 2012-2022 Arne Schwabe
 * Distributed under the GNU GPL v2 with additional terms. For full terms see the file doc/LICENSE.txt
 */

package de.blinkt.openvpn.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.VpnService;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;

import de.blinkt.openvpn.R;
import de.blinkt.openvpn.VpnProfile;
import de.blinkt.openvpn.core.Connection;
import de.blinkt.openvpn.core.IOpenVPNServiceInternal;
import de.blinkt.openvpn.core.OpenVPNService;
import de.blinkt.openvpn.core.VpnStatus;

public class VpnManage {
    private IOpenVPNServiceInternal mService;
    private ServiceConnection mConnection;

    private volatile static VpnManage Instance;

    private VpnManage() {

    }

    public static VpnManage getInstance() {
        if (Instance == null) {
            synchronized (VpnManage.class) {
                if (Instance == null) {
                    Instance = new VpnManage();
                }
            }
        }
        return Instance;
    }

    public void startAndCheckVpn(Activity context, ProxyModel proxyModel, boolean isNeedPwd, HashSet<String> pkgSet) {
        Intent intent = VpnService.prepare(context);
        if (intent != null) {
            Log.d("iVPN", "intent != null");
            try {
                context.startActivityForResult(intent, 95270);
            } catch (ActivityNotFoundException ane) {
                // Shame on you Sony! At least one user reported that
                // an official Sony Xperia Arc S image triggers this exception
                VpnStatus.logError(R.string.no_vpn_support_image);
            }
        } else {
            Log.d("iVPN", "intent == null");
            startVpn(context, proxyModel, pkgSet);
        }
    }

    public void startVpn(Context context, ProxyModel proxyModel, HashSet<String> pkgSet) {
        VpnProfile vpnProfile = getProfile();
        if (vpnProfile != null) {
            if (vpnProfile.mConnections != null && vpnProfile.mConnections.length > 0) {
                vpnProfile.mConnections[0].mServerName = proxyModel.getIp();
                vpnProfile.mConnections[0].mServerPort = proxyModel.getPort();
                vpnProfile.mConnections[0].mUseUdp = proxyModel.isUseUDP();
            }
            if (proxyModel.getUserName() != null && !proxyModel.getUserName().isEmpty() &&
                    proxyModel.getPassword() != null && !proxyModel.getPassword().isEmpty()) {
                vpnProfile.mCheckRemoteCN = false;  //取消证书主机名检查
                vpnProfile.mAuthenticationType = VpnProfile.TYPE_USERPASS_CERTIFICATES;
                vpnProfile.mUsername = proxyModel.getUserName();
                vpnProfile.mPassword = proxyModel.getPassword();
            }
            if (pkgSet != null && pkgSet.size() > 0) {
                if (pkgSet != null && pkgSet.size() > 0) {
                    Iterator<String> iterator = pkgSet.iterator();
                    while (iterator.hasNext()) {
                        vpnProfile.mAllowedAppsVpn.add(iterator.next());
                    }
                }
            }
        }
        Log.d("iVPN", "connect proxy : " + vpnProfile.mConnections[0].mServerName + ":" +
                vpnProfile.mConnections[0].mServerPort + "      " + vpnProfile.mConnections[0].mUseUdp +
                "    username:" + vpnProfile.mUsername + "   password:" + vpnProfile.mPassword);
        Intent intent = vpnProfile.prepareStartService(context, vpnProfile);
        context.startForegroundService(intent);
    }

    public void stopVpn(Context context) {
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName className,
                                           IBinder service) {
                mService = IOpenVPNServiceInternal.Stub.asInterface(service);
                try {
                    Log.d("iVPN", "stopVPN");
                    mService.stopVPN(false);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName arg0) {
                mService = null;
            }
        };

        Intent intent = new Intent(context, OpenVPNService.class);
        intent.setAction(OpenVPNService.START_SERVICE);
        context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private VpnProfile getProfile() {
        VpnProfile vpnProfile = new VpnProfile();
        vpnProfile.mAuthenticationType = 0;
        vpnProfile.mCaFilename = "[[INLINE]]-----BEGIN CERTIFICATE-----\n" +
                "MIIBwjCCAWegAwIBAgIJAP0ltPtENv28MAoGCCqGSM49BAMCMB4xHDAaBgNVBAMM\n" +
                "E2NuX0hDRlVBSGl2MXYwa21maFgwHhcNMjAwNzA3MDkzNjE2WhcNMzAwNzA1MDkz\n" +
                "NjE2WjAeMRwwGgYDVQQDDBNjbl9IQ0ZVQUhpdjF2MGttZmhYMFkwEwYHKoZIzj0C\n" +
                "AQYIKoZIzj0DAQcDQgAEx9SqNezAmIfvXVw13znifyUKAOZuBZnhRpbDPm+GV45m\n" +
                "KYe/dnCht7V9golgDcei/Ta1jUbSuBjkLS2maYbkOKOBjTCBijAdBgNVHQ4EFgQU\n" +
                "f3GIVaFt7BfXzOCXKCPLAoyDWqAwTgYDVR0jBEcwRYAUf3GIVaFt7BfXzOCXKCPL\n" +
                "AoyDWqChIqQgMB4xHDAaBgNVBAMME2NuX0hDRlVBSGl2MXYwa21maFiCCQD9JbT7\n" +
                "RDb9vDAMBgNVHRMEBTADAQH/MAsGA1UdDwQEAwIBBjAKBggqhkjOPQQDAgNJADBG\n" +
                "AiEAgcWuULZi1q1gDMuqsKwYMM34/vobsxMNPBnE+PV4DhgCIQDqeH1Hmf8lwAkG\n" +
                "jfJnPtfQb1R74P5x5acKkVlDKYTLFw==\n" +
                "-----END CERTIFICATE-----";
        vpnProfile.mCheckRemoteCN = true;
        vpnProfile.mCipher = "AES-128-GCM";
        vpnProfile.mClientCertFilename = "[[INLINE]]-----BEGIN CERTIFICATE-----\n" +
                "MIIBzDCCAXKgAwIBAgIQXdhXmKx/6kkJLSTmhdNiDDAKBggqhkjOPQQDAjAeMRww\n" +
                "GgYDVQQDDBNjbl9IQ0ZVQUhpdjF2MGttZmhYMB4XDTIyMDgwNDA3MzUxNFoXDTMy\n" +
                "MDgwMTA3MzUxNFowEDEOMAwGA1UEAwwFbXl2cG4wWTATBgcqhkjOPQIBBggqhkjO\n" +
                "PQMBBwNCAAQpkz5a9vpEtHvgabVl7mC2iSRP4TKxVHtMEEz57bmHeG6UlWihuI0n\n" +
                "1Dr/kIi2EmL3o6wF6HnpzeJLVj5gqzmxo4GfMIGcMAkGA1UdEwQCMAAwHQYDVR0O\n" +
                "BBYEFFyv55i2JctlzAYZZaLEPyYBk0tSME4GA1UdIwRHMEWAFH9xiFWhbewX18zg\n" +
                "lygjywKMg1qgoSKkIDAeMRwwGgYDVQQDDBNjbl9IQ0ZVQUhpdjF2MGttZmhYggkA\n" +
                "/SW0+0Q2/bwwEwYDVR0lBAwwCgYIKwYBBQUHAwIwCwYDVR0PBAQDAgeAMAoGCCqG\n" +
                "SM49BAMCA0gAMEUCIE7R9UfC2Wopo5RnUAcoWSoHSnm3DQ3r/kCcbVxdS8PAAiEA\n" +
                "wYKEtPwHUcYyMYc6A+M1IKNN2/uc2qln2rOIotrPScs=\n" +
                "-----END CERTIFICATE-----";
        vpnProfile.mClientKeyFilename = "[[INLINE]]-----BEGIN PRIVATE KEY-----\n" +
                "MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgUfzfrNDM5LJRzJS8\n" +
                "Z0pbErx1mUxLzm6gsCRfWPWANEmhRANCAAQpkz5a9vpEtHvgabVl7mC2iSRP4TKx\n" +
                "VHtMEEz57bmHeG6UlWihuI0n1Dr/kIi2EmL3o6wF6HnpzeJLVj5gqzmx\n" +
                "-----END PRIVATE KEY-----";
        vpnProfile.mAllowAppVpnBypass = true;
        //
        //
        //
        Connection connection = new Connection();
        connection.mConnectTimeout = 0;
        connection.mCustomConfiguration = "explicit-exit-notify \n";
        connection.mEnabled = true;
        connection.mProxyName = "proxy.example.com";
        connection.mProxyPort = "8080";
        connection.mProxyType = Connection.ProxyType.NONE;
        connection.mServerName = "141.98.100.106";
        connection.mServerPort = "103";
        connection.mUseCustomConfig = true;
        connection.mUseProxyAuth = false;
        connection.mUseUdp = false;
        //
        //
        //
        vpnProfile.mConnections = new Connection[]{connection};
        vpnProfile.mExpectTLSCert = true;
        vpnProfile.mName = "myproxy";
        vpnProfile.mNobind = true;
        vpnProfile.mPersistTun = true;
        vpnProfile.mRemoteCN = "server_R2Puof1cldz4yD3X";
        vpnProfile.mTLSAuthDirection = "tls-crypt";
        vpnProfile.mTLSAuthFilename = "[[INLINE]]#\n" +
                "# 2048 bit OpenVPN static key\n" +
                "#\n" +
                "-----BEGIN OpenVPN Static key V1-----\n" +
                "f500690acf5d3bf2992b7f4adfe755f4\n" +
                "c816bfde4062f3c700564418933eccb2\n" +
                "a80d4043d1b719e7cd7c04a979f78a2f\n" +
                "1ea222990f835be5c383e2e919cfbf2d\n" +
                "abea066a5d82efb59372627c150522e5\n" +
                "cef0ea030d789d3f0c86520d8ebbed44\n" +
                "286e3f79804a023594a4e677131179c1\n" +
                "a339fdbd9270a12fbee2e7c6b79a0f02\n" +
                "7c3d2e93d2cfe5fbb166265b86f7a0c9\n" +
                "cdc8da0fa926e4ea2897ae2e70298583\n" +
                "50b1ce781ab482554930f9c2e9cc6328\n" +
                "c6933af654e2956cd34e2b279380af74\n" +
                "209e579b896c34da89804acb7ed339d4\n" +
                "4d230c177ef87434eb6594b48c4b6f0d\n" +
                "9280f5aea7e138fb98ff6fca5b2f0e6a\n" +
                "0fb7722e15256f91d89c89cb21e986de\n" +
                "-----END OpenVPN Static key V1-----";
        vpnProfile.mUseCustomConfig = true;
        vpnProfile.mUseDefaultRoute = false;
        vpnProfile.mUseDefaultRoutev6 = false;
        vpnProfile.mUseTLSAuth = true;
        vpnProfile.setUUID(UUID.randomUUID());
        vpnProfile.mVerb = "3";
        vpnProfile.mVersion = 8;

//                if (vpnProfile.encrypt) {
//                    vpnProfile.mCheckRemoteCN = false;//取消证书主机名检查
//                    vpnProfile.mAuthenticationType = VpnProfile.TYPE_USERPASS_CERTIFICATES;
//                    vpnProfile.mUsername = userName;
//                    vpnProfile.mPassword = passWord;
//                }
        return vpnProfile;
    }

//    native String getCA();
}
