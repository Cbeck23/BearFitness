package org.example.users;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;




class ScheduledClass {
    Date date;
    String time;
    Integer sessionLength; // session length in minutes
    Integer numWeeks;

    static int participantLimit;

    FitnessLevel fitnessLevel;

    /// WORKOUT AND EQUIPMENT CLASSES NEED TO BE IMPLIMENTED, DISCUSS OPTIONS
    //List<Workout> prerequisites = new ArrayList<>();
    //List<Equipment> requiredEquipment = new ArrayList<>();
    List<DayOfWeek> daysOfWeek= new ArrayList<>();
}
