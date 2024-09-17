package com.final_project_clinic.doctor.utils;

import com.final_project_clinic.doctor.data.model.DoctorSchedule;
import com.final_project_clinic.doctor.data.model.ScheduleTime;
import com.final_project_clinic.doctor.dto.ScheduleTimeDTO;
import com.final_project_clinic.doctor.exception.InvalidPhoneNumberException;

import java.time.LocalTime;
import java.util.List;

public class ValidationUtils {

    private ValidationUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    private static final String PHONE_REGEX = "\\+62\\d{9,13}";

    public static void validatePhoneNumber(String phoneNumber) {
        if (!phoneNumber.matches(PHONE_REGEX)) {
            throw new InvalidPhoneNumberException("Invalid phone number. The phone number must start with +62 and contain 9 to 13 digits.");
        }
    }

    public static void validateNoInternalOverlap(List<ScheduleTimeDTO> newScheduleTimes) {
        for (int i = 0; i < newScheduleTimes.size(); i++) {
            LocalTime startTime1 = newScheduleTimes.get(i).getStartWorkingHour();
            LocalTime endTime1 = newScheduleTimes.get(i).getEndWorkingHour();

            for (int j = i + 1; j < newScheduleTimes.size(); j++) {
                LocalTime startTime2 = newScheduleTimes.get(j).getStartWorkingHour();
                LocalTime endTime2 = newScheduleTimes.get(j).getEndWorkingHour();

                if ((startTime1.isBefore(endTime2) && endTime1.isAfter(startTime2))) {
                    throw new IllegalArgumentException("Schedule times within the request overlap.");
                }
            }
        }
    }

    public static void validateNoOverlap(DoctorSchedule existingSchedule, List<ScheduleTimeDTO> newScheduleTimes) {
        List<ScheduleTime> existingTimes = existingSchedule.getScheduleTimes();

        for (ScheduleTimeDTO newTime : newScheduleTimes) {
            LocalTime newStart = newTime.getStartWorkingHour();
            LocalTime newEnd = newTime.getEndWorkingHour();

            for (ScheduleTime existingTime : existingTimes) {
                LocalTime existingStart = existingTime.getStartWorkingHour();
                LocalTime existingEnd = existingTime.getEndWorkingHour();

                if ((newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart))) {
                    throw new IllegalArgumentException("Schedule time overlaps with existing times.");
                }
            }
        }
    }

    public static void validateNoOverlapEdit(List<ScheduleTime> existingTimes, ScheduleTimeDTO newTime) {
        for (ScheduleTime existingTime : existingTimes) {
            boolean isOverlapping = isTimeOverlapping(
                    existingTime.getStartWorkingHour(), existingTime.getEndWorkingHour(),
                    newTime.getStartWorkingHour(), newTime.getEndWorkingHour()
            );

            if (isOverlapping) {
                throw new IllegalArgumentException("Schedule time overlaps with an existing schedule");
            }
        }
    }

    private static boolean isTimeOverlapping(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return (start1.isBefore(end2) && start2.isBefore(end1));
    }

}
