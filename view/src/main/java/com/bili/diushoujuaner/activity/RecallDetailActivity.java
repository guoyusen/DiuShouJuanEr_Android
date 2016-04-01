package com.bili.diushoujuaner.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.CommentAdapter;
import com.bili.diushoujuaner.adapter.ImageAdapter;
import com.bili.diushoujuaner.base.BaseFragmentActivity;
import com.bili.diushoujuaner.event.CommentEvent;
import com.bili.diushoujuaner.fragment.PictureFragment;
import com.bili.diushoujuaner.model.tempHelper.GoodTemper;
import com.bili.diushoujuaner.model.tempHelper.RecallTemper;
import com.bili.diushoujuaner.presenter.presenter.RecallDetailActivityPresenter;
import com.bili.diushoujuaner.presenter.view.IRecallDetailView;
import com.bili.diushoujuaner.utils.Common;

import com.bili.diushoujuaner.utils.entity.PictureVo;
import com.bili.diushoujuaner.utils.response.CommentDto;
import com.bili.diushoujuaner.utils.response.GoodDto;

import com.bili.diushoujuaner.widget.CustomGridView;
import com.bili.diushoujuaner.widget.CustomListView;
import com.bili.diushoujuaner.widget.TintedBitmapDrawable;
import com.bili.diushoujuaner.widget.aligntextview.CBAlignTextView;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;
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
    EditText txtComment;
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

    private ArrayList<PictureVo> pictureVoList;
    private long recallNo;
    private long currentNo;
    private boolean goodstatus = false;
    private Handler handler;
    private CustomRunnable customRunnable;
    private CommentAdapter commentAdapter;
    private List<CommentDto> commentDtoList;
    private ImageAdapter imageAdapter;
    private HashMap<Long,String> draftMap = new HashMap<>();
    private Drawable thumbUpDrawable, thumbDownDrawable, commentNormalDrawable;

    public static final String TAG = "RecallDetailActivity";

    @Override
    public void initIntentParam(Intent intent) {
        recallNo = intent.getLongExtra(TAG, -1);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_recall_detail);
    }

    @Override
    public void beforeInitView() {
        commentDtoList = new ArrayList<>();
        handler = new Handler();
        customRunnable = new CustomRunnable();
        pictureVoList = new ArrayList<>();

        commentNormalDrawable = new TintedBitmapDrawable(context.getResources(),R.mipmap.icon_comment,ContextCompat.getColor(context, R.color.COLOR_BFBFBF));
        thumbDownDrawable = new TintedBitmapDrawable(context.getResources(),R.mipmap.icon_good,ContextCompat.getColor(context, R.color.COLOR_BFBFBF));
        thumbUpDrawable = new TintedBitmapDrawable(context.getResources(),R.mipmap.icon_good,ContextCompat.getColor(context, R.color.COLOR_THEME));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layoutGood:
                if(GoodTemper.getGoodStatus(recallNo)){
                    GoodTemper.setGoodStatus(recallNo, false);
                    setGoodStatus(false);
                    getRelativePresenter().removeGoodDtoFromTemper(recallNo);
                }else{
                    GoodTemper.setGoodStatus(recallNo, true);
                    setGoodStatus(true);
                    getRelativePresenter().addGoodDtoToTemper(recallNo);
                }
                showGoodDetail(RecallTemper.getRecallDto(recallNo).getGoodList());
                handler.removeCallbacks(customRunnable);
                handler.postDelayed(customRunnable, 1500);
                break;
            case R.id.layoutComment:
                txtComment.requestFocus();
                txtComment.setHint(R.string.send_hint);

                currentNo = RecallTemper.getRecallDto(recallNo).getUserNo();
                if(draftMap.get(currentNo) != null){
                    txtComment.setText(draftMap.get(currentNo).toString());
                }
                Common.showSoftInputFromWindow(this, txtComment);
                break;
            case R.id.layoutSend:

                break;
        }
    }

    class CustomRunnable implements Runnable{
        @Override
        public void run() {
            goodstatus = getRelativePresenter().executeGoodChange(goodstatus,recallNo);
        }
    }

    class CommentTextWatcher implements TextWatcher{
        @Override
        public void afterTextChanged(Editable s) {
            if(s.toString().trim().length() > 0){
                layoutSend.setBackground(ContextCompat.getDrawable(context, R.drawable.layout_send_ready_bg));
            }else{
                layoutSend.setBackground(ContextCompat.getDrawable(context, R.drawable.layout_send_close_bg));
            }
            draftMap.put(RecallTemper.getRecallDto(recallNo).getUserNo(), s.toString());
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }

    @Override
    public void setViewStatus() {
        EventBus.getDefault().register(this);
        showPageHead(null, null, Common.getFormatTime(RecallTemper.getRecallDto(recallNo).getPublishTime()));
        basePresenter = new RecallDetailActivityPresenter(this, context);

        layoutGood.setOnClickListener(this);
        layoutComment.setOnClickListener(this);
        layoutSend.setOnClickListener(this);

        ivComment.setImageDrawable(commentNormalDrawable);
        ivGoodSum.setImageDrawable(thumbUpDrawable);

        txtComment.addTextChangedListener(new CommentTextWatcher());

        txtRecallDetail.setText(RecallTemper.getRecallDto(recallNo).getContent());
        Common.displayDraweeView(RecallTemper.getRecallDto(recallNo).getUserPicPath(), ivNavHead);
        txtAuthor.setText(RecallTemper.getRecallDto(recallNo).getUserName());
        // 设置赞状态
        showGoodDetail(RecallTemper.getRecallDto(recallNo).getGoodList());
        goodstatus = GoodTemper.getGoodStatus(recallNo);
        setGoodStatus(GoodTemper.getGoodStatus(recallNo));

        pictureVoList.addAll(Common.changePictureDtoListToPictureVoList(RecallTemper.getRecallDto(recallNo).getPictureList()));
        imageAdapter = new ImageAdapter(this, pictureVoList);
        customGridView.setAdapter(imageAdapter);
        customGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("PictureVoList", pictureVoList);
                    bundle.putInt("Position", position);
                    parent.getChildAt(position);
                    getSupportFragmentManager().beginTransaction().add(android.R.id.content, PictureFragment.getInstance(bundle), "PictureFragment")
                            .addToBackStack(null).commit();
            }
        });

        commentDtoList.addAll(RecallTemper.getRecallDto(recallNo).getCommentList());
        commentAdapter = new CommentAdapter(this, commentDtoList);
        listViewComment.setAdapter(commentAdapter);
        commentAdapter.notifyDataSetChanged();
    }

    /**
     * 设置点赞按钮的状态 我 是否已赞
     * @param isGood
     */
    private void setGoodStatus(boolean isGood) {
        if (isGood) {
            layoutGood.setBackground(ContextCompat.getDrawable(context, R.drawable.common_rectangle_5c84dc));
            ivGood.setImageDrawable(thumbUpDrawable);
            txtGood.setText("已赞");
            txtGood.setTextColor(ContextCompat.getColor(context, R.color.TC_12B7F5));
        } else {
            layoutGood.setBackground(ContextCompat.getDrawable(context, R.drawable.common_rectangle_bfbfbf));
            ivGood.setImageDrawable(thumbDownDrawable);
            txtGood.setText("赞");
            txtGood.setTextColor(ContextCompat.getColor(context, R.color.TC_BFBFBF));
        }
    }

    /**
     * 显示点赞列表
     *
     * @param goodDtoList
     */
    private void showGoodDetail(List<GoodDto> goodDtoList) {
        if (goodDtoList.size() <= 0) {
            layoutGoodContent.setVisibility(View.GONE);
        } else {
            layoutGoodContent.setVisibility(View.VISIBLE);
        }
        txtGoodDetail.setText(getRelativePresenter().getSpannableString(goodDtoList));
        txtGoodDetail.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void showGooderDetail(Long userNo) {
        showWarning(userNo + "");
    }

    @Override
    public void onPageDestroy() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommentEvent(CommentEvent commentEvent){

    }
}
