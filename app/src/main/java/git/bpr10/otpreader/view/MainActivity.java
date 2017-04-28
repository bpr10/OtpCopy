package git.bpr10.otpreader.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.Toast;

import git.bpr10.otpreader.R;
import git.bpr10.otpreader.utils.Constants;
import git.bpr10.otpreader.utils.SharedPrefsUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  EditText edtUrl;
  SharedPrefsUtils sharedPrefsUtils;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    findViewById(R.id.btn_save_url).setOnClickListener(this);
    edtUrl = (EditText) findViewById(R.id.edt_save_url);
    edtUrl.setText(SharedPrefsUtils.getStringPreference(this, Constants.URL));
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_save_url: {
        saveUrl();
      }
    }
  }

  private void saveUrl() {
    String newUrl = edtUrl.getText().toString();
    if (newUrl.isEmpty() || !URLUtil.isValidUrl(newUrl)) {
      Toast.makeText(this, R.string.error_invalid_url, Toast.LENGTH_SHORT).show();
      return;
    }

    SharedPrefsUtils.setStringPreference(this, Constants.URL, newUrl);
  }
}
