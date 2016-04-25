package com.bili.diushoujuaner.activity;

import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.base.BaseActivity;
import com.bili.diushoujuaner.model.eventhelper.DeleteContactEvent;
import com.bili.diushoujuaner.presenter.presenter.ChattingSettingActivityPresenter;
import com.bili.diushoujuaner.presenter.presenter.impl.ChattingSettingActivityPresenterImpl;
import com.bili.diushoujuaner.presenter.view.IChattingSettingView;
import com.bili.diushoujuaner.utils.CommonUtil;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.entity.vo.FriendVo;
import com.bili.diushoujuaner.widget.TintedBitmapDrawable;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/4/3.
 */
public class ChattingSettingActivity extends BaseActivity<ChattingSettingActivityPresenter> implements IChattingSettingView {

    @Bind(R.id.ivHead)
    SimpleDraweeView ivHead;
    @Bind(R.id.txtName)
    TextView txtName;
    @Bind(R.id.txtContent)
    TextView txtContent;
    @Bind(R.id.ivUser)
    ImageView ivUser;
    @Bind(R.id.ivRecord)
    ImageView ivRecord;
    @Bind(R.id.layoutRecord)
    RelativeLayout layoutRecord;
    @Bind(R.id.ivFile)
    ImageView ivFile;
    @Bind(R.id.layoutFile)
    RelativeLayout layoutFile;
    @Bind(R.id.btnDelete)
    Button btnDelete;

    private TintedBitmapDrawable arrowRightDrawable;
    private FriendVo friendVo;

    @Override
    public void beforeInitView() {
        arrowRightDrawable = new TintedBitmapDrawable(getResources(), R.mipmap.icon_arrow_right, ContextCompat.getColor(context, R.color.COLOR_8A8A8A));
        basePresenter = new ChattingSettingActivityPresenterImpl(this, context);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_chatting_setting);
    }

    @Override
    public void setViewStatus() {
        showPageHead("聊天设置", null, null);
        ivRecord.setImageDrawable(arrowRightDrawable);
        ivFile.setImageDrawable(arrowRightDrawable);
        getBindPresenter().getContactInfo();
    }

    @Override
    public void showContactInfo(FriendVo friendVo) {
        if(friendVo == null){
            return;
        }
        this.friendVo = friendVo;
        CommonUtil.displayDraweeView(friendVo.getPicPath(), ivHead);
        txtName.setText(friendVo.getDisplayName());
        txtContent.setText(friendVo.getAutograph());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeleteContactEvent(DeleteContactEvent deleteContactEvent){
        if(deleteContactEvent.getType() == ConstantUtil.DELETE_CONTACT_FRIEND
                && deleteContactEvent.getContNo() == this.friendVo.getFriendNo()){
            finish();
        }
    }

}
