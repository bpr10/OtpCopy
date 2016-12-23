package git.bpr10.otpreader.model;

/**
 * @author: bedprakash on 23/12/16.
 */
public class OTP implements Comparable<OTP> {
  public String otp;
  private int score;

  public OTP(String otp, int start, int end) {
    this.otp = otp;

    // scoring as per length
    if (otp.length() == 6) {
      score += 6;
    } else if (otp.length() == 4) {
      score += 4;
    } else {
      score += 2;
    }
    //TODO: add remaining logic here
  }

  @Override
  public int compareTo(OTP o) {
    return this.score - o.score;
  }
}
