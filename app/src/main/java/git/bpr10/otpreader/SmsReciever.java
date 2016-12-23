package git.bpr10.otpreader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * @author: bedprakash on 18/12/16.
 */

public class SmsReciever extends BroadcastReceiver {

  private static final String SMS_RECEIVED_INTENT_ACTION =
      "android.provider.Telephony.SMS_RECEIVED";
  private static final String TAG = "SMSBroadcastReceiver";

  @Override
  public void onReceive(Context context, Intent intent) {
    Log.i(TAG, "Intent recieved: " + intent.getAction());

    if (!intent.getAction().equals(SMS_RECEIVED_INTENT_ACTION)) {
      return;
    }
    Bundle bundle = intent.getExtras();
    if (bundle == null) {
      return;
    }

    SmsService.startIntent(OtpApplication.getApplication(), bundle);

  }
}
