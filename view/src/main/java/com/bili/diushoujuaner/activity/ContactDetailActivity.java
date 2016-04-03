package com.bili.diushoujuaner.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.ContactRecentGalleryAdapter;
import com.bili.diushoujuaner.base.BaseActivity;
import com.bili.diushoujuaner.utils.entity.FriendVo;
import com.bili.diushoujuaner.model.apihelper.response.PictureDto;
import com.bili.diushoujuaner.widget.CustomGridView;
import com.bili.diushoujuaner.widget.CustomRelativeLayout;
import com.bili.diushoujuaner.widget.TintedBitmapDrawable;
import com.bili.diushoujuaner.widget.aligntextview.AlignTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/3/9.
 */
public class ContactDetailActivity extends BaseActivity {

    @Bind(R.id.customGridView)
    CustomGridView customGridView;
    @Bind(R.id.layoutHead)
    RelativeLayout layoutHead;
    @Bind(R.id.layoutParent)
    CustomRelativeLayout layoutParent;
    @Bind(R.id.txtFriendName)
    TextView txtFriendName;
    @Bind(R.id.btnFocus)
    Button btnFocus;
    @Bind(R.id.txtFriendAutograph)
    AlignTextView txtFriendAutograph;
    @Bind(R.id.btnStartMsg)
    Button btnStartMsg;
    @Bind(R.id.ivArrowRight)
    ImageView ivArrowRight;

    private ContactRecentGalleryAdapter contactRecentGalleryAdapter;
    private List<PictureDto> pictureList;
    private FriendVo friendVo;
    public static final String TAG = "ContactDetailActivity";

    @Override
    public void initIntentParam(Intent intent) {
        friendVo = intent.getExtras().getParcelable(TAG);
    }

    @Override
    public void tintStatusColor() {
        super.tintStatusColor();
        tintManager.setStatusBarTintResource(R.color.TRANSPARENT_HALF);
    }

    @Override
    public void beforeInitView() {
        pictureList = new ArrayList<>();
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_contact_detail);
    }

    @Override
    public void setViewStatus() {
        showPageHead(null, null, "更多");

        ivArrowRight.setImageDrawable(new TintedBitmapDrawable(getResources(),R.mipmap.icon_arrow_right,ContextCompat.getColor(context,R.color.COLOR_8A8A8A)));
        layoutHead.setBackground(ContextCompat.getDrawable(context, R.drawable.transparent_black_down_bg));
        layoutParent.setBgUrl(friendVo.getPicPath());
        txtFriendName.setText(friendVo.getDisplayName());
        txtFriendAutograph.setText(friendVo.getAutograph());

        for (int i = 0; i < 3; i++) {
            pictureList.add(new PictureDto());
        }
        contactRecentGalleryAdapter = new ContactRecentGalleryAdapter(this, pictureList);
        customGridView.setAdapter(contactRecentGalleryAdapter);
    }

}
