package com.bili.diushoujuaner.utils;

import com.bili.diushoujuaner.utils.entity.dto.ContactDto;
import com.bili.diushoujuaner.utils.entity.dto.MemberDto;
import com.bili.diushoujuaner.utils.entity.dto.MessageDto;
import com.bili.diushoujuaner.utils.entity.dto.OffMsgDto;
import com.bili.diushoujuaner.utils.entity.dto.UserDto;
import com.bili.diushoujuaner.utils.entity.po.Friend;
import com.bili.diushoujuaner.utils.entity.po.Member;
import com.bili.diushoujuaner.utils.entity.po.Party;
import com.bili.diushoujuaner.utils.entity.po.User;
import com.bili.diushoujuaner.utils.entity.vo.MessageVo;
import com.google.gson.reflect.TypeToken;

/**
 * Created by BiLi on 2016/4/25.
 */
public class EntityUtil {


    public static MessageVo getMessageVoFromOffMsgDto(OffMsgDto offMsgDto){
        MessageVo messageVo = new MessageVo();
        messageVo.setContent(offMsgDto.getContent());
        messageVo.setTime(offMsgDto.getTime());
        messageVo.setConType(offMsgDto.getConType());
        messageVo.setMsgType(offMsgDto.getMsgType());
        messageVo.setStatus(ConstantUtil.MESSAGE_STATUS_SUCCESS);
        messageVo.setFromNo(offMsgDto.getFromNo());
        messageVo.setToNo(offMsgDto.getToNo());
        return messageVo;
    }

    public static MessageVo getMessageVoForNoticeNewMember(long partyNo, long memberNo, String time){
        MessageVo messageVo = new MessageVo();
        messageVo.setContent("我加入了群聊");
        messageVo.setTime(time);
        messageVo.setConType(ConstantUtil.CONTENT_PARTY_ADD);
        messageVo.setMsgType(ConstantUtil.CHAT_PAR);
        messageVo.setStatus(ConstantUtil.MESSAGE_STATUS_SUCCESS);
        messageVo.setFromNo(memberNo);
        messageVo.setToNo(partyNo);
        return messageVo;
    }

    public static MessageVo getMessageVoFromReceive(String jsonString){
        MessageDto messageDto = getMessageDtoFromJSONString(jsonString);
        MessageVo messageVo = null;
        if(messageDto != null){
            messageVo = new MessageVo();
            messageVo.setSerialNo(messageDto.getSerialNo());
            messageVo.setMsgType(messageDto.getMsgType());
            messageVo.setStatus(ConstantUtil.MESSAGE_STATUS_SUCCESS);
            messageVo.setToNo(messageDto.getReceiverNo());
            messageVo.setFromNo(messageDto.getSenderNo());
            messageVo.setTime(messageDto.getMsgTime());
            messageVo.setConType(messageDto.getConType());
            messageVo.setContent(messageDto.getMsgContent());
        }
        return messageVo;
    }

    /**
     * 发送成功之后，会回调messageSent方法，解析出对应的MessageVo，发送event来 通知更新状态
     * @return
     */
    public static MessageVo getMessageVoFromMessageDto(MessageDto messageDto){
        MessageVo messageVo = null;
        try{
            if(messageDto != null){
                messageVo = new MessageVo();
                messageVo.setSerialNo(messageDto.getSerialNo());
                messageVo.setMsgType(messageDto.getMsgType());
                messageVo.setStatus(ConstantUtil.MESSAGE_STATUS_SUCCESS);
                messageVo.setToNo(messageDto.getReceiverNo());
                messageVo.setFromNo(messageDto.getSenderNo());
                messageVo.setTime(messageDto.getMsgTime());
                messageVo.setConType(messageDto.getConType());
                messageVo.setContent(messageDto.getMsgContent());
            }
        }catch(Exception e){
            return null;
        }
        return messageVo;
    }

    /**
     * 将收到的数据转化成MessageDto
     * @param jsonString
     * @return
     */
    public static MessageDto getMessageDtoFromJSONString(String jsonString) {
        MessageDto messageDto;
        try{
            messageDto = GsonUtil.getInstance().fromJson(jsonString, new TypeToken<MessageDto>(){}.getType());
        }catch(Exception e){
            return null;
        }
        return messageDto;
    }

    /**
     * 将要准备发送的messageVo转化成MessageDto
     */
    public static MessageDto getMessageDtoFromMessageVo(MessageVo messageVo){
        MessageDto messageDto = new MessageDto();
        messageDto.setSerialNo(messageVo.getSerialNo());
        messageDto.setConType(messageVo.getConType());
        messageDto.setMsgContent(messageVo.getContent());
        messageDto.setMsgType(messageVo.getMsgType());
        messageDto.setMsgTime(messageVo.getTime());
        messageDto.setReceiverNo(messageVo.getToNo());
        messageDto.setSenderNo(messageVo.getFromNo());

        return messageDto;
    }

    /**
     * 得到空的消息包
     * @return
     */
    public static String getEmptyMessage(String serialNo, long senderNo, int chatType){
        MessageDto messageDto = new MessageDto();
        messageDto.setSerialNo(serialNo);
        messageDto.setMsgContent("");
        messageDto.setMsgTime("");
        messageDto.setMsgType(chatType);
        messageDto.setConType(ConstantUtil.CHAT_CONTENT_TEXT);
        messageDto.setReceiverNo(0);
        messageDto.setSenderNo(senderNo);

        return GsonUtil.getInstance().toJson(messageDto);
    }

