package tulsi.periodpal.Helper;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.core.content.ContextCompat;

import com.applandeo.materialcalendarview.EventDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import tulsi.periodpal.BuildConfig;
import tulsi.periodpal.R;


public class CalculateUtil {


//    DEFAULT CALENDAR INSTANCE
    private static Calendar calendarCycle;


//    CALENDAR
    public static List<Calendar> periodCalendarList = new ArrayList<>();
    public static List<Calendar> futurePeriodCalendarList = new ArrayList<>();
    public static List<Calendar> fertileCalendarList = new ArrayList<>();
    public static List<Calendar> ovulationCalendarList = new ArrayList<>();
    public static List<Calendar> highCalendarList = new ArrayList<>();
    public static List<Calendar> mediumCalendarList = new ArrayList<>();
    public static List<Calendar> lowCalendarList = new ArrayList<>();


//    EVENT-LIST
    public static List<EventDay> eventList = new ArrayList<>();


//    SHARED-PREFERENCES
    private static SharedPreferences sharedPref;
    public static int cycle, bleeding_D, lastPeriod_Y, lastPeriod_M, lastPeriod_D;





    public static void getInstance(Context context) {

//        SHARED-PREFERENCES
        sharedPref = context.getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE);
        cycle = sharedPref.getInt("cycle", -1);
        bleeding_D = sharedPref.getInt("bleeding_D", -1);
        lastPeriod_Y = sharedPref.getInt("lastPeriod_Y", -1);
        lastPeriod_M = sharedPref.getInt("lastPeriod_M", -1);
        lastPeriod_D = sharedPref.getInt("lastPeriod_D", -1);
        calendarCycle = Calendar.getInstance();

    }


    public static void clearAll() {

        eventList.clear();
        periodCalendarList.clear();
        futurePeriodCalendarList.clear();
        fertileCalendarList.clear();
        ovulationCalendarList.clear();
        highCalendarList.clear();
        mediumCalendarList.clear();
        lowCalendarList.clear();

    }


    public static boolean isEmpty() {
        return (cycle == -1 || bleeding_D == -1 || lastPeriod_Y == -1 || lastPeriod_M == -1 || lastPeriod_D == -1);
    }


    public static void setPreferences(int cycle, int bleeding_D, int lastPeriod_D, int lastPeriod_M, int lastPeriod_Y) {

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt("cycle", cycle);
        editor.putInt("bleeding_D", bleeding_D);
        editor.putInt("lastPeriod_D", lastPeriod_D);
        editor.putInt("lastPeriod_M", lastPeriod_M);
        editor.putInt("lastPeriod_Y", lastPeriod_Y);
        editor.apply();

    }


    public static void initEvents(Context context, int lastPeriod_Y, int lastPeriod_M, int lastPeriod_D, boolean reccursion) {

        Calendar startDateOfPeriod = Calendar.getInstance();
        startDateOfPeriod.set(lastPeriod_Y, lastPeriod_M, lastPeriod_D);
        Calendar lastDateOfPeriod = getNextDayOfCycle(lastPeriod_Y, lastPeriod_M, lastPeriod_D, bleeding_D);
        Calendar nextDayOfCycle = getNextDayOfCycle(lastPeriod_Y, lastPeriod_M, lastPeriod_D, cycle);

//      OVULATION
        int OVU = ( (daysBetweenDates(startDateOfPeriod.getTime(), nextDayOfCycle.getTime())) / 2);
        Calendar ovulationDate = getNextDayOfCycle(startDateOfPeriod.get(Calendar.YEAR), startDateOfPeriod.get(Calendar.MONTH), startDateOfPeriod.get(Calendar.DAY_OF_MONTH), OVU);
        ovulationCalendarList.add(ovulationDate);

//      FERTILE
        fertileCalendarList.addAll(getListOfDaysInBetween(
                getNextDayOfCycle(ovulationDate.get(Calendar.YEAR), ovulationDate.get(Calendar.MONTH), ovulationDate.get(Calendar.DAY_OF_MONTH), -5),
                getNextDayOfCycle(ovulationDate.get(Calendar.YEAR), ovulationDate.get(Calendar.MONTH), ovulationDate.get(Calendar.DAY_OF_MONTH), 6)
        ));

//        HIGH-CALENDAR LIST
        highCalendarList.add(ovulationDate);
        highCalendarList.add(getNextDayOfCycle(ovulationDate.get(Calendar.YEAR), ovulationDate.get(Calendar.MONTH), ovulationDate.get(Calendar.DAY_OF_MONTH), -1));

//        MEDIUM-CALENDAR LIST
        mediumCalendarList.addAll(getListOfDaysInBetween(
                getNextDayOfCycle(ovulationDate.get(Calendar.YEAR), ovulationDate.get(Calendar.MONTH), ovulationDate.get(Calendar.DAY_OF_MONTH), -5),
                getNextDayOfCycle(ovulationDate.get(Calendar.YEAR), ovulationDate.get(Calendar.MONTH), ovulationDate.get(Calendar.DAY_OF_MONTH), 0)
        ));
        mediumCalendarList.addAll(getListOfDaysInBetween(
                getNextDayOfCycle(ovulationDate.get(Calendar.YEAR), ovulationDate.get(Calendar.MONTH), ovulationDate.get(Calendar.DAY_OF_MONTH), 1),
                getNextDayOfCycle(ovulationDate.get(Calendar.YEAR), ovulationDate.get(Calendar.MONTH), ovulationDate.get(Calendar.DAY_OF_MONTH), 6)
        ));

//        LOW-CALENDAR LIST
        lowCalendarList.addAll(getListOfDaysInBetween(
                getNextDayOfCycle(ovulationDate.get(Calendar.YEAR), ovulationDate.get(Calendar.MONTH), ovulationDate.get(Calendar.DAY_OF_MONTH), -7),
                getNextDayOfCycle(ovulationDate.get(Calendar.YEAR), ovulationDate.get(Calendar.MONTH), ovulationDate.get(Calendar.DAY_OF_MONTH), -5)
        ));
        lowCalendarList.addAll(getListOfDaysInBetween(
                getNextDayOfCycle(ovulationDate.get(Calendar.YEAR), ovulationDate.get(Calendar.MONTH), ovulationDate.get(Calendar.DAY_OF_MONTH), 6),
                nextDayOfCycle
        ));



        if (!reccursion) {

            setPreferences(cycle, bleeding_D, CalculateUtil.lastPeriod_D, CalculateUtil.lastPeriod_M, CalculateUtil.lastPeriod_Y);
            periodCalendarList.addAll(getListOfDaysInBetween(startDateOfPeriod, lastDateOfPeriod));

        } else {
            futurePeriodCalendarList.addAll(getListOfDaysInBetween(startDateOfPeriod, lastDateOfPeriod));
        }


        if (lastDateOfPeriod.get(Calendar.YEAR) <= (CalculateUtil.lastPeriod_Y + 1) && reccursion)
            initEvents(context, nextDayOfCycle.get(Calendar.YEAR), nextDayOfCycle.get(Calendar.MONTH), nextDayOfCycle.get(Calendar.DAY_OF_MONTH), true);

        else {

            for (Calendar cal : futurePeriodCalendarList)
                eventList.add(new EventDay(cal, R.drawable.ic_future_blood, ContextCompat.getColor(context, R.color.red_D)));

            for (Calendar cal : periodCalendarList)
                eventList.add(new EventDay(cal, R.drawable.ic_blood, ContextCompat.getColor(context, R.color.red_D)));

            for (Calendar cal : ovulationCalendarList)
                eventList.add(new EventDay(cal, R.drawable.ic_egg, ContextCompat.getColor(context, R.color.red_D)));

            for (Calendar cal : fertileCalendarList)
                eventList.add(new EventDay(cal, R.drawable.ic_fertile, ContextCompat.getColor(context, R.color.red_D)));

        }

    }



    public static int daysBetweenDates(Date d1, Date d2){
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    } // daysBetweenDates()



    public static List<Calendar> getListOfDaysInBetween(Calendar startDateOfPeriod, Calendar lastDateOfPeriod) {

        List<Calendar> list = new ArrayList<>();

        while (startDateOfPeriod.before(lastDateOfPeriod)) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(startDateOfPeriod.get(Calendar.YEAR), startDateOfPeriod.get(Calendar.MONTH), startDateOfPeriod.get(Calendar.DAY_OF_MONTH));
            list.add(calendar);
            startDateOfPeriod.add(Calendar.DAY_OF_MONTH, 1);
        }

        return list;

    } // getListOfDaysInBetween()



    public static Calendar getNextDayOfCycle(int lastPeriod_Y, int lastPeriod_M, int lastPeriod_D, int cycle) {

//        CYCLE-DATE (eg. 1 - 21 = 22)
        calendarCycle = Calendar.getInstance();
        calendarCycle.set(lastPeriod_Y, lastPeriod_M, lastPeriod_D);
        calendarCycle.add(Calendar.DAY_OF_MONTH, cycle);

        return calendarCycle;

    } // getNextDayOfCycle()




}
