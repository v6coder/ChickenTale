package com.chickentale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.ProgressBar;

/**
 * Dialog fragment showing a progress bar
 */
public class ProgressDialogFragment extends DialogFragment
{
	// The created dialog
	private Dialog dialog;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		// Build the dialog and keep track of it
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		builder.setView(inflater.inflate(R.layout.progress_dialog, null));
		dialog = builder.create();
		return dialog;
	}
	
	/**
	 * Sets the progress of the progress bar
	 * @param progress
	 */
	public void setProgress(int progress)
	{
		ProgressBar bar = (ProgressBar) dialog.findViewById(R.id.progressBar);
		bar.setProgress(progress);
	}
}
