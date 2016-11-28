package com.mss.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.mss.activities.R;

/**
 * 
 * @author master software solutions
 * 
 *         This class must be used to show Alert dialog throughout the App
 * 
 */

public class Alert {
	private Activity	mActivity;

	public Alert(Activity activity) {
		mActivity = activity;
	}

	public void showAlert(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		builder.setTitle(R.string.app_name);
		builder.setMessage(message);

		builder.setCancelable(false);

		Session.setAccerlValue(new Utils().random(1, 1));
		Session.setGyroValue(new Utils().random(0, 0));
		Session.setGpsValue(new Utils().random(4, 14));

		AlertDialog dialog = builder.show();

		Window window = dialog.getWindow();
		WindowManager.LayoutParams wlp = window.getAttributes();

		wlp.gravity = Gravity.TOP;
		wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		window.setAttributes(wlp);

		TextView txtMessage = (TextView) dialog.findViewById(android.R.id.message);
		if (txtMessage != null)
			txtMessage.setGravity(Gravity.CENTER);
		dialog.show();

	}

}
