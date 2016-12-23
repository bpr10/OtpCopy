package git.bpr10.otpreader.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import git.bpr10.otpreader.model.OTP;

/**
 * @author: bedprakash on 23/12/16.
 */

public class OtpMatcher {

  private static String OTP_PATTERN = "\\b(?=\\w)[0-9]{4,8}\\b(?<=\\w)";

  /**
   * @param msg - Sms text
   * @return OTP string if found else null
   */
  public static String checkForOTP(String msg) {
    ArrayList<OTP> otps = getMatchingCandidates(msg);
    if (!otps.isEmpty()) {
      Collections.sort(otps, Collections.reverseOrder());
      System.out.print(otps.get(0).otp);
      return otps.get(0).otp;
    }
    return null;
  }

  private static ArrayList<OTP> getMatchingCandidates(String text) {
    Pattern pattern = Pattern.compile(OTP_PATTERN);
    Matcher matcher = pattern.matcher(text);

    ArrayList<OTP> otpList = new ArrayList<>();
    while (matcher.find()) {
      otpList.add(new OTP(matcher.group(), matcher.start(), matcher.end()));
      System.out.println(matcher.group());
    }
    return otpList;
  }

}
