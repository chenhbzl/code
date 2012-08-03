package com.itcast.collage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import android.provider.MediaStore;
import android.text.InputFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CreateCollageActivity extends Activity implements OnClickListener {
	/** Called when the activity is first created. */

	private ImageView exitBtn;
	private Button goToAlbumBtn;
	private Button addPicBtn;
	private EditText collageNameET;
	private TextView textView; 
	private Handler myHandler; 
	private String sdcardRootPath;
	private ArrayList<String> paths;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.create_collage);
		sdcardRootPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		paths = new ArrayList<String>();
		paths.add("/sdcard/3.jpg");
 		paths.add("/sdcard/2.jpg");	
 		paths.add("/sdcard/1.jpg");
 		paths.add("/sdcard/4.jpg");
 		paths.add("/sdcard/ha.jpg");
 		paths.add("/sdcard/hb.jpg");
 		paths.add("/sdcard/hc.jpg");
 		paths.add("/sdcard/hd.jpg");
 		paths.add("/sdcard/a.jpg");
 		paths.add("/sdcard/b.jpg");
 		paths.add("/sdcard/d.jpg");
 		paths.add("/sdcard/c.jpg");
		exitBtn = (ImageView) findViewById(R.id.create_exitbtn);
		textView = (TextView) findViewById(R.id.create_tv_selectedcount);
		addPicBtn = (Button) findViewById(R.id.create_addpicbtn);
		goToAlbumBtn = (Button) findViewById(R.id.create_gobtn);
		collageNameET = (EditText) findViewById(R.id.create_collagenameet);
		
		exitBtn.setOnClickListener(this);
		addPicBtn.setOnClickListener(this);
		goToAlbumBtn.setOnClickListener(this);

		
		initView();

		myHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				textView.setText("Already selected " + paths.size());
			}
		};
		
		
		
		
		
		
		
//		Bundle bundle = new Bundle();
//		bundle.putString("collageName", collageNameET.getText().toString().trim());
//		bundle.putStringArrayList("paths", paths);
//		Intent it = new Intent();
//		it.putExtras(bundle);
//		it.setClass(CreateCollageActivity.this, EditCollageActivity.class);
//		startActivity(it);
//		
		
		
		
		
		
		
		

	}

	private void initView() { 
		textView.setText("Already selected " + paths.size());
		int ran = (int) Math.rint(Math.random() * (1000 - 1));
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
		String date = dateformat.format(new Date());
		String defaultName = "collage" + date + ran;
		collageNameET.setText(defaultName);
//		collageNameET.setImeOptions(EditorInfo.IME_ACTION_DONE
//				| EditorInfo.IME_FLAG_NO_EXTRACT_UI);
		collageNameET
				.setFilters(new InputFilter[] { new InputFilter.LengthFilter(5) });
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		System.out.println(v.getId());
		switch (v.getId()) {
		case R.id.create_exitbtn:
			this.finish();
			break;
			
		case R.id.create_gobtn:
			if(collageNameET.getText().toString().trim().length() == 0){
				Toast.makeText(CreateCollageActivity.this, "Please Input Your Collage Name.", 
						Toast.LENGTH_LONG).show();
			} else if(!checkName(collageNameET.getText().toString())){
				Toast.makeText(CreateCollageActivity.this, "Collage Name is invaild.", 
						Toast.LENGTH_LONG).show();
			} else if(hasSameName(collageNameET.getText().toString())){
				Toast.makeText(CreateCollageActivity.this, "Collage Name has already existed.", 
						Toast.LENGTH_LONG).show();
			}else{
				 
				Bundle bundle = new Bundle();
				bundle.putString("collageName", collageNameET.getText().toString().trim());
				bundle.putStringArrayList("paths", paths);
				Intent it = new Intent();
				it.putExtras(bundle);
				it.setClass(CreateCollageActivity.this, EditCollageActivity.class);
				startActivity(it);
			}
			break;
			
		case R.id.create_addpicbtn:
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intent, 1);
			break;
		}
	}

	public static boolean checkName(String name) {
		boolean pass = false;
		if (name.contains("@") || name.contains("#") || name.contains("!")
				|| name.contains("$") || name.contains("%")
				|| name.contains("*") || name.contains("/")
				|| name.contains(":")) {
			pass = false;
		} else {
			pass = true;
		}
		return pass;
	}

	public boolean hasSameName(String name) {
		boolean has = false;
		if (name != null) {
			name += ".jpg";
			String rootPath = sdcardRootPath + "/collage";
			File file = new File(rootPath);
			String[] fileNames = file.list();

			if (fileNames != null) {
				int len = fileNames.length;
				for (int i = 0; i < len; i++) {
					if (fileNames[i].equals(name)) {
						has = true;
					}
				}
			}
		}
		return has;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			Uri uri = data.getData();
			String[] projection = { MediaStore.Images.Media.DATA };
			Cursor cursor = managedQuery(uri, projection, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			paths.add(cursor.getString(column_index));
			myHandler.sendEmptyMessage(0);
		}

		super.onActivityResult(requestCode, resultCode, data);

	}
}