/**
 * 
 */
package org.fenixedu.ulisboa.specifications.domain.studentCurriculum;

import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.SchoolClass;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.ShiftType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.bennu.signals.DomainObjectEvent;
import org.fenixedu.ulisboa.specifications.domain.curricularRules.StudentSchoolClassCurricularRule;
import org.fenixedu.ulisboa.specifications.domain.services.RegistrationServices;

/**
 * @author shezad
 *
 */
public class StudentScheduleListeners {

    static public Consumer<DomainObjectEvent<Enrolment>> SHIFTS_ENROLLER = new Consumer<DomainObjectEvent<Enrolment>>() {

        @Override
        public void accept(DomainObjectEvent<Enrolment> event) {
            final Enrolment enrolment = event.getInstance();
            final ExecutionSemester executionSemester = enrolment.getExecutionPeriod();
            final Attends attends = enrolment.getAttendsFor(executionSemester);

            if (attends == null) {
                return;
            }

            boolean enrolInShiftIfUnique = enrolment.getCurricularRules(executionSemester).stream()
                    .filter(cr -> cr instanceof StudentSchoolClassCurricularRule).map(cr -> (StudentSchoolClassCurricularRule) cr)
                    .anyMatch(ssccr -> ssccr.getEnrolInShiftIfUnique());
            if (enrolInShiftIfUnique) {

                final Registration registration = enrolment.getRegistration();
                final ExecutionCourse executionCourse = attends.getExecutionCourse();

                final Optional<SchoolClass> schoolClassOpt =
                        RegistrationServices.getSchoolClassBy(registration, executionSemester);

                if (schoolClassOpt.isPresent()) {
                    final SchoolClass schoolClass = schoolClassOpt.get();
                    final Set<Shift> shifts = schoolClass.getAssociatedShiftsSet().stream()
                            .filter(s -> s.getExecutionCourse() == executionCourse).collect(Collectors.toSet());
                    if (!shifts.isEmpty()) {
                        for (final Shift shift : shifts) {
                            shift.reserveForStudent(registration);
                        }
                        return;
                    }
                }

                for (final ShiftType shiftType : executionCourse.getShiftTypes()) {
                    final SortedSet<Shift> shiftsByType = executionCourse.getShiftsByTypeOrderedByShiftName(shiftType);
                    if (shiftsByType.size() == 1) {
                        shiftsByType.iterator().next().reserveForStudent(registration);
                    }
                }

            }

        }

    };

}
