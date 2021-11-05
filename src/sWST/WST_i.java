package sWST;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface WST_i {
    boolean createroom(String rno,String date,String timeslot);
    boolean deleteroom(String rno,String date,String timeslot);
    String bookroom(String campusName,String rno,String date,String timeslot,String UID);
    String getAvailableTimeSlot (String date) throws InterruptedException;
    String cancelBooking(String bookingid, String campus);
    String changeReservation(String studentid, String booking_id, String new_date, String new_campus_name, String new_room_no, String new_time_slot);
}
