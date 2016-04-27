package com.bili.diushoujuaner.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.RecallAdapter;
import com.bili.diushoujuaner.base.BaseFragmentActivity;
import com.bili.diushoujuaner.fragment.PictureFragment;
import com.bili.diushoujuaner.utils.entity.dto.RecallDto;
import com.bili.diushoujuaner.presenter.base.IBaseView;
import com.bili.diushoujuaner.model.eventhelper.PublishRecallEvent;
import com.bili.diushoujuaner.presenter.presenter.SpaceActivityPresenter;
import com.bili.diushoujuaner.presenter.presenter.impl.SpaceActivityPresenterImpl;
import com.bili.diushoujuaner.presenter.publisher.OnPublishListener;
import com.bili.diushoujuaner.presenter.publisher.RecallPublisher;
import com.bili.diushoujuaner.presenter.view.ISpaceView;
import com.bili.diushoujuaner.utils.CommonUtil;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.entity.vo.FriendVo;
import com.bili.diushoujuaner.utils.entity.vo.PictureVo;
import com.bili.diushoujuaner.model.eventhelper.GoodRecallEvent;
import com.bili.diushoujuaner.model.eventhelper.RemoveRecallEvent;
import com.bili.diushoujuaner.widget.BottomMoreListView;
import com.bili.diushoujuaner.widget.TintedBitmapDrawable;
import com.bili.diushoujuaner.widget.badgeview.BGABadgeRelativeLayout;
import com.bili.diushoujuaner.widget.dialog.DialogTool;
import com.bili.diushoujuaner.widget.dialog.OnDialogPositiveClickListener;
import com.bili.diushoujuaner.widget.floatingactionbutton.FloatingActionButton;
import com.bili.diushoujuaner.widget.imagepicker.ImagePicker;
import com.bili.diushoujuaner.utils.entity.vo.ImageItemVo;
import com.bili.diushoujuaner.widget.imagepicker.loader.GlideImageLoader;
import com.bili.diushoujuaner.widget.imagepicker.view.CropImageView;
import com.bili.diushoujuaner.widget.scrollview.OnChangeHeadStatusListener;
import com.bili.diushoujuaner.widget.scrollview.OnScrollRefreshListener;
import com.bili.diushoujuaner.widget.scrollview.ReboundScrollView;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/4/3.
 */
public class SpaceActivity extends BaseFragmentActivity<SpaceActivityPresenter> implements ISpaceView, OnScrollRefreshListener, OnChangeHeadStatusListener, BottomMoreListView.OnLoadMoreListener, View.OnClickListener, OnPublishListener {

    @Bind(R.id.ivWallPaper)
    SimpleDraweeView ivWallPaper;
    @Bind(R.id.userHead)
    SimpleDraweeView userHead;
    @Bind(R.id.layoutTip)
    RelativeLayout layoutTip;
    @Bind(R.id.listviewRecall)
    BottomMoreListView listviewRecall;
    @Bind(R.id.btnFloat)
    FloatingActionButton btnFloat;
    @Bind(R.id.layoutDetail)
    RelativeLayout layoutDetail;
    @Bind(R.id.layoutHead)
    RelativeLayout layoutHead;
    @Bind(R.id.reboundScrollView)
    ReboundScrollView reboundScrollView;
    @Bind(R.id.ivTip)
    ImageView ivTip;
    @Bind(R.id.txtNavTitle)
    TextView txtNavTitle;
    @Bind(R.id.ivUploading)
    ImageView ivUploading;
    @Bind(R.id.layoutProgress)
    BGABadgeRelativeLayout layoutProgress;
    @Bind(R.id.btnRight)
    ImageButton btnRight;


    public static int openCount = 0;
    public static final String TAG = "SpaceActivity";

    private long userNo;
    private List<RecallDto> recallDtoList;
    private RecallAdapter recallAdapter;
    private int index;
    private Handler handler;
    private boolean goodStatus = false;
    private boolean isGoodStatusInited = false;
    private CustomRunnable customRunnable;
    private long goodRecallNo;
    private FriendVo currentFriendVo;
    private ImagePicker imagePicker;
    private AnimationDrawable uploadingAni;
    private boolean isFriended, isOwner;

