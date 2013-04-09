package com.cse5236groupthirteen.utilities;

public class MessageDetail {
    int icon ;
    String RestaurantName;
    String Comments;
    String WaitingTime;
    String time;

    public String getName() {
        return RestaurantName;
    }

    public void setName(String RestaurantName) {
        this.RestaurantName = RestaurantName;
    }

    public String getComm() {
        return Comments;
    }

    public void setComm(String Comments) {
        this.Comments = Comments;
    }
    
    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
    
    public void setWaitingTime(String WaitingTime){
    	this.WaitingTime = WaitingTime;
    }
    
    public String getWaitingTime(){
    	return WaitingTime;
    }
    
    public void setTime(String time){
    	this.time = time;
    }
    
    public String getTime(){
    	return time;
    }
}