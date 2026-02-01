package com.erp.student_erp.userDTO;

public class TimetableResponse {

    private String course;
    private String day;
    private String startTime;
    private String endTime;
    private String room;

    public TimetableResponse(String course, String day,
                             String startTime, String endTime, String room) {
        this.course = course;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
    }

    public String getCourse() { return course; }
    public String getDay() { return day; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public String getRoom() { return room; }
}
