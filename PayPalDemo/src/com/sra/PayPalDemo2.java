package com.sra;

import java.math.BigDecimal;

import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalPayment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

public class PayPalDemo2 extends Activity implements OnClickListener {

	private PaypalAsuTask mPATask;
	private final int PAYPAL_ID = 10001;
	private CheckoutButton mChkButton;
	private Integer[] mImg_cost = { 12, 7, 10, 22 };
	private ProgressDialog pDialog;
	private Handler mHandler;
	private PayPalPayment newPayment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);


		pDialog = new ProgressDialog(PayPalDemo2.this);

		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {

				Toast.makeText(getApplicationContext(),
						" Toast in close dialog ", 1).show();
				if (msg.what == 1) {
					pDialog.cancel();
					Intent checkoutIntent = PayPal.getInstance().checkout(
							newPayment, PayPalDemo2.this);
					startActivityForResult(checkoutIntent, 2);
				}

			}
		};

		// nextScreen();

	}

	public void onClick(View v) {
		// TODO Auto-generated method stub

		nextScreen();

		Toast.makeText(getApplicationContext(), " Toast in show dialog ", 1)
				.show();
	}

	public void paypalLoad(View v) {

		
				 mPATask = new PaypalAsuTask();
				mPATask.execute();
			
		

	}

	private class PaypalAsuTask extends AsyncTask<Void, Void, Void> {

		 ProgressDialog pDialog = new ProgressDialog(PayPalDemo2.this);

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			RelativeLayout rl = (RelativeLayout) findViewById(R.id.xRl);
			rl.addView(mChkButton);

			mChkButton.setOnClickListener(PayPalDemo2.this);
			pDialog.cancel();

		}

		@Override
		protected void onPreExecute() {

			pDialog.setMessage(" inilizing pay library ");
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... params) {

			PayPal pp;
			pp = PayPal.getInstance();
			pp = PayPal.initWithAppID(getApplicationContext(),
					"APP-80W284485P519543T", PayPal.ENV_SANDBOX);
			pp.setLanguage("en_US");
			mChkButton = pp.getCheckoutButton(PayPalDemo2.this,
					PayPal.BUTTON_152x33, CheckoutButton.TEXT_PAY);
			RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			parms.addRule(RelativeLayout.CENTER_IN_PARENT);
			mChkButton.setId(PAYPAL_ID);
			mChkButton.setLayoutParams(parms);

			return null;
		}

	}

	private void nextScreen() {

		pDialog = new ProgressDialog(PayPalDemo2.this);
		pDialog.setMessage(" Loding paypal Login Page ...");
		pDialog.show();

		Runnable r = new Runnable() {

			public void run() {

				newPayment = new PayPalPayment();
				newPayment.setSubtotal(new BigDecimal(mImg_cost[3]));
				newPayment.setCurrencyType("USD");
				newPayment.setRecipient("vekariya1994@gmail.com");
				newPayment.setMerchantName("Bhagvati's Store");
				mHandler.sendEmptyMessage(1);
			}
		};
		Thread th = new Thread(r);
		th.start();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		String msg = " req code :" + requestCode;
		msg = msg + " result:" + resultCode;
		if (data.getData() != null) {
			msg = msg + " data :" + data.getData();
		}
		Toast.makeText(getApplicationContext(), msg, 1).show();
	}

}
