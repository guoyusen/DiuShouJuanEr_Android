package com.bili.diushoujuaner.activity;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.base.BaseActivity;
import com.bili.diushoujuaner.presenter.view.IContactSearchView;
import com.bili.diushoujuaner.utils.entity.dto.ContactDto;
import com.bili.diushoujuaner.widget.CustomEditText;
import com.bili.diushoujuaner.widget.CustomListView;
import com.bili.diushoujuaner.widget.TintedBitmapDrawable;

import java.util.List;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/3/7.
 */
public class ContactSearchActivity extends BaseActivity implements IContactSearchView {

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

    @Override
    public void initView() {
        setContentView(R.layout.activity_contact_add);
    }

    @Override
    public void setViewStatus() {
        showPageHead("添加", null, null);

        ivTip.setImageDrawable(new TintedBitmapDrawable(getResources(), R.mipmap.icon_nodata, ContextCompat.getColor(context, R.color.COLOR_BFBFBF)));
        layoutTip.setVisibility(View.GONE);
        listViewResult.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSearchContactsResult(List<ContactDto> contactDtoList) {

    }
}
