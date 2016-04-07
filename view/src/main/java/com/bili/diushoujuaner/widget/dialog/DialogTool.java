package com.bili.diushoujuaner.widget.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.bili.diushoujuaner.R;

/**
 * Created by BiLi on 2016/4/7.
 */
public class DialogTool {

    public static void createUserInfoExitDialog(final Context context, final OnDialogPositiveClickListener onDialogPositiveClickListener) {
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.layout_user_edit_exit_dialog);
        Button btnCancle = (Button) window.findViewById(R.id.btnCancle);
        Button btnKeep = (Button) window.findViewById(R.id.btnKeep);
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onDialogPositiveClickListener.onPositiveClicked();
            }
        });
        btnKeep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public static void createDeleteCommentDialog(final Context context, final OnDialogPositiveClickListener onDialogPositiveClickListener){
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.dialogWindowAnim);
        window.setBackgroundDrawableResource(R.color.TRANSPARENT);
        window.setContentView(R.layout.layout_comment_delete_dialog);
        window.setGravity(Gravity.BOTTOM);
        RelativeLayout layoutCancel = (RelativeLayout) window.findViewById(R.id.layoutCancle);
        layoutCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        RelativeLayout layoutDelete = (RelativeLayout) window.findViewById(R.id.layoutDelete);
        layoutDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDialogPositiveClickListener.onPositiveClicked();
                dialog.dismiss();
            }
        });
    }

    public static void createGenderDialog(final Context context, final OnDialogChoseListener onDialogChoseListener){
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.dialogWindowAnim);
        window.setBackgroundDrawableResource(R.color.TRANSPARENT);
        window.setContentView(R.layout.layout_gender_dialog);
        window.setGravity(Gravity.BOTTOM);
        RelativeLayout layoutMale = (RelativeLayout) window.findViewById(R.id.layoutMale);
        layoutMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDialogChoseListener.onDialogChose("男");
                dialog.dismiss();
            }
        });
        RelativeLayout layoutFemale = (RelativeLayout) window.findViewById(R.id.layoutFemale);
        layoutFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDialogChoseListener.onDialogChose("女");
                dialog.dismiss();
            }
        });
    }

}
