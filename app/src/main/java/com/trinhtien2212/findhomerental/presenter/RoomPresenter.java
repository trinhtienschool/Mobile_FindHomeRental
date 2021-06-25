package com.trinhtien2212.findhomerental.presenter;

import android.util.Log;

import com.trinhtien2212.findhomerental.dao.ConnectServer;
import com.trinhtien2212.findhomerental.dao.RoomDB;
import com.trinhtien2212.findhomerental.dao.SaveLocationBehavior;
import com.trinhtien2212.findhomerental.model.Location;
import com.trinhtien2212.findhomerental.model.Room;
import com.trinhtien2212.findhomerental.ui.add_room.AddRoomActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RoomPresenter implements StatusResult, RoomsResult {
    ConnectServer connectServer;
    RoomDB roomDB;
    Room room;
    RoomsResult roomsResult;
    StatusResult statusResult;
    private int startPrice=1000000, endPrice=1200000;
    private int rangePrice = 100000;
    private boolean isLoading = false;
    private Date startDate,endDate;

    public RoomPresenter(RoomsResult roomsResult) {
//        this.addRoomActivity = addRoomActivity;
        this.roomsResult = roomsResult;
        this.connectServer = new SaveLocationBehavior(this);
        this.roomDB = RoomDB.getInstance();
    }
    public RoomPresenter(StatusResult statusResult) {
//        this.addRoomActivity = addRoomActivity;
        this.statusResult = statusResult;

        this.connectServer = new SaveLocationBehavior(this);
        this.roomDB = RoomDB.getInstance();
    }
    public RoomPresenter(RoomsResult roomsResult,StatusResult statusResult) {
//        this.addRoomActivity = addRoomActivity;
        this.statusResult = statusResult;
        this.roomsResult = roomsResult;
        this.connectServer = new SaveLocationBehavior(this);
        this.roomDB = RoomDB.getInstance();
    }
    public RoomPresenter(StatusResult statusResult,Room room) {
//        this.addRoomActivity = addRoomActivity;
        this.roomsResult = roomsResult;
        this.statusResult = statusResult;
        this.room = room;
        this.connectServer = new SaveLocationBehavior(this);
        this.roomDB = RoomDB.getInstance();
    }

    public void getAllRoomsOfUser(String uid){
        roomDB.getAllRoomOfUser(uid,this);
    }
    public void getRandomRooms(){
        roomDB.getRandomRooms(this);
    }
    public void setRoom(Room room){
        this.room = room;
    }
    public void saveRoom() {
        this.roomDB.addRoom(room, this);
    }

    public void saveLocation(String roomID) {
        this.room.setRoomID(roomID);
        Log.e("ROOMID", roomID);
        Location location = new Location(this.room.getAddress(), false, roomID);
        connectServer.connectServer(location, ConnectServer.ADDROOM);

    }

    public void saveImage() {
//          room.setImages(imagesStorageUri);
        roomDB.uploadImage(room, this);
    }

    public void updateRoom() {

        this.roomDB.updateRoom(room, this);
    }

    public void updateLocation() {
        Location location = new Location(this.room.getAddress(), false, this.room.getRoomID());
        connectServer.connectServer(location, ConnectServer.UPDATEROOM);
    }

    public void deleteRoom() {

        //phai set isDeleted = true
        this.roomDB.deleteRoom(this.room.getRoomID(), this);
    }

    public void deleteLocation() {
        Location location = new Location(this.room.getAddress(), true, this.room.getRoomID());
        connectServer.connectServer(location, ConnectServer.DELETEROOM);
    }

    public RoomsResult getRoomsResult() {
        return roomsResult;
    }

    public void setRoomsResult(RoomsResult roomsResult) {
        this.roomsResult = roomsResult;
    }

    public StatusResult getStatusResult() {
        return statusResult;
    }

    public void setStatusResult(StatusResult statusResult) {
        this.statusResult = statusResult;
    }

    public void sortRoom(boolean isASC){
        endPrice = startPrice+rangePrice;
        roomDB.getRoomForSort(this,isASC,startPrice,endPrice);
        startPrice = endPrice;
    }
    public void filterRoom(int month_start,int month_end){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-month_end);
        Date start = calendar.getTime();
        calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-month_start);
        Date end = calendar.getTime();
        Log.e("dateStart",start.toString());
        Log.e("dateEnd",end.toString());
        roomDB.filterByDateCreated(this,start,end);

    }
    @Override
    public void returnRooms(List<Room> rooms) {
        for(Room room: rooms){
            Log.e("Room",room.toString());
        }
//        roomsResult.returnRooms(rooms);

    }


    @Override
    public void onFail() {
        statusResult.onFail();
    }

    @Override
    public void onSuccess() {
        Log.e("Thanh cong","Xóa thành công");
        statusResult.onSuccess();
    }
}
