package git.bpr10.otpreader.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author: bedprakash on 16/1/17.
 */
public class BootCompleteReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    // to start app to read Sms
    Log.d(BootCompleteReceiver.class.getSimpleName(), "OTPReader started");
  }
}
