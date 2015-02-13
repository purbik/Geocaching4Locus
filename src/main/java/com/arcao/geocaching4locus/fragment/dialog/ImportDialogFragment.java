package com.arcao.geocaching4locus.fragment.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.arcao.geocaching4locus.R;
import com.arcao.geocaching4locus.task.ImportTask;
import com.arcao.geocaching4locus.task.ImportTask.TaskListener;

import java.lang.ref.WeakReference;

public final class ImportDialogFragment extends AbstractDialogFragment implements TaskListener {
	public static final String FRAGMENT_TAG = ImportDialogFragment.class.getName();

	private static final String PARAM_CACHE_ID = "CACHE_ID";

	public interface DialogListener {
		void onImportFinished(boolean success);
	}

	private ImportTask mTask;
	private WeakReference<DialogListener> mDialogListenerRef;

	public static ImportDialogFragment newInstance(String cacheId) {
		ImportDialogFragment fragment = new ImportDialogFragment();

		Bundle args = new Bundle();
		args.putString(PARAM_CACHE_ID, cacheId);

		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRetainInstance(true);
		setCancelable(false);

		String cacheId = getArguments().getString(PARAM_CACHE_ID);

		mTask = new ImportTask(getActivity(), this);
		mTask.execute(cacheId);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			mDialogListenerRef = new WeakReference<>((DialogListener) activity);
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnTaskFinishedListener");
		}
	}

	@Override
	public void onTaskFinished(boolean success) {
		mTask = null;

		dismiss();

		DialogListener listener = mDialogListenerRef.get();
		if (listener != null) {
			listener.onImportFinished(success);
		}
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		if (mTask != null) {
			mTask.cancel(true);
		}
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		ProgressDialog dialog = new ProgressDialog(getActivity());
		dialog.setIndeterminate(true);
		dialog.setMessage(getText(R.string.import_cache_progress));
		dialog.setButton(ProgressDialog.BUTTON_NEGATIVE, getText(R.string.cancel_button), (OnClickListener) null);
		return dialog;
	}
}
