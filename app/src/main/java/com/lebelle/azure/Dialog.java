package com.lebelle.azure;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;

import com.lebelle.azure.data.AppPrefs;

/**
 * Created by HP on 06-Jan-18.
 */

public class Dialog {

    Context context;

    public Dialog(Context context){this.context = context;}

    public static void addLocationDialog(final Context context){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.ic_location);
        builder.setTitle(R.string.choose);
        //set up input method
        final EditText inputEditText = new EditText(context);
        inputEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(inputEditText);

        builder.setNegativeButton(R.string.dismiss, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              String location = inputEditText.getText().toString();
                AppPrefs.CreateCityInSharedPrefs(context, location);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
