package git.bpr10.otpreader;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: bedprakash on 18/12/16.
 */

public class SmsService extends IntentService {
  private static final String TAG = SmsService.class.getSimpleName();

  public SmsService() {
    super(SmsService.class.getSimpleName());
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    if (intent == null || intent.getExtras() == null) {
      return;
    }
    Bundle bundle = intent.getExtras();
    Object[] pdus = (Object[]) bundle.get("pdus");
    final SmsMessage[] messages = new SmsMessage[pdus.length];
    for (int i = 0; i < pdus.length; i++) {
      messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
    }
    if (messages.length < 0) {
      return;
    }
    Log.i(TAG, "Message recieved: " + messages[0].getMessageBody());

    SMSData sms = new SMSData(null, messages[0].getMessageBody());

    Pattern p = Pattern.compile("[0-9]{6}");
    Matcher m = p.matcher(sms.smsBody);
    if (!m.find()) {
      return;
    }
    sms.setOtp(m.group());

    JSONObject obj = new JSONObject();
    try {
      obj = new JSONObject(new Gson().toJson(sms));
    } catch (JSONException e) {
      e.printStackTrace();
    }
    ;

    AndroidNetworking.post("")
        .addQueryParameter("sender_name", sms.sender_name)
        .addQueryParameter("otp", sms.getOtp())
        .setTag(SmsService.class.getSimpleName())
        .setPriority(Priority.HIGH)
        .build()
        .getAsJSONObject(new JSONObjectRequestListener() {
          @Override
          public void onResponse(JSONObject response) {
            Log.d(TAG, "Success " + response);
          }

          @Override
          public void onError(ANError anError) {
            Log.e(TAG, "Error " + anError.getErrorBody());
          }
        });
  }

  public static void startIntent(Context context, Bundle extras) {
    Intent intent = new Intent(context, SmsService.class);
    intent.putExtras(extras);
    context.startService(intent);
  }
}
