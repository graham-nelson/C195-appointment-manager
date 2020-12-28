package utils;

import java.time.*;
import java.util.TimeZone;

/**
 * Method used for creating the company business hours of
 * 8:00AM - 10:00PM EST.
 */
public class TimeUtils {

    static LocalDate date = LocalDate.now();
    static ZoneId estId = ZoneId.of("America/New_York");
    static ZoneId localZone = ZoneId.of(TimeZone.getDefault().getID());

    /**
     * @return LocalDateTime object representing business open at 8:00AM EST
     */
    public static LocalDateTime getLocalStartTime(){
        LocalTime start = LocalTime.of( 8, 0 );
        ZonedDateTime businessStartEst = ZonedDateTime.of(date, start, estId);
        ZonedDateTime businessStartLocal = businessStartEst.withZoneSameInstant(localZone);

        return businessStartLocal.toLocalDateTime();
    }

    /**
     * @return LocalDateTime object representing business close at 10:00PM EST
     */
    public static LocalDateTime getLocalEndTime(){
        //making end time one hour less so the last appointment that can be scheduled is 7PMEST for 1 hour which would end at 8PMEST which is the close of business hours
        LocalTime end = LocalTime.of(21, 0 );
        ZonedDateTime businessEndEst = ZonedDateTime.of(date, end, estId);
        ZonedDateTime businessEndLocal = businessEndEst.withZoneSameInstant(localZone);

        return businessEndLocal.toLocalDateTime();
    }


}
