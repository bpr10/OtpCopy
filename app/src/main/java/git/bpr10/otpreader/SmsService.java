package git.bpr10.otpreader;

import android.app.IntentService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import git.bpr10.otpreader.utils.OtpMatcher;

/**
 * @author: bedprakash on 18/12/16.
 */

public class SmsService extends IntentService {
  private static final String TAG = SmsService.class.getSimpleName();

  public SmsService() {
    super(SmsService.class.getSimpleName());
  }

  public static void startIntent(Context context, Bundle extras) {
    Intent intent = new Intent(context, SmsService.class);
    intent.putExtras(extras);
    context.startService(intent);
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    if (intent == null || intent.getExtras() == null) {
      return;
    }

    Bundle bundle = intent.getExtras();
    Object[] pdus = (Object[]) bundle.get("pdus");
    String format = bundle.getString("format");
    if (pdus == null) {
      return;
    }

    final SmsMessage[] messages = new SmsMessage[pdus.length];

    for (int i = 0; i < pdus.length; i++) {
      messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
    }
    if (messages.length <= 0) {
      return;
    }
    SmsMessage currentMessage = messages[0];

    String otp = OtpMatcher.checkForOTP(currentMessage.getMessageBody());

    if (otp != null) {
      copyToClipBoard(otp);
      showToast("OTP for " + currentMessage.getDisplayOriginatingAddress() + " is Copied : " + otp);
      postToAPI(otp,currentMessage.getDisplayOriginatingAddress());
    }
  }

  private void postToAPI(String otp, String sender_name) {
    AndroidNetworking.post("dummy url here ")
        .addQueryParameter("sender_name", sender_name)
        .addQueryParameter("otp", otp)
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

  private void copyToClipBoard(String otp) {
    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
    ClipData clip = ClipData.newPlainText("OTP", otp);
    clipboard.setPrimaryClip(clip);
  }

  private void showToast(final String message) {
    new Handler(Looper.getMainLooper()).post(new Runnable() {
      @Override
      public void run() {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
      }
    });
  }
}