    class CustomRunnable implements Runnable {
        @Override
        public void run() {
            // 重置，允许再次进行点击，并发送请求
            isGoodStatusInited = false;
            getBindPresenter().executeGoodChange(goodStatus, goodRecallNo);
        }
    }

    @Override
    public void initIntentParam(Intent intent) {
        userNo = intent.getLongExtra(TAG, -1);
    }

    @Override
    public void beforeInitView() {
        basePresenter = new SpaceActivityPresenterImpl(this, context);
        recallDtoList = new ArrayList<>();
        handler = new Handler();
        customRunnable = new CustomRunnable();
        openCount++;
        index = openCount;

        imagePicker = ImagePicker.getInstance();
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_space);
    }

    @Override
    public void resetStatus() {
        FrameLayout.LayoutParams lpHead = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.y100));
        lpHead.setMargins(0, tintManager.getConfig().getStatusBarHeight(), 0, 0);
        layoutHead.setLayoutParams(lpHead);
        RelativeLayout.LayoutParams lpFloat = new RelativeLayout.LayoutParams((int) getResources().getDimension(R.dimen.x128), (int) getResources().getDimension(R.dimen.x128));
        lpFloat.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lpFloat.setMargins(0, tintManager.getConfig().getStatusBarHeight() + (int) getResources().getDimension(R.dimen.x416), (int) getResources().getDimension(R.dimen.x24), 0);
        btnFloat.setLayoutParams(lpFloat);
        btnFloat.setPadding(0, 0, 0, 0);
    }

    @Override
    public void setViewStatus() {
        isFriended = getBindPresenter().isFriend(userNo);
        isOwner = basePresenter.getCurrentUserNo() == userNo;
        if(!(isFriended || isOwner)){
            showPageHead("最近发表", R.mipmap.icon_menu, null);
        }else {
            showPageHead("最近发表", null, null);
        }
        setTintStatusColor(R.color.TRANSPARENT_BLACK);
        layoutHead.setBackground(ContextCompat.getDrawable(context, R.drawable.transparent_black_down_bg));


        RecallPublisher.getInstance(context).register(this);
        uploadingAni = (AnimationDrawable)ivUploading.getDrawable();

        if (userNo == getBindPresenter().getCustomSessionUserNo()) {
            btnFloat.setImageResource(R.mipmap.icon_editor);
        }else{
            btnFloat.setImageResource(R.mipmap.icon_star_solid);
        }

        userHead.setOnClickListener(this);
        ivWallPaper.setOnClickListener(this);
        btnFloat.setOnClickListener(this);
        layoutProgress.setOnClickListener(this);
        btnRight.setOnClickListener(this);

        reboundScrollView.setScrollRefreshListener(this);
        reboundScrollView.setChangeHeadStatusListener(this);
        reboundScrollView.scrollTo(0,0);

        ivTip.setImageDrawable(new TintedBitmapDrawable(getResources(), R.mipmap.icon_nodata, ContextCompat.getColor(context, R.color.COLOR_BFBFBF)));

        recallAdapter = new RecallAdapter(context, recallDtoList, ConstantUtil.RECALL_ADAPTER_SPACE, index);
        listviewRecall.setAdapter(recallAdapter);
        listviewRecall.setCanLoadMore(true);
        listviewRecall.setOnLoadMoreListener(this);
        listviewRecall.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getBindPresenter().addRecallDtoToTemper(recallAdapter.getItem(position));

                Intent intent = new Intent(SpaceActivity.this, RecallDetailActivity.class);
                intent.putExtra(RecallDetailActivity.TAG, recallAdapter.getItem(position).getRecallNo());
                startActivity(intent);
            }
        });

        imagePicker.setImageLoader(new GlideImageLoader());
        imagePicker.setMultiMode(false);
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);
        imagePicker.setFocusWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ConstantUtil.CORP_IMAGE_WALLPAPER_EAGE, getResources().getDisplayMetrics()));
        imagePicker.setFocusHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ConstantUtil.CORP_IMAGE_WALLPAPER_EAGE, getResources().getDisplayMetrics()));
        imagePicker.setOutPutX(ConstantUtil.CORP_IMAGE_OUT_WIDTH);
        imagePicker.setOutPutY(ConstantUtil.CORP_IMAGE_OUT_HEIGHT);

        getBindPresenter().getContactInfo(userNo);
        getBindPresenter().showRecallFromCache(userNo);
        getBindPresenter().getRecallList(userNo);
    }

    @Override
    public void onError() {
        uploadingAni.stop();
        showWarning("趣事发表失败");
    }

    @Override
    public void onProgress(int position, float progress) {
        if(layoutProgress != null){
            layoutProgress.setVisibility(View.VISIBLE);
            layoutProgress.showTextBadge((position + 1) + "");
        }
    }

    @Override
    public void onFinishPublish() {
        if(layoutProgress != null){
            layoutProgress.hiddenBadge();
            layoutProgress.setVisibility(View.GONE);
        }
        uploadingAni.stop();
    }

    @Override
    public void onStartPublish() {
        if(layoutProgress != null){
            layoutProgress.hiddenBadge();
            layoutProgress.setVisibility(View.VISIBLE);
        }
        if(!uploadingAni.isRunning()){
            uploadingAni.start();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivWallPaper:
                if(userNo == getBindPresenter().getCustomSessionUserNo()){
                    Intent intent = new Intent(this, ImageGridActivity.class).putExtra(ImageGridActivity.TAG,"更换壁纸");
                    startActivityForResult(intent, 100);
                }
                break;
            case R.id.userHead:
                if(currentFriendVo != null){
                    ArrayList<PictureVo> pictureVoList = new ArrayList<>();
                    PictureVo pictureVo = new PictureVo();
                    pictureVo.setPicPath(currentFriendVo.getPicPath());
                    pictureVoList.add(pictureVo);
                    PictureFragment.showPictureDetail(getSupportFragmentManager(),pictureVoList,0, false);
                }
                break;
            case R.id.btnFloat:
                if(userNo == getBindPresenter().getCustomSessionUserNo()){
                    startActivity(new Intent(context, RecallAddActivity.class));
                }else{
                    //TODO 执行关注逻辑
                }
                break;
            case R.id.layoutProgress:
                startActivity(new Intent(context, ProgressActivity.class));
                break;
            case R.id.btnRight:
                DialogTool.createFriendAddDialog(context, new OnDialogPositiveClickListener() {
                    @Override
                    public void onPositiveClicked() {
                        startActivity(new Intent(context, ContentEditActivity.class)
                                .putExtra(ContentEditActivity.TAG_TYPE, ConstantUtil.EDIT_CONTENT_FRIEND_APPLY)
                                .putExtra(ContentEditActivity.TAG_CONTENT, "")
                                .putExtra(ContentEditActivity.TAG_CONTNO, userNo));
                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS && data != null && requestCode == 100) {
            ArrayList<ImageItemVo> images = data.getBundleExtra(ImagePicker.EXTRA_IMAGES_BUNDLE).getParcelableArrayList(ImagePicker.EXTRA_RESULT_ITEMS);
            getBindPresenter().updateWallpaper(images.get(0).path);
        }
    }

    @Override
    public void onPageResume() {
        recallAdapter.notifyDataSetChanged();
    }

    @Override
    public void showContactInfo(FriendVo friendVo) {
        currentFriendVo = friendVo;
        CommonUtil.displayDraweeView(friendVo.getWallPaper(), ivWallPaper);
        CommonUtil.displayDraweeView(friendVo.getPicPath(), userHead);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(friendVo.getNickName());
        if(stringBuilder.toString().length() > 4){
            txtNavTitle.setText(stringBuilder.toString().substring(0,4) + "...的发表");
        }else{
            txtNavTitle.setText(stringBuilder.toString().substring(0) + "的发表");
        }

    }

    @Override
    public void showRecallList(List<RecallDto> recallDtoList) {
        recallAdapter.refresh(recallDtoList);
        if (CommonUtil.isEmpty(recallDtoList)) {
            layoutTip.setVisibility(View.VISIBLE);
            listviewRecall.setVisibility(View.GONE);
            return;
        } else {
            layoutTip.setVisibility(View.GONE);
            listviewRecall.setVisibility(View.VISIBLE);
        }
        if (recallDtoList.size() < 20) {
            listviewRecall.setListViewStateComplete();
        } else {
            listviewRecall.setListViewStateFinished();
        }
        reboundScrollView.scrollTo(0,0);
    }

    @Override
    public void onScrollRefresh() {
        listviewRecall.setListViewStateRefresh();
        getBindPresenter().getRecallList(userNo);
    }

    @Override
    public void onLoadMore() {
        getBindPresenter().getMoreRecallList(userNo);
    }

    @Override
    public void setLoadMoreEnd() {
        listviewRecall.setListViewStateFinished();
    }

    @Override
    public void showMoreRecallList(List<RecallDto> recallDtoList) {
        recallAdapter.add(recallDtoList);
        if (CommonUtil.isEmpty(recallDtoList)) {
            listviewRecall.setListViewStateComplete();
            return;
        }
        if (recallDtoList.size() < 20) {
            listviewRecall.setListViewStateComplete();
        } else {
            listviewRecall.setListViewStateFinished();
        }
    }

    @Override
    public void updateWallPaper(String wallPaper) {
        currentFriendVo.setWallPaper(wallPaper);
        CommonUtil.displayDraweeView(wallPaper, ivWallPaper);
    }

    @Override
    public void onHeadStatusChanged(int alpha) {
        if (alpha > 0) {
            layoutHead.setBackgroundColor(Color.parseColor(CommonUtil.getThemeAlphaColor(alpha)));
            setTintStatusColor(R.color.COLOR_THEME_MAIN);
            setTineStatusAlpha((float) alpha / 100);
        } else {
            layoutHead.setBackground(ContextCompat.getDrawable(context, R.drawable.transparent_black_down_bg));
            setTintStatusColor(R.color.TRANSPARENT_BLACK);
            setTineStatusAlpha(1.0f);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRemoveRecallEvent(final RemoveRecallEvent removeRecallEvent) {
        if (removeRecallEvent.getIndex() != index) {
            return;
        }
        DialogTool.createRemoveRecallDialog(context, new OnDialogPositiveClickListener() {
            @Override
            public void onPositiveClicked() {
                getBindPresenter().getRecallRemove(removeRecallEvent.getRecallNo(), removeRecallEvent.getPosition());
            }
        });
    }

    @Override
    public void removeRecallByPosition(int position) {
        recallAdapter.remove(position);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGoodRecallEvent(GoodRecallEvent goodRecallEvent) {
        if (!(goodRecallEvent.getIndex() == index && goodRecallEvent.getType() == ConstantUtil.RECALL_ADAPTER_SPACE)) {
            return;
        }
        if (!isGoodStatusInited) {
            isGoodStatusInited = true;
            goodStatus = getBindPresenter().getGoodStatusByRecallNo(recallAdapter.getItem(goodRecallEvent.getPosition()).getRecallNo());
            goodRecallNo = recallAdapter.getItem(goodRecallEvent.getPosition()).getRecallNo();
        }

        if (getBindPresenter().getGoodStatusByRecallNo(goodRecallNo)) {
            getBindPresenter().setGoodStatusByRecallNo(goodRecallNo, false);
            getBindPresenter().removeGoodFromLocal(recallAdapter.getItem(goodRecallEvent.getPosition()));
        } else {
            getBindPresenter().setGoodStatusByRecallNo(goodRecallNo, true);
            getBindPresenter().addGoodToLocal(recallAdapter.getItem(goodRecallEvent.getPosition()));
        }

        recallAdapter.notifyDataSetChanged();
        handler.removeCallbacks(customRunnable);
        handler.postDelayed(customRunnable, 1500);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPublishRecallEvent(PublishRecallEvent publishRecallEvent){
        if (recallAdapter != null) {
            RecallDto recallDto = publishRecallEvent.getRecallDto().getCloneRecallDto();
            recallAdapter.addFirst(recallDto);
            recallAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPageDestroy() {
        RecallPublisher.getInstance(context).unregister(this);
        super.onPageDestroy();
        openCount--;
    }

    public static void showSpaceActivity(Context context, IBaseView baseView, long userNo) {
        if (SpaceActivity.openCount >= 5) {
            baseView.showWarning("您已经同时打开五个空间页面了，请返回");
        } else {
            context.startActivity(new Intent(context, SpaceActivity.class).putExtra(SpaceActivity.TAG, userNo));
        }
    }
}
