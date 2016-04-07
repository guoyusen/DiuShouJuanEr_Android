package com.bili.diushoujuaner.dao;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class CustomGenerator {

    public static void main(String[] args) throws Exception{
        Schema schema = new Schema(1, "com.bili.diushoujuaner.model.databasehelper.dao");
        addUser(schema);
        addFriend(schema);
        addParty(schema);
        addMember(schema);
        addChat(schema);

        new DaoGenerator().generateAll(schema, "../diushoujuaner/model/src/main/java");
    }

    /**
     *  User Table
     */
    private static void addUser(Schema schema){
        Entity user = schema.addEntity("User");
        user.addIdProperty().autoincrement().primaryKey();
        user.addLongProperty("userNo").notNull();
        user.addStringProperty("nickName");
        user.addStringProperty("mobile");
        user.addStringProperty("email");
        user.addStringProperty("autograph");
        user.addIntProperty("gender");
        user.addStringProperty("birthday");
        user.addStringProperty("homeTown");
        user.addStringProperty("location");
        user.addStringProperty("picPath");
        user.addStringProperty("smallNick");
        user.addStringProperty("registTime");
        user.addStringProperty("wallPaper");
        user.addStringProperty("updateTime");
    }

    /**
     *   Friend Table
     */
    private static void addFriend(Schema schema){
        Entity friend = schema.addEntity("Friend");
        friend.addIdProperty().autoincrement().primaryKey();
        friend.addLongProperty("ownerNo").notNull().index();
        friend.addLongProperty("friendNo").notNull();
        friend.addStringProperty("remark");
    }

    /**
     *  Party Table
     */
    private static void addParty(Schema schema){
        Entity party = schema.addEntity("Party");
        party.addIdProperty().autoincrement().primaryKey();
        party.addLongProperty("partyNo").notNull();
        party.addStringProperty("partyName").notNull();
        party.addLongProperty("ownerNo").notNull();
        party.addStringProperty("information");
        party.addStringProperty("registerTime").notNull();
        party.addStringProperty("picPath").notNull();

    }

    private static void addMember(Schema schema){
        Entity member = schema.addEntity("Member");
        member.addIdProperty().autoincrement().primaryKey();
        member.addLongProperty("partyNo").notNull().index();
        member.addLongProperty("userNo").notNull();
        member.addStringProperty("memberName").notNull();
        member.addStringProperty("addTime").notNull();
        member.addIntProperty("type").notNull();
    }

    private static void addChat(Schema schema){
        Entity chat = schema.addEntity("Chat");
        chat.addIdProperty().autoincrement().primaryKey();
        chat.addLongProperty("ownerNo").notNull().index();
        chat.addLongProperty("fromNo").notNull();
        chat.addLongProperty("toNo").notNull();
        chat.addStringProperty("content");
        chat.addStringProperty("time").notNull();
        chat.addIntProperty("msgType").notNull();
        chat.addIntProperty("conType").notNull();
    }

}
