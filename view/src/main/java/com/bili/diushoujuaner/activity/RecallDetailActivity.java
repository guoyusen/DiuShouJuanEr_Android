package com.bili.diushoujuaner.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.CommentAdapter;
import com.bili.diushoujuaner.adapter.ImageAdapter;
import com.bili.diushoujuaner.base.BaseFragmentActivity;
import com.bili.diushoujuaner.model.eventhelper.ResponEvent;
import com.bili.diushoujuaner.fragment.PictureFragment;
import com.bili.diushoujuaner.presenter.presenter.RecallDetailActivityPresenter;
import com.bili.diushoujuaner.presenter.presenter.impl.RecallDetailActivityPresenterImpl;
import com.bili.diushoujuaner.presenter.view.IRecallDetailView;
import com.bili.diushoujuaner.utils.CommonUtil;

import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.TimeUtil;
import com.bili.diushoujuaner.utils.entity.vo.PictureVo;
import com.bili.diushoujuaner.utils.entity.dto.CommentDto;
import com.bili.diushoujuaner.utils.entity.dto.GoodDto;

import com.bili.diushoujuaner.utils.entity.dto.RecallDto;
import com.bili.diushoujuaner.widget.CustomEditText;
import com.bili.diushoujuaner.widget.CustomGridView;
import com.bili.diushoujuaner.widget.CustomListView;
import com.bili.diushoujuaner.widget.dialog.DialogTool;
import com.bili.diushoujuaner.widget.dialog.OnDialogPositiveClickListener;
import com.bili.diushoujuaner.widget.scrollview.ReboundScrollView;
import com.bili.diushoujuaner.widget.TintedBitmapDrawable;
import com.bili.diushoujuaner.widget.aligntextview.CBAlignTextView;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/3/8.
 */
public class RecallDetailActivity extends BaseFragmentActivity<RecallDetailActivityPresenter> implements IRecallDetailView, View.OnClickListener {

    @Bind(R.id.ivNavHead)
    SimpleDraweeView ivNavHead;
    @Bind(R.id.txtAuthor)
    TextView txtAuthor;
    @Bind(R.id.txtRecallDetail)
    CBAlignTextView txtRecallDetail;
    @Bind(R.id.txtComment)
    CustomEditText txtComment;
    @Bind(R.id.customGridView)
    CustomGridView customGridView;
    @Bind(R.id.txtGoodDetail)
    TextView txtGoodDetail;
    @Bind(R.id.layoutGoodContent)
    LinearLayout layoutGoodContent;
    @Bind(R.id.layoutGood)
    LinearLayout layoutGood;
    @Bind(R.id.layoutComment)
    LinearLayout layoutComment;
    @Bind(R.id.ivGood)
    ImageView ivGood;
    @Bind(R.id.ivGoodSum)
    ImageView ivGoodSum;
    @Bind(R.id.ivComment)
    ImageView ivComment;
    @Bind(R.id.txtGood)
    TextView txtGood;
    @Bind(R.id.listViewComment)
    CustomListView listViewComment;
    @Bind(R.id.layoutSend)
    RelativeLayout layoutSend;
    @Bind(R.id.layoutBottom)
    RelativeLayout layoutBottom;
    @Bind(R.id.layoutParent)
    RelativeLayout layoutParent;
    @Bind(R.id.scrollView)
    ReboundScrollView scrollView;

    private ArrayList<PictureVo> pictureVoList;
    private long recallNo;
    private boolean goodStatus = false;
    private Handler handler;
    private CustomRunnable customRunnable;
    private CommentAdapter commentAdapter;
    private List<CommentDto> commentDtoList;
    private ImageAdapter imageAdapter;
    private Drawable thumbUpDrawable, thumbDownDrawable, commentNormalDrawable;
    private CommentConfig commentConfig;

    public static final String TAG = "RecallDetailActivity";

    class CommentConfig{
        public static final int TYPE_NONE = 0;
        public static final int TYPE_COMMENT = 1;
        public static final int TYPE_RESPON_FIRST = 2;
        public static final int TYPE_RESPON_SECOND = 3;

        private long receiveNo = -1;
        private long commentNo = -1;
        private long responNo = -1;
        private int type = 0;
        private String nickNameTo = "";
        private boolean isReady = false;
        private HashMap<String,String> draftMap = new HashMap<>();
        private StringBuilder stringBuilder = new StringBuilder();

