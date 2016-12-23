package git.bpr10.otpreader;

import java.io.Serializable;

/**
 * @author: bedprakash on 18/12/16.
 */
public class SMSData implements Serializable {
  String sender_name;
  String smsBody;

  public String getOtp() {
    return otp;
  }

  public void setOtp(String otp) {
    this.otp = otp;
  }

  String otp;

  public SMSData(String sender, String smsBody) {
    this.sender_name = sender;
    this.smsBody = smsBody;
  }
}
