package org.avee.xs4allwebtv.tasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

/***
 * Abstract subclass of the android AsyncTask which add handling of errors and
 * (optionally) shows a busy indicator. This class is used in the same way as
 * {@link AsyncTask}, but instead of overriding
 * {@link AsyncTask#onPostExecute(Result)} subclasses should override
 * {@link BaseTask#onSuccess(Object)} which will only be called when execution
 * was succesfull.
 * 
 * To indicate the fact that an error has occurred subclasses should set either
 * {@link #errorMessage} or {@link #error} in their override of
 * {@link AsyncTask#doInBackground(Params...)}.
 * 
 * @author AVee
 * 
 * @param <Tparams>
 * @param <Tprogress>
 * @param <Tresult>
 * 
 * @see AsyncTask
 */
public abstract class BaseTask<Tparams, Tprogress, Tresult> extends
		AsyncTask<Tparams, Tprogress, Tresult> {
	protected Activity context;
	protected String busyMessage;
	protected String errorMessage;
	protected Exception error;
	protected ProgressDialog busyIndicator;

	/***
	 * @see AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (busyMessage != null) {
			busyIndicator.setMessage(busyMessage);
			busyIndicator.show();
		}
	}

	/***
	 * Constructs a new BaseTask.
	 * 
	 * @param context
	 *            Required. The Activity which launched this background task,
	 *            needed to be able to show error dialogs.
	 * @param busyMessage
	 *            Optional. The message to show in the BusyIndicator. When set
	 *            to null a busy indicator will not be shown.
	 */
	protected BaseTask(Activity context, String busyMessage) {
		this.context = context;
		this.busyMessage = busyMessage;

		busyIndicator = new ProgressDialog(context);
		busyIndicator.setIndeterminate(true);
		busyIndicator.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	}

	/***
	 * Called after the task has been executed. Don't override this, override
	 * onSuccess() instead.
	 * 
	 * @see #onSuccess(Object)
	 * @see AsyncTask#onPostExecute(Result)
	 */
	protected void onPostExecute(Tresult result) {
		if (errorMessage == null && error == null) {
			onSuccess(result);
			busyIndicator.dismiss();
		} else {
			if (error != null)
				Log.e("org.avee.xs4allwebtv.tasks.BaseTask", "Task Failed",
						error);

			String message = errorMessage != null ? errorMessage : error
					.getMessage();

			busyIndicator.dismiss();
			AlertDialog.Builder altDialog = new AlertDialog.Builder(context);
        	altDialog.setTitle("Error");
			altDialog.setMessage(message);
			altDialog.setPositiveButton("OK", null);
			altDialog.show();
		}
	}

	/***
	 * This method will be called when the task finished succesfully. Override
	 * this method to handle the result. When the task failed this method will
	 * not be called, instead an error will be displayed.
	 * 
	 * @param result
	 */
	protected abstract void onSuccess(Tresult result);
}
