package com.qiyou.dhlive.core.room.service.vo;

import com.qiyou.dhlive.core.room.outward.model.RoomMessageBoard;

import java.util.List;

/**
 * describe:
 *
 * @author fish
 * @date 2018/02/03
 */
public class MessageBoardVO {

    private RoomMessageBoard roomMessageBoard;

    private List<RoomMessageBoard> childs;

    public RoomMessageBoard getRoomMessageBoard() {
        return roomMessageBoard;
    }

    public void setRoomMessageBoard(RoomMessageBoard roomMessageBoard) {
        this.roomMessageBoard = roomMessageBoard;
    }

    public List<RoomMessageBoard> getChilds() {
        return childs;
    }

    public void setChilds(List<RoomMessageBoard> childs) {
        this.childs = childs;
    }
}
