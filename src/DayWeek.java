import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Данил on 20.02.2016.
 */
public class DayWeek {
    GregorianCalendar newCal = new GregorianCalendar( );
    int day = newCal.get( Calendar.DAY_OF_WEEK );

    public int getDay() {
        return day;
    }

    public String getDate(boolean nextWeek, int DAY){
        String date="";
        int day = newCal.get(Calendar.DAY_OF_MONTH);
        int month = newCal.get(Calendar.MONTH)+1;
        int year = newCal.get(Calendar.YEAR);
        if(nextWeek){
            day++;
        }
        day+=DAY-dayOfWeek();

        if(day> newCal.getActualMaximum(Calendar.DAY_OF_MONTH)){
            month++;
            day=day-newCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        }

        date+=day+"."+month+"."+year;

        return date;
    }

    protected int dayOfWeek(){
        int dayOfWeek= newCal.get( Calendar.DAY_OF_WEEK )-2;
        return dayOfWeek;
    }

    public boolean isMonday(){
        if(newCal.get(Calendar.DAY_OF_WEEK)==2){
            return true;
        } else {
            return false;
        }
    }
    protected void dayString(){

    }

    public boolean isSunday(){
        if(newCal.get(Calendar.DAY_OF_WEEK)==1){
            return true;
        } else {
            return false;
        }
    }

    public boolean isTuesday(){
        if(newCal.get(Calendar.DAY_OF_WEEK)==3){
            return true;
        } else {
            return false;
        }
    }
}