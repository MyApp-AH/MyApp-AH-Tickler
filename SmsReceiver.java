package pbasis.hackathon.smsapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;
import android.util.Log;
import android.widget.Toast;
import android.net.ConnectivityManager;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * Created by Nok on 8/8/15.
 */
public class SmsReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = SmsReceiver.class.getSimpleName();
    Boolean connected = false;
    ConnectionDetector cd;
    @Override
    public void onReceive(Context context, Intent intent)
    {

        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String str = "";
        String[] checker = new String[20];
        String[] token = new String[20];
        int cmd;

        if (bundle != null)
        {
            //Toast if SMS is received
            Toast.makeText(context, "SMS received", Toast.LENGTH_SHORT).show();
            //---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];

            for (int i=0; i<msgs.length; i++){
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                str += msgs[i].getMessageBody().toString();
            }

            //test if the first keyword matches ENDOCMD then proceed if yes
            //checker[0] = "ENDOCMD";
            checker = str.split("\\s",2);

            if(checker[0].equals("ENDOCMD")){
            //---display the new SMS message---
                Toast.makeText(context, checker[0], Toast.LENGTH_SHORT).show();
                System.out.println(checker[0]);
                checker = checker[1].split("\\?",2);
                cd = new ConnectionDetector(context);
                //check which command to perform

                cmd = Integer.parseInt(checker[0]);
                switch (cmd){
                    case 1: System.out.println("CASE 1");
                        connected = cd.NetworkDetection();
                        if(!connected){
                            Toast.makeText(context,"Not connected",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(context,"Connected",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2: System.out.println("CASE 2");

                        //connected = checkConnection();
                        connected = cd.NetworkDetection();

                        if(!connected){
                            Toast.makeText(context,"Not connected",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(context,"Connected",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default: System.out.println("DEFAULT");
                        break;

                }
            }
            //if not a match, then keyword is invalid
            else{
                System.out.println("***KEYWORD INVALID***");
            }
        }
    }
   /* private class ConChkASYNC extends AsyncTask<Context, Void, Boolean>{
        private boolean tester;
        @Override
        protected Boolean doInBackground(Context... context){
            //connected = hasInternetAccess(context[0]);
            tester = true;
            return tester;
        }

        @Override
        protected void onPostExecute(Boolean result){
            connected = result;
        }
    }*/


    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public static boolean hasInternetAccess(Context context) {
        if (isNetworkAvailable(context)) {
            try {
                HttpURLConnection urlc = (HttpURLConnection)
                        (new URL("http://clients3.google.com/generate_204")
                                .openConnection());
                urlc.setRequestProperty("User-Agent", "Android");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 204 &&
                        urlc.getContentLength() == 0);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error checking internet connection", e);
            }
        } else {
            Log.d(LOG_TAG, "No network available!");
        }
        return false;
    }
}
