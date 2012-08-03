package cn.itcast.douban;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class AboutActivity extends Activity {
	WebView wv;
	String path = "http://192.168.1.247:8080/about.html";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		wv= (WebView) this.findViewById(R.id.webviewabout);

		WebSettings settings = wv.getSettings();
		settings.setJavaScriptEnabled(true);

		wv.addJavascriptInterface(new Object(){
			public void callphone(){
				System.out.println("click");
            	Uri uri= Uri.parse("tel:138000000");
                Intent mIntent = new Intent(Intent.ACTION_CALL,uri);    
                startActivity(mIntent);   
			}
		}, "demo");
		wv.loadUrl(path);
	}

}