    public static MessageVo getEmptyMessageVo(long senderNo, int chatType){
        MessageVo messageVo = new MessageVo();
        messageVo.setSerialNo(StringUtil.getSerialNo());
        messageVo.setMsgType(chatType);
        messageVo.setFromNo(senderNo);
        messageVo.setStatus(ConstantUtil.MESSAGE_STATUS_SENDING);

        return messageVo;
    }

    public static User getUserFromContactDto(ContactDto contactDto){
        User user = new User();
        user.setGender(contactDto.getGender());
        user.setPicPath(contactDto.getPicPath());
        user.setUserNo(contactDto.getContNo());
        user.setSmallNick(contactDto.getSmallNick());
        user.setAutograph(contactDto.getAutograph());
        user.setNickName(contactDto.getNickName());
        user.setHomeTown(contactDto.getHomeTown());
        user.setWallPaper(contactDto.getWallPaper());

        return user;
    }

    public static Party getPartyFromContactDto(ContactDto contactDto){
        Party party = new Party();
        party.setInformation(contactDto.getInformation());
        party.setOwnerNo(contactDto.getOwnerNo());
        party.setPartyName(contactDto.getDisplayName());
        party.setPartyNo(contactDto.getContNo());
        party.setPicPath(contactDto.getPicPath());
        party.setRegisterTime(contactDto.getStartTime());

        return party;
    }

    public static Friend getFriendFromContactDto(ContactDto contactDto, long ownerNo){
        Friend friend = new Friend();
        friend.setFriendNo(contactDto.getContNo());
        friend.setOwnerNo(ownerNo);
        friend.setRemark(contactDto.getDisplayName());

        return friend;
    }

    public static Member getMemberFromMemberDto(MemberDto memberDto){
        Member member = new Member();
        member.setPartyNo(memberDto.getPartyNo());
        member.setUserNo(memberDto.getUserNo());
        member.setAddTime(memberDto.getAddTime());
        member.setMemberName(memberDto.getMemberName());
        member.setType(memberDto.getType());

        return member;
    }

    public static User getUserFromMemberDto(MemberDto memberDto){
        User user = new User();
        user.setUserNo(memberDto.getUserNo());
        user.setPicPath(memberDto.getPicPath());

        return user;
    }

    public static User getUserFromUserDto(UserDto userDto){
        User user = new User();
        user.setMobile(userDto.getMobile());
        user.setAutograph(userDto.getAutograph());
        user.setBirthday(userDto.getBirthday());
        user.setHomeTown(userDto.getHomeTown());
        user.setLocation(userDto.getLocation());
        user.setNickName(userDto.getNickName());
        user.setGender(userDto.getGender());
        user.setPicPath(userDto.getPicPath());
        user.setSmallNick(userDto.getSmallNick());
        user.setUserNo(userDto.getUserNo());
        user.setRegistTime(userDto.getRegistTime());
        user.setWallPaper(userDto.getWallPaper());
        user.setEmail(userDto.getEmail());

        return user;
    }

    public static User updateUserBySelective(User older, User newer){
        older.setMobile(StringUtil.isEmpty(newer.getMobile()) ? older.getMobile() : newer.getMobile());
        older.setAutograph(StringUtil.isEmpty(newer.getAutograph()) ? older.getAutograph() : newer.getAutograph());
        older.setBirthday(StringUtil.isEmpty(newer.getBirthday()) ? older.getBirthday() : newer.getBirthday());
        older.setHomeTown(StringUtil.isEmpty(newer.getHomeTown()) ? older.getHomeTown() : newer.getHomeTown());
        older.setLocation(StringUtil.isEmpty(newer.getLocation()) ? older.getLocation() : newer.getLocation());
        older.setNickName(StringUtil.isEmpty(newer.getNickName()) ? older.getNickName() : newer.getNickName());
        older.setPicPath(StringUtil.isEmpty(newer.getPicPath()) ? older.getPicPath() : newer.getPicPath());
        older.setSmallNick(StringUtil.isEmpty(newer.getSmallNick()) ? older.getSmallNick() : newer.getSmallNick());
        older.setRegistTime(StringUtil.isEmpty(newer.getRegistTime()) ? older.getRegistTime() : newer.getRegistTime());
        older.setGender(newer.getGender() == null ? older.getGender() : newer.getGender());
        older.setUpdateTime(StringUtil.isEmpty(newer.getUpdateTime()) ? older.getUpdateTime() : newer.getUpdateTime());
        older.setWallPaper(StringUtil.isEmpty(newer.getWallPaper()) ? older.getWallPaper() : newer.getWallPaper());
        older.setEmail(StringUtil.isEmpty(newer.getEmail()) ? older.getEmail() : newer.getEmail());

        return older;
    }

    public static Friend updateFriendByNewer(Friend older, Friend newer){
        older.setRemark(StringUtil.isEmpty(newer.getRemark()) ? older.getRemark() : newer.getRemark());
        return older;
    }

    public static Member updateMemberByNewer(Member older, Member newer){
        older.setMemberName(StringUtil.isEmpty(newer.getMemberName()) ? older.getMemberName() : newer.getMemberName());
        older.setType(newer.getType());

        return older;
    }
}
