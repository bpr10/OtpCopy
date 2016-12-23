package git.bpr10.otpreader;

import android.app.Application;

/**
 * Application class for getting Context in BroadcastReciever
 *
 * @author: bedprakash on 18/12/16.
 */

public class OtpApplication extends Application {

  private static OtpApplication application;

  public static OtpApplication getApplication() {
    return application;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    application = this;
  }
}
