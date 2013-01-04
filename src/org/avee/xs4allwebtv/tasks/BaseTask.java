package org.avee.xs4allwebtv.tasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public abstract class BaseTask<Tparams, Tprogress, Tresult> extends AsyncTask<Tparams, Tprogress, Tresult> {
	protected Activity context;
	protected String busyMessage;
	protected String errorMessage;
	protected Exception error;
	protected ProgressDialog busyIndicator;
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if(busyMessage != null)
		{
			busyIndicator.setMessage(busyMessage);
			busyIndicator.show();
		}
	}
	
	protected BaseTask(Activity context, String busyMessage) {
		this.context = context;
		this.busyMessage = busyMessage;

        busyIndicator = new ProgressDialog(context);
        busyIndicator.setIndeterminate(true);
        busyIndicator.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	}
	
	protected void onPostExecute(Tresult result) {
		if(errorMessage == null && error == null) {
			onSuccess(result);
			busyIndicator.dismiss();
		}
		else
		{
			if(error != null)
				Log.e("org.avee.xs4allwebtv.tasks.BaseTask", "Task Failed", error);
			
			String message = errorMessage != null ? errorMessage : error.getMessage();
			
			busyIndicator.dismiss();
			AlertDialog.Builder altDialog= new AlertDialog.Builder(context);
			altDialog.setMessage(message);
			altDialog.show();
		}
	}

	protected abstract void onSuccess(Tresult result);
}
