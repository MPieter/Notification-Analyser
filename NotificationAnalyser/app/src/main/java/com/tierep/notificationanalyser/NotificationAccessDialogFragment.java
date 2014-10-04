package com.tierep.notificationanalyser;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class NotificationAccessDialogFragment extends DialogFragment {
    private NotificationAccessDialogFragmentListener mListener;

    public NotificationAccessDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (NotificationAccessDialogFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement NotificationAccessDialogFragmentListener");
        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity()); // TODO verfiy theme (Holo ??)
        builder.setMessage(getString(R.string.NotificationAccessDialogFragment_Message))
               .setTitle(R.string.NotificationAccessDialogFragment_Title)
               .setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton(R.string.NotificationAccessDialogFragment_Confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mListener.onDialogPositiveClick(NotificationAccessDialogFragment.this);
            }
        });
        builder.setNegativeButton(R.string.NotificationAccessDialogFragment_Cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mListener.onDialogNegativeClick(NotificationAccessDialogFragment.this);
            }
        });

        return builder.create();
    }

    public interface NotificationAccessDialogFragmentListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
}