        public void clearConfig(){
            clearDraft();
            this.type = TYPE_NONE;
            this.receiveNo = -1;
            this.commentNo = -1;
            this.responNo = -1;
            this.isReady = false;
            this.nickNameTo = "";
        }

        public boolean isConfigFree(){
            if(this.type == TYPE_NONE && this.receiveNo == -1 && this.commentNo == -1 && this.responNo == -1 && !this.isReady){
                return true;
            }
            return false;
        }

        private String getCurrentDraftMapKey(){
            stringBuilder.delete(0, stringBuilder.length());
            stringBuilder.append(receiveNo);
            if(this.type == TYPE_RESPON_FIRST){
                stringBuilder.append(commentNo);
            }else if(this.type == TYPE_RESPON_SECOND){
                stringBuilder.append(commentNo);
                stringBuilder.append(responNo);
            }
            return stringBuilder.toString();
        }

        /**
         * =====key=====
         * Comment: receiveNo + "";
         * ResponFirst: receiveNo + commentNo + "";
         * ResponSecond: receiveNo + commentNo + responNo + "";
         */
        public void addDraft(String value){
            draftMap.put(getCurrentDraftMapKey(), value);
        }

        public String getDraft(){
            return draftMap.get(getCurrentDraftMapKey());
        }

        public void clearDraft(){
            draftMap.remove(getCurrentDraftMapKey());
        }

        public boolean isReady() {
            return isReady;
        }

        public void setIsReady(boolean isReady) {
            this.isReady = isReady;
        }

        public long getCommentNo() {
            return commentNo;
        }

        public void setCommentNo(long commentNo) {
            this.commentNo = commentNo;
        }

        public long getReceiveNo() {
            return receiveNo;
        }

        public void setReceiveNo(long receiveNo) {
            this.receiveNo = receiveNo;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public long getResponNo() {
            return responNo;
        }

        public void setResponNo(long responNo) {
            this.responNo = responNo;
        }

        public String getNickNameTo() {
            return nickNameTo;
        }

        public void setNickNameTo(String nickNameTo) {
            this.nickNameTo = nickNameTo;
        }
    }

    class CustomRunnable implements Runnable{
        @Override
        public void run() {
            goodStatus = getBindPresenter().executeGoodChange(goodStatus,recallNo);
        }
    }

    class CommentTextWatcher implements TextWatcher{
        @Override
        public void afterTextChanged(Editable s) {
            if(s.toString().trim().length() > 0){
                layoutSend.setBackground(ContextCompat.getDrawable(context, R.drawable.layout_send_ready_bg));
                commentConfig.setIsReady(true);
            }else{
                layoutSend.setBackground(ContextCompat.getDrawable(context, R.drawable.layout_send_close_bg));
                commentConfig.setIsReady(false);
            }
            commentConfig.addDraft(s.toString());
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }

    class CustomTouchListener implements View.OnTouchListener{
        private boolean isResponForFocus = true;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    isResponForFocus = true;
                    break;
                case MotionEvent.ACTION_MOVE:
                    isResponForFocus = false;
                    break;
                case MotionEvent.ACTION_UP:
                    if(isResponForFocus){
                        layoutParent.setFocusable(true);
                        layoutParent.setFocusableInTouchMode(true);
                        layoutParent.requestFocus();
                    }
                    break;
            }

            return false;
        }
    }

