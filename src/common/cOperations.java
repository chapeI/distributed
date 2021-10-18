package common;


/**
* common/cOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from common.idl
* Sunday, October 17, 2021 5:18:15 o'clock PM EDT
*/

public interface cOperations 
{
  boolean createroom (String rno, String date, String timeslot);
  boolean deleteroom (String rno, String date, String timeslot);
  String bookroom (String campusName, String rno, String date, String timeslot, String UID);
  String getAvailableTimeSlot (String date) throws InterruptedException;
  String cancelBooking (String bookingID, String userid);
  String changeReservation (String studentid, String booking_id, String new_date, String new_campus_name, String new_room_no, String new_time_slot);
} // interface cOperations
