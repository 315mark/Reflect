package com.example.yangon.library.annotations;


import android.app.Activity;
import android.os.Bundle;

public class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//
		InjectMananger.inject(this);  //
	}
}