    class CustomFocusChangeListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus){
                layoutBottom.setVisibility(View.GONE);
                CommonUtil.hideSoftInputFromWindow(context, txtComment);
            }
        }
    }

    @Override
    public void initIntentParam(Intent intent) {
        recallNo = intent.getLongExtra(TAG, -1);
    }

    @Override
    public void beforeInitView() {
        commentDtoList = new ArrayList<>();
        handler = new Handler();
        customRunnable = new CustomRunnable();
        pictureVoList = new ArrayList<>();
        commentConfig = new CommentConfig();

        commentNormalDrawable = new TintedBitmapDrawable(context.getResources(),R.mipmap.icon_comment,ContextCompat.getColor(context, R.color.COLOR_BFBFBF));
        thumbDownDrawable = new TintedBitmapDrawable(context.getResources(),R.mipmap.icon_good,ContextCompat.getColor(context, R.color.COLOR_BFBFBF));
        thumbUpDrawable = new TintedBitmapDrawable(context.getResources(),R.mipmap.icon_good,ContextCompat.getColor(context, R.color.COLOR_THEME_SUB));

        basePresenter = new RecallDetailActivityPresenterImpl(this, context);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_recall_detail);
    }

    @Override
    public void setViewStatus() {
        layoutGood.setOnClickListener(this);
        layoutComment.setOnClickListener(this);
        layoutSend.setOnClickListener(this);
        ivNavHead.setOnClickListener(this);

        layoutParent.setOnTouchListener(new CustomTouchListener());
        scrollView.setOnTouchListener(new CustomTouchListener());
        customGridView.setOnTouchListener(new CustomTouchListener());
        listViewComment.setOnTouchListener(new CustomTouchListener());

        ivComment.setImageDrawable(commentNormalDrawable);
        ivGoodSum.setImageDrawable(thumbUpDrawable);
        txtComment.addTextChangedListener(new CommentTextWatcher());
        txtComment.setOnFocusChangeListener(new CustomFocusChangeListener());

        imageAdapter = new ImageAdapter(this, pictureVoList);
        customGridView.setAdapter(imageAdapter);
        customGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PictureFragment.showPictureDetail(getSupportFragmentManager(), pictureVoList, position, true);
            }
        });
        commentAdapter = new CommentAdapter(this, commentDtoList);
        listViewComment.setAdapter(commentAdapter);

        getBindPresenter().showRecallDetailByRecallNo(recallNo);

        goodStatus = getBindPresenter().getGoodStatus(recallNo);
        setGoodStatus(goodStatus);

    }

    @Override
    public void showRecallDetail(RecallDto recallDto) {
        showPageHead(null, null, TimeUtil.getFormatTime(recallDto.getPublishTime()));
        txtRecallDetail.setText(recallDto.getContent());
        CommonUtil.displayDraweeView(recallDto.getUserPicPath(), ivNavHead);
        txtAuthor.setText(recallDto.getUserName());
        showGoodDetail(recallDto.getGoodList());

        pictureVoList.addAll(getBindPresenter().changePictureDtoListToPictureVoList(recallDto.getPictureList()));
        commentDtoList.addAll(recallDto.getCommentList());
        imageAdapter.notifyDataSetChanged();
        commentAdapter.notifyDataSetChanged();
    }

    /**
     * 设置点赞按钮的状态 我 是否已赞
     * @param isGood
     */
    @Override
    public void setGoodStatus(boolean isGood) {
        if (isGood) {
            layoutGood.setBackground(ContextCompat.getDrawable(context, R.drawable.rectangle_themesub));
            ivGood.setImageDrawable(thumbUpDrawable);
            txtGood.setText("已赞");
            txtGood.setTextColor(ContextCompat.getColor(context, R.color.TC_388ECD));
        } else {
            layoutGood.setBackground(ContextCompat.getDrawable(context, R.drawable.rectangle_bfbfbf));
            ivGood.setImageDrawable(thumbDownDrawable);
            txtGood.setText("赞");
            txtGood.setTextColor(ContextCompat.getColor(context, R.color.TC_BFBFBF));
        }
    }

    /**
     * 显示点赞列表
     * @param goodDtoList
     */
    private void showGoodDetail(List<GoodDto> goodDtoList) {
        if (CommonUtil.isEmpty(goodDtoList)) {
            layoutGoodContent.setVisibility(View.GONE);
            return;
        } else {
            layoutGoodContent.setVisibility(View.VISIBLE);
        }
        txtGoodDetail.setText(getBindPresenter().getSpannableString(goodDtoList));
        txtGoodDetail.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layoutGood:
                executeClickForGood();
                break;
            case R.id.layoutComment:
                executeClickForComment();
                break;
            case R.id.layoutSend:
                executeClickForSend();
                break;
            case R.id.ivNavHead:
                SpaceActivity.showSpaceActivity(this,this,getBindPresenter().getRecallDtoByRecallNo(recallNo).getUserNo());
                break;
        }
    }

    private void executeClickForGood(){
        showGoodDetail(getBindPresenter().changeGoodStatusToLocal(recallNo));
        handler.removeCallbacks(customRunnable);
        handler.postDelayed(customRunnable, 1500);
    }

    private void executeClickForComment(){
        layoutBottom.setVisibility(View.VISIBLE);
        txtComment.requestFocus();
        txtComment.setHint(R.string.send_hint);

        commentConfig.setReceiveNo(getBindPresenter().getRecallDtoByRecallNo(recallNo).getUserNo());
        commentConfig.setType(CommentConfig.TYPE_COMMENT);
        txtComment.setText(commentConfig.getDraft());
        CommonUtil.showSoftInputFromWindow(this, txtComment);
    }

    private void executeClickForSend(){
        if(!commentConfig.isReady()){
            return;
        }
        if(commentConfig.getType() == CommentConfig.TYPE_COMMENT){
            getBindPresenter().getCommentPublish(recallNo, commentConfig.getDraft());
        }else if(commentConfig.getType() == CommentConfig.TYPE_RESPON_FIRST || commentConfig.getType() == CommentConfig.TYPE_RESPON_SECOND){
            getBindPresenter().getResponPublish(recallNo, commentConfig.getCommentNo(), commentConfig.getReceiveNo(),commentConfig.getDraft(), commentConfig.getNickNameTo());
        }

        commentConfig.clearConfig();
        txtComment.clearFocus();
        txtComment.setText(null);
        txtComment.setHint("说点什么呗...");
        layoutBottom.setVisibility(View.GONE);
        CommonUtil.hideSoftInputFromWindow(context, txtComment);
    }

    @Override
    public void showGooderSpace(Long userNo) {
        startActivity(new Intent(context, SpaceActivity.class).putExtra(SpaceActivity.TAG, userNo));
    }

    @Override
    public void refreshComment() {
        commentAdapter.refresh(getBindPresenter().getRecallDtoByRecallNo(recallNo).getCommentList());
        commentAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResponEvent(ResponEvent responEvent){
        switch (responEvent.getType()){
            case ConstantUtil.COMMENT_CLICK_LAYOUT_RESPON:
                if(responEvent.getCommentNo() == null){
                    return;
                }
                commentConfig.setType(CommentConfig.TYPE_RESPON_FIRST);
                commentConfig.setCommentNo(responEvent.getCommentNo());
                commentConfig.setReceiveNo(responEvent.getToNo());
                commentConfig.setNickNameTo(responEvent.getNickName());
                break;
            case ConstantUtil.COMMENT_CLICK_COMMENT_CONTENT:
                if(responEvent.getCommentNo() == null){
                    return;
                }
                if(responEvent.getToNo() == getBindPresenter().getLoginedUserNo()){
                    showFooterDialog(responEvent.getCommentNo(), -1, ConstantUtil.DELETE_COMMENT);
                    return;
                }else{
                    commentConfig.setType(CommentConfig.TYPE_RESPON_FIRST);
                    commentConfig.setCommentNo(responEvent.getCommentNo());
                    commentConfig.setReceiveNo(responEvent.getToNo());
                    commentConfig.setNickNameTo(responEvent.getNickName());
                }
                break;
            case ConstantUtil.COMMENT_CLICK_SUB_RESPON:
                if(responEvent.getResponNo() == null){
                    return;
                }
                if(responEvent.getToNo() == getBindPresenter().getLoginedUserNo()){
                    showFooterDialog(responEvent.getCommentNo(), responEvent.getResponNo(), ConstantUtil.DELETE_RESPON);
                    return;
                }else{
                    commentConfig.setType(CommentConfig.TYPE_RESPON_SECOND);
                    commentConfig.setCommentNo(responEvent.getCommentNo());
                    commentConfig.setReceiveNo(responEvent.getToNo());
                    commentConfig.setResponNo(responEvent.getResponNo());
                    commentConfig.setNickNameTo(responEvent.getNickName());
                }
                break;
        }
        txtComment.requestFocus();
        txtComment.setHint("回复" + commentConfig.getNickNameTo() + ":");
        txtComment.setText(commentConfig.getDraft());
        CommonUtil.showSoftInputFromWindow(context, txtComment);
        layoutBottom.setVisibility(View.VISIBLE);
    }

    private void showFooterDialog(final long commentNo, final long responNo, final int type){
        DialogTool.createDeleteCommentDialog(context, new OnDialogPositiveClickListener() {
            @Override
            public void onPositiveClicked() {
                if(type == ConstantUtil.DELETE_COMMENT){
                    getBindPresenter().getCommentRemove(recallNo, commentNo);
                }else if(type == ConstantUtil.DELETE_RESPON){
                    getBindPresenter().getResponRemove(recallNo, commentNo, responNo);
                }
            }
        });

    }

}
