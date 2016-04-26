package com.bili.diushoujuaner.widget.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bili.diushoujuaner.R;

/**
 * Created by BiLi on 2016/4/7.
 */
public class DialogTool {

    public static void createDropInfoDialog(final Context context, final OnDialogPositiveClickListener onDialogPositiveClickListener) {
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setCanceledOnTouchOutside(false);
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

    public static void createLoginConflictDialog(final Context context, final OnConflictClickListener onConflictClickListener) {
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.layout_dialog_login_conflict);
        Button btnExit = (Button) window.findViewById(R.id.btnExit);
        Button btnLogin = (Button) window.findViewById(R.id.btnLogin);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onConflictClickListener.onExitClick();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onConflictClickListener.onReLoginClick();
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

    public static void createDeleteContactDialog(final Context context, String content, final OnDialogPositiveClickListener onDialogPositiveClickListener){
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.layout_dialog_delete_contact);
        Button btnCancle = (Button) window.findViewById(R.id.btnCancle);
        Button btnSure = (Button) window.findViewById(R.id.btnSure);
        TextView txtWarning = (TextView) window.findViewById(R.id.txtWarning);
        txtWarning.setText(content);
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

    public static void createPartyOperateDialog(final Context context, final OnPartyClickListener onPartyClickListener){
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.layout_dialog_party);
        window.setBackgroundDrawableResource(R.color.TRANSPARENT);
        window.setWindowAnimations(R.style.dialogWindowAnim);
        window.setGravity(Gravity.BOTTOM);
        RelativeLayout layoutCancle = (RelativeLayout) window.findViewById(R.id.layoutCancle);
        RelativeLayout layoutBuild = (RelativeLayout) window.findViewById(R.id.layoutBuild);
        RelativeLayout layoutSearch = (RelativeLayout) window.findViewById(R.id.layoutSearch);
        layoutCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        layoutBuild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onPartyClickListener.onBuildPartyClick();
            }
        });
        layoutSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onPartyClickListener.onSearchPartyClick();
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

    public static void createFriendAddDialog(final Context context, final OnDialogPositiveClickListener onDialogPositiveClickListener){
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.layout_dialog_space_add);
        window.setBackgroundDrawableResource(R.color.TRANSPARENT);
        window.setWindowAnimations(R.style.dialogWindowAnim);
        window.setGravity(Gravity.BOTTOM);
        RelativeLayout layoutCancle = (RelativeLayout) window.findViewById(R.id.layoutCancle);
        RelativeLayout layoutAdd = (RelativeLayout) window.findViewById(R.id.layoutAdd);
        layoutCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        layoutAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onDialogPositiveClickListener.onPositiveClicked();
            }
        });
    }

    public static void createFriendDetailDialog(Context context, final OnFriendDetailClickListener friendDetailClickListener){
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.layout_dialog_friend_detail);
        window.setBackgroundDrawableResource(R.color.TRANSPARENT);
        window.setWindowAnimations(R.style.dialogWindowAnim);
        window.setGravity(Gravity.BOTTOM);
        RelativeLayout layoutCancle = (RelativeLayout) window.findViewById(R.id.layoutCancle);
        RelativeLayout layoutDelete = (RelativeLayout) window.findViewById(R.id.layoutDelete);
        RelativeLayout layoutRemark = (RelativeLayout) window.findViewById(R.id.layoutRemark);

        layoutCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        layoutDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                friendDetailClickListener.onDeleteFriendClick();
            }
        });
        layoutRemark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                friendDetailClickListener.onModifyRemarkClick();
            }
        });
    }

    public static void createPartyDetailDialog(final Context context, boolean isOwner, final OnPartyDetailClickListener onPartyDetailClickListener){
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.layout_dialog_party_detail);
        window.setBackgroundDrawableResource(R.color.TRANSPARENT);
        window.setWindowAnimations(R.style.dialogWindowAnim);
        window.setGravity(Gravity.BOTTOM);
        RelativeLayout layoutCancle = (RelativeLayout) window.findViewById(R.id.layoutCancle);
        View line = window.findViewById(R.id.line);
        RelativeLayout layoutExit = (RelativeLayout) window.findViewById(R.id.layoutExit);
        RelativeLayout layoutHead = (RelativeLayout) window.findViewById(R.id.layoutHead);
        if(isOwner){
            layoutHead.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
            ((TextView) window.findViewById(R.id.txtExit)).setText("解散该群");
        }else{
            layoutHead.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            ((TextView) window.findViewById(R.id.txtExit)).setText("退出该群");
        }
        layoutCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        layoutExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onPartyDetailClickListener.onPartyExit();
            }
        });
        layoutHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onPartyDetailClickListener.onUpdateHead();
            }
        });
    }

}
