package cn.itcast.mobilesafe;

import cn.itcast.mobilesafe.service.AddressService;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class FindAddressActivity extends Activity {
	EditText et_address;
	AddressService service;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.find_address);
		et_address = (EditText) this.findViewById(R.id.et_number_address);
		service = new AddressService();
	}

	public void findAddress(View view){
	   String number =	et_address.getText().toString();
		String address = service.getAddress(number);
		Toast.makeText(this, address, 1).show();
	}

}
