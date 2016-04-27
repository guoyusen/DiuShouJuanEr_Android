package com.bili.diushoujuaner.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.ContactSearchAdapter;
import com.bili.diushoujuaner.base.BaseActivity;
import com.bili.diushoujuaner.presenter.presenter.ContactSearchActivityPresenter;
import com.bili.diushoujuaner.presenter.presenter.impl.ContactSearchActivityPresenterImpl;
import com.bili.diushoujuaner.presenter.view.IContactSearchView;
import com.bili.diushoujuaner.utils.CommonUtil;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.entity.dto.ContactDto;
import com.bili.diushoujuaner.widget.CustomEditText;
import com.bili.diushoujuaner.widget.CustomListView;
import com.bili.diushoujuaner.widget.TintedBitmapDrawable;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/3/7.
 */
public class ContactSearchActivity extends BaseActivity<ContactSearchActivityPresenter> implements IContactSearchView {

    @Bind(R.id.edtParamNo)
    CustomEditText edtParamNo;
    @Bind(R.id.txtSearch)
    TextView txtSearch;
    @Bind(R.id.listViewResult)
    CustomListView listViewResult;
    @Bind(R.id.ivTip)
    ImageView ivTip;
    @Bind(R.id.layoutTip)
    RelativeLayout layoutTip;

    private List<ContactDto> contactDtoList;
    private ContactSearchAdapter contactSearchAdapter;

    @Override
    public void beforeInitView() {
        contactDtoList = new ArrayList<>();
        basePresenter = new ContactSearchActivityPresenterImpl(this, context);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_contact_search);
    }

    @Override
    public void setViewStatus() {
        showPageHead("添加", null, null);

        contactSearchAdapter = new ContactSearchAdapter(context, contactDtoList);
        listViewResult.setAdapter(contactSearchAdapter);

        ivTip.setImageDrawable(new TintedBitmapDrawable(getResources(), R.mipmap.icon_nodata, ContextCompat.getColor(context, R.color.COLOR_BFBFBF)));
        layoutTip.setVisibility(View.GONE);
        listViewResult.setVisibility(View.VISIBLE);

        listViewResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(contactSearchAdapter.getItem(position).getType() == ConstantUtil.CONTACT_FRIEND){
                    Intent intent = new Intent(context, ContactDetailActivity.class);
                    intent.putExtra(ContactDetailActivity.TAG, contactSearchAdapter.getItem(position).getContNo());
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(context, PartyDetailActivity.class);
                    intent.putExtra(PartyDetailActivity.TAG_DTO, contactSearchAdapter.getItem(position));
                    intent.putExtra(PartyDetailActivity.TAG_TYPE, PartyDetailActivity.TYPE_SEARCH);
                    startActivity(intent);
                }
            }
        });

        txtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(txtSearch.getText())){
                    getBindPresenter().getContactsSearch(edtParamNo.getText().toString().trim());
                }
            }
        });
    }

    @Override
    public void showSearchContactsResult(List<ContactDto> contactDtoList) {
        if(CommonUtil.isEmpty(contactDtoList)){
            listViewResult.setVisibility(View.GONE);
            layoutTip.setVisibility(View.VISIBLE);
        }else{
            listViewResult.setVisibility(View.VISIBLE);
            layoutTip.setVisibility(View.GONE);
            contactSearchAdapter.refresh(contactDtoList);
            contactSearchAdapter.notifyDataSetChanged();
        }
    }
}
