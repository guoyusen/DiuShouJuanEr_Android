package com.bili.diushoujuaner.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.ApplyAdapter;
import com.bili.diushoujuaner.base.BaseActivity;
import com.bili.diushoujuaner.callback.OnContactAddListener;
import com.bili.diushoujuaner.presenter.presenter.ContactAddActivityPresenter;
import com.bili.diushoujuaner.presenter.presenter.impl.ContactAddActivityPresenterImpl;
import com.bili.diushoujuaner.presenter.view.IContactAddView;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.entity.vo.ApplyVo;
import com.bili.diushoujuaner.widget.CustomListView;
import com.bili.diushoujuaner.widget.TintedBitmapDrawable;
import com.bili.diushoujuaner.widget.scrollview.ReboundScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/4/3.
 */
public class ContactAddActivity extends BaseActivity<ContactAddActivityPresenter> implements IContactAddView, OnContactAddListener {

    @Bind(R.id.txtRight)
    TextView txtRight;
    @Bind(R.id.listView)
    CustomListView listView;
    @Bind(R.id.ivTip)
    ImageView ivTip;
    @Bind(R.id.layoutTip)
    RelativeLayout layoutTip;
    @Bind(R.id.scrollView)
    ReboundScrollView scrollView;

    private ApplyAdapter applyAdapter;
    private List<ApplyVo> applyVoList;

    @Override
    public void beforeInitView() {
        applyVoList = new ArrayList<>();
        basePresenter = new ContactAddActivityPresenterImpl(this, context);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_contact_add);
    }

    @Override
    public void setViewStatus() {
        showPageHead("新的朋友", null, "添加");

        txtRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ContactAddActivity.this, ContactSearchActivity.class));
            }
        });

        applyAdapter = new ApplyAdapter(context, applyVoList);
        applyAdapter.setContactAddListener(this);
        listView.setAdapter(applyAdapter);
        ivTip.setImageDrawable(new TintedBitmapDrawable(getResources(), R.mipmap.icon_nodata, ContextCompat.getColor(context, R.color.COLOR_BFBFBF)));

        getBindPresenter().getApplyVoList();
    }

    @Override
    public void onContactAdd(int position) {
        ApplyVo applyVo = applyAdapter.getItem(position);
        if(!applyVo.getAccept() && applyVo.getType() == Constant.CHAT_FRIEND_ADD){
            //好友申请
            getBindPresenter().getFriendAgree(applyVo.getToNo(), applyVo.getFromNo());
        }
    }

    @Override
    public void showApplyVoList(List<ApplyVo> applyVoList) {
        if (Common.isEmpty(applyVoList)) {
            layoutTip.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }else{
            layoutTip.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);

            applyAdapter.refresh(applyVoList);
        }
    }
}
