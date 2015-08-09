package pbasis.hackathon.smsapp;

/**
 * Created by Nok on 8/9/15.
 */
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectionDetector {

    private static Context _context;
    private static final String LOG_TAG = SmsReceiver.class.getSimpleName();

    public ConnectionDetector(Context context){
        this._context = context;
    }/*
    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name

            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            return false;
        }

    }*/

    // --- CHECKS IF THE NETWORK HAS ACCESS TO THE INTERNET BY TESTING A URL VISIT ---
    public static boolean checkInternetAccess() {
        if (NetworkDetection()) {
            try {
                HttpURLConnection urltest = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urltest.setConnectTimeout(1500);
                urltest.connect();
                return (urltest.getResponseCode() == 200);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error checking internet connection", e);
            }
        } else {
            Log.d(LOG_TAG, "No network available!");
        }
        return false;
    }

    // --- CHECKS ONLY IF THE PHONE IS CONNECTED TO A NETWORK INTERFACE ---
    public static boolean NetworkDetection(){
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }
}
