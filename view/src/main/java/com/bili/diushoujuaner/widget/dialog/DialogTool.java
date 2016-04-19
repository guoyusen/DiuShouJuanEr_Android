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

    public static void createDropInfoDialog(final Context context, final OnDialogPositiveClickListener onDialogPositiveClickListener) {
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.layout_dialog_drop_info);
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
        window.setContentView(R.layout.layout_dialog_comment_delete);
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

    public static void createChoseGenderDialog(final Context context, final OnGenderChoseListener onGenderChoseListener){
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.dialogWindowAnim);
        window.setBackgroundDrawableResource(R.color.TRANSPARENT);
        window.setContentView(R.layout.layout_dialog_chose_gender);
        window.setGravity(Gravity.BOTTOM);
        RelativeLayout layoutMale = (RelativeLayout) window.findViewById(R.id.layoutMale);
        layoutMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGenderChoseListener.onDialogChose("男");
                dialog.dismiss();
            }
        });
        RelativeLayout layoutFemale = (RelativeLayout) window.findViewById(R.id.layoutFemale);
        layoutFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGenderChoseListener.onDialogChose("女");
                dialog.dismiss();
            }
        });
    }

    public static void createRemoveRecallDialog(final Context context, final OnDialogPositiveClickListener onDialogPositiveClickListener){
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.layout_dialog_delete_recall);
        Button btnCancle = (Button) window.findViewById(R.id.btnCancle);
        Button btnSure = (Button) window.findViewById(R.id.btnSure);
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onDialogPositiveClickListener.onPositiveClicked();
            }
        });
    }

    public static void createPictureSaveDialog(final Context context, final OnDialogPositiveClickListener onDialogPositiveClickListener){
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.layout_dialog_picture_save);
        window.setBackgroundDrawableResource(R.color.TRANSPARENT);
        window.setWindowAnimations(R.style.dialogWindowAnim);
        window.setGravity(Gravity.BOTTOM);
        RelativeLayout layoutCancle = (RelativeLayout) window.findViewById(R.id.layoutCancle);
        RelativeLayout layoutSave = (RelativeLayout) window.findViewById(R.id.layoutSave);
        layoutCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        layoutSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onDialogPositiveClickListener.onPositiveClicked();
            }
        });
    }

    public static void createReSendDialog(final Context context, final OnDialogPositiveClickListener onDialogPositiveClickListener){
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.layout_dialog_re_send);
        window.setBackgroundDrawableResource(R.color.TRANSPARENT);
        window.setWindowAnimations(R.style.dialogWindowAnim);
        window.setGravity(Gravity.BOTTOM);
        RelativeLayout layoutCancle = (RelativeLayout) window.findViewById(R.id.layoutCancle);
        RelativeLayout layoutSend = (RelativeLayout) window.findViewById(R.id.layoutSend);
        layoutCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        layoutSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onDialogPositiveClickListener.onPositiveClicked();
            }
        });
    }

}
