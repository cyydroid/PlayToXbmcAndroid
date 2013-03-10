package com.khloke.PlayToXbmcAndroid.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.khloke.PlayToXbmcAndroid.R;
import com.khloke.PlayToXbmcAndroid.objects.XbmcClient;

/**
 * Created by IntelliJ IDEA.
 * User: khloke
 * Date: 10/03/13
 * Time: 1:58 PM
 */
public class NewClientFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.dialog_new_client, null);

        //If not a new client, fill in the existing client's info
        if (getArguments() != null) {
            XbmcClient xbmcClient = XbmcClient.load(getActivity(), String.valueOf(getArguments().get("id")));
            EditText inputNameView = (EditText) dialogLayout.findViewById(R.id.editTextName);
            inputNameView.setText(xbmcClient.getName());
            EditText inputAddressView = (EditText) dialogLayout.findViewById(R.id.editTextAddress);
            inputAddressView.setText(xbmcClient.getAddress());
            EditText inputPortView = (EditText) dialogLayout.findViewById(R.id.editTextPort);
            inputPortView.setText(xbmcClient.getPort());
        }

        builder
                .setTitle(R.string.new_client_title)
                .setPositiveButton(R.string.new_client_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        EditText inputNameView = (EditText) dialogLayout.findViewById(R.id.editTextName);
                        EditText inputAddressView = (EditText) dialogLayout.findViewById(R.id.editTextAddress);
                        EditText inputPortView = (EditText) dialogLayout.findViewById(R.id.editTextPort);

                        if (getArguments() == null) {
                            new XbmcClient(inputNameView.getText().toString(), inputAddressView.getText().toString(), inputPortView.getText().toString()).save(getActivity());
                        } else {
                            XbmcClient xbmcClient = XbmcClient.load(getActivity(), String.valueOf(getArguments().get("id")));
                            xbmcClient.setName(inputNameView.getText().toString());
                            xbmcClient.setAddress(inputAddressView.getText().toString());
                            xbmcClient.setPort(inputPortView.getText().toString());
                            xbmcClient.save(getActivity());
                        }
                        Toast.makeText(getActivity(), "New Client Saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.new_client_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getActivity(), "Don't save it!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setView(dialogLayout);

        return builder.create();
    }
}
