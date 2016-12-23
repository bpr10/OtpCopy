package git.bpr10.otpreader.services;

import android.app.IntentService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SmsMessage;
import android.widget.Toast;

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
    }
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
