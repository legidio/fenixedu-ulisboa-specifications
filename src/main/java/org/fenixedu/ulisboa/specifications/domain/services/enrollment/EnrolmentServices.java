package org.fenixedu.ulisboa.specifications.domain.services.enrollment;

import java.util.Collection;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.Shift;

public class EnrolmentServices {

    static public Collection<Shift> getShiftsFor(final Enrolment enrolment) {
        return enrolment.getRegistration()
                .getShiftEnrolmentsSet().stream().filter(s -> s.getShift().getExecutionCourse()
                        .getAssociatedCurricularCoursesSet().contains(enrolment.getCurricularCourse()))
                .map(s -> s.getShift()).collect(Collectors.toSet());
    }

    static public boolean containsAnyShift(final Enrolment enrolment, final Collection<Shift> shifts) {
        return getShiftsFor(enrolment).stream().anyMatch(s -> shifts.contains(s));
    }

}
