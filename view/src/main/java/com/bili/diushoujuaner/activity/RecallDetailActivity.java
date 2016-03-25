package com.bili.diushoujuaner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.CommentAdapter;
import com.bili.diushoujuaner.adapter.ImageAdapter;
import com.bili.diushoujuaner.base.BaseActivity;
import com.bili.diushoujuaner.base.BaseFragmentActivity;
import com.bili.diushoujuaner.fragment.PictureFragment;
import com.bili.diushoujuaner.model.tempHelper.GoodTemper;
import com.bili.diushoujuaner.presenter.presenter.RecallDetailActivityPresenter;
import com.bili.diushoujuaner.presenter.view.IRecallDetailView;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.Imageloader;
import com.bili.diushoujuaner.utils.entity.PictureVo;
import com.bili.diushoujuaner.utils.response.CommentDto;
import com.bili.diushoujuaner.utils.response.GoodDto;
import com.bili.diushoujuaner.utils.response.PictureDto;
import com.bili.diushoujuaner.utils.response.RecallDto;
import com.bili.diushoujuaner.widget.CustomGridView;
import com.bili.diushoujuaner.widget.CustomListView;
import com.bili.diushoujuaner.widget.aligntextview.CBAlignTextView;
import com.bili.diushoujuaner.widget.picture.ImageInfo;
import com.bili.diushoujuaner.widget.picture.PhotoFrescoView;
import com.bili.diushoujuaner.widget.picture.PhotoView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
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
    @Bind(R.id.textComment)
    EditText textComment;
    @Bind(R.id.customGridView)
    CustomGridView customGridView;
    @Bind(R.id.txtGoodDetail)
    TextView txtGoodDetail;
    @Bind(R.id.layoutGoodContent)
    LinearLayout layoutGoodContent;
    @Bind(R.id.layoutGood)
    LinearLayout layoutGood;
    @Bind(R.id.ivGood)
    ImageView ivGood;
    @Bind(R.id.txtGood)
    TextView txtGood;
    @Bind(R.id.listViewComment)
    CustomListView listViewComment;

    private ArrayList<PictureVo> pictureVoList;
    private RecallDto recallDto;
    private boolean goodstatus = false;
    private Handler handler;
    private CustomRunnable customRunnable;
    private CommentAdapter commentAdapter;
    private List<CommentDto> commentDtoList;
    private ArrayList<ImageInfo> imageInfoArrayList;
    private ImageAdapter imageAdapter;

    @Override
    public void initIntentParam(Intent intent) {
        recallDto = intent.getExtras().getParcelable(Constant.INTENT_RECALL_DETAIL);
    }

    @Override
    public void onPageResume() {
        super.onPageResume();
        tintManager.setStatusBarTintResource(R.color.COLOR_THEME);
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
        imageInfoArrayList = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layoutGood:
                if(GoodTemper.getGoodStatus(recallDto.getRecallNo())){
                    GoodTemper.setGoodStatus(recallDto.getRecallNo(), false);
                    setGoodStatus(false);
                    getRelativePresenter().removeGoodDtoFromTemper(recallDto.getRecallNo());
                }else{
                    GoodTemper.setGoodStatus(recallDto.getRecallNo(), true);
                    setGoodStatus(true);
                    getRelativePresenter().addGoodDtoToTemper(recallDto.getRecallNo());
                }
                showGoodDetail(getRelativePresenter().getGoodDtoListFromTemper(recallDto.getRecallNo()));
                handler.removeCallbacks(customRunnable);
                handler.postDelayed(customRunnable, 1500);
                break;
        }
    }

    class CustomRunnable implements Runnable{
        @Override
        public void run() {
            goodstatus = getRelativePresenter().executeGoodChange(goodstatus, recallDto.getRecallNo());
        }
    }

    @Override
    public void setViewStatus() {
        showPageHead(null, null, Common.getFormatTime(recallDto.getPublishTime()));
        basePresenter = new RecallDetailActivityPresenter(this, context);

        layoutGood.setOnClickListener(this);

        txtRecallDetail.setText(recallDto.getContent());
        Imageloader.getInstance().displayDraweeView(recallDto.getUserPicPath(), ivNavHead);
        txtAuthor.setText(recallDto.getUserName());
        // 设置赞状态
        showGoodDetail(recallDto.getGoodList());
        goodstatus = GoodTemper.getGoodStatus(recallDto.getRecallNo());
        setGoodStatus(GoodTemper.getGoodStatus(recallDto.getRecallNo()));

        pictureVoList.addAll(Common.changePictureDtoListToPictureVoList(recallDto.getPictureList()));
        imageAdapter = new ImageAdapter(this, pictureVoList);
        customGridView.setAdapter(imageAdapter);
        customGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(view.isEnabled()) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("PictureVoList", pictureVoList);
                    bundle.putParcelable("ImageInfo", ((PhotoFrescoView) view).getInfo());
                    bundle.putInt("Position", position);
                    imageInfoArrayList.clear();
                    for (int i = 0; i < pictureVoList.size(); i++) {
                        imageInfoArrayList.add(((PhotoFrescoView) parent.getChildAt(i)).getInfo());
                    }
                    parent.getChildAt(position);
                    bundle.putParcelableArrayList("ImageInfoList", imageInfoArrayList);
                    getSupportFragmentManager().beginTransaction().replace(android.R.id.content, PictureFragment.getInstance(bundle), "PictureFragment")
                            .addToBackStack(null).commit();
                }
            }
        });

        commentDtoList.addAll(recallDto.getCommentList());
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
            ivGood.setImageResource(R.mipmap.icon_good_press);
            txtGood.setText("已赞");
            txtGood.setTextColor(ContextCompat.getColor(context, R.color.TC_5C84DC));
        } else {
            layoutGood.setBackground(ContextCompat.getDrawable(context, R.drawable.common_rectangle_bfbfbf));
            ivGood.setImageResource(R.mipmap.icon_good_normal);
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
}
