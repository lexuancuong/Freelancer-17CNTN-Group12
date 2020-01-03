package com.example.freelancer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

public class TopUpDialog extends AppCompatDialogFragment {

    private EditText txtPassword;
    private EditText txtAmount;
    private String myFunc;
    private ExampleDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_top_up_dialog, null);
        TextView txtAmountShow = (TextView) view.findViewById(R.id.txtAmountLabel);
        Bundle args = getArguments();
        int f = args.getInt("function");
        if (f == 1) {
            myFunc = "Withdraw";
            txtAmountShow.setText("Amount to Withdraw");
        }

        else {
            myFunc = "Top up";
            txtAmountShow.setText("Amount to Top up");

        }
        builder.setView(view)
                .setTitle(myFunc)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String password = txtPassword.getText().toString();
                        String amount = txtAmount.getText().toString();
                        if (myFunc == "Withdraw")
                            amount = "-"+amount;
                        listener.applyTexts(password, amount);
                    }
                });

        txtPassword = view.findViewById(R.id.txtPassword);
        txtAmount = view.findViewById(R.id.txtAmount);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface ExampleDialogListener
    {
        void applyTexts(String password, String amount);
    }
}
