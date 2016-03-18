package com.bili.diushoujuaner.presenter.presenter;

import android.content.Context;

import com.bili.diushoujuaner.model.action.ContactsAction;
import com.bili.diushoujuaner.model.apihelper.ApiRespon;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;
import com.bili.diushoujuaner.model.databasehelper.DBManager;
import com.bili.diushoujuaner.model.databasehelper.dao.Friend;
import com.bili.diushoujuaner.model.databasehelper.dao.Party;
import com.bili.diushoujuaner.model.databasehelper.dao.User;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.base.IBaseView;
import com.bili.diushoujuaner.presenter.viewinterface.ContactFragmentView;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.entity.FriendVo;
import com.bili.diushoujuaner.utils.pinyin.PinyinComparator;
import com.bili.diushoujuaner.utils.response.ContactDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by BiLi on 2016/3/15.
 */
public class ContactFragmentPresenter extends BasePresenter {

    public ContactFragmentPresenter(IBaseView baseView, Context context) {
        super(baseView, context);
    }

    public void getContacts(){
        showContacts();
        if(Common.checkNetworkStatus(context)){
            getViewByClass(ContactFragmentView.class).showLoading(Constant.LOADING_DEFAULT);
            ContactsAction.getInstance(context).getContacts(new ActionCallbackListener<ApiRespon<List<ContactDto>>>() {
                @Override
                public void onSuccess(ApiRespon<List<ContactDto>> result) {
                    if(showMessage(result.getRetCode(), result.getMessage())){
                        saveContactFromList(result.getData());
                    }
                    showContacts();
                    getViewByClass(ContactFragmentView.class).hideLoading(Constant.LOADING_DEFAULT);
                }

                @Override
                public void onFailure(int errorCode) {
                    showError(errorCode);
                    getViewByClass(ContactFragmentView.class).hideLoading(Constant.LOADING_DEFAULT);
                }
            });
        }
    }

    private void showContacts(){
        List<FriendVo> friendVoList = getFriendFromDb();
        Collections.sort(friendVoList, new PinyinComparator());
        getViewByClass(ContactFragmentView.class).showContactList(friendVoList);
    }

    private List<FriendVo> getFriendFromDb(){
        return DBManager.getInstance().getFriendVo();
    }

    private void saveContactFromList(List<ContactDto> contactDtoList){
        List<Friend> friendList = new ArrayList<>();
        List<Party> partyList = new ArrayList<>();
        List<User> userList = new ArrayList<>();

        for(ContactDto contactDto : contactDtoList){
            if(contactDto.getType() == Constant.CONTACT_FRIEND){
                friendList.add(getFriendFromContactDto(contactDto));
                userList.add(getUserFromContactDto(contactDto));
            }else if(contactDto.getType() == Constant.CONTACT_PARTY){
                partyList.add(getPartyFromContactDto(contactDto));
            }
        }
        DBManager.getInstance().saveUserList(userList);
        DBManager.getInstance().saveFriendList(friendList);
        DBManager.getInstance().savePartyList(partyList);
    }

    private User getUserFromContactDto(ContactDto contactDto){
        User user = new User();
        user.setGender(contactDto.getGender());
        user.setPicPath(contactDto.getPicPath());
        user.setUserNo(contactDto.getContNo());
        user.setSmallNick(contactDto.getSmallNick());
        user.setAutograph(contactDto.getAutograph());
        user.setNickName(contactDto.getNickName());
        user.setHomeTown(contactDto.getHomeTown());

        return user;
    }

    private Party getPartyFromContactDto(ContactDto contactDto){
        Party party = new Party();
        party.setInformation(contactDto.getInformation());
        party.setOwnerNo(contactDto.getOwnerNo());
        party.setPartyName(contactDto.getDisplayName());
        party.setPartyNo(contactDto.getContNo());
        party.setPicPath(contactDto.getPicPath());
        party.setRegisterTime(contactDto.getStartTime());

        return party;
    }

    private Friend getFriendFromContactDto(ContactDto contactDto){
        Friend friend = new Friend();
        friend.setFriendNo(contactDto.getContNo());
        friend.setOwnerNo(CustomSessionPreference.getInstance().getCustomSession().getUserNo());
        friend.setRemark(contactDto.getDisplayName());

        return friend;
    }

}
