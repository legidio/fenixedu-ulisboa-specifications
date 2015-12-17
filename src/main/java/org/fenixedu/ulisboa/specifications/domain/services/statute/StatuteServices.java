package org.fenixedu.ulisboa.specifications.domain.services.statute;

import java.util.Collection;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.StatuteType;

public class StatuteServices {

    static public Collection<StatuteType> findStatuteTypes(final Registration registration) {

        return registration.getStudent().getStudentStatutesSet().stream()
                .filter(s -> s.getRegistration() == null || s.getRegistration() == registration).map(s -> s.getType())
                .collect(Collectors.toSet());
    }

    static public Collection<StatuteType> findVisibleStatuteTypes(final Registration registration) {
        return findStatuteTypes(registration).stream().filter(s -> s.getVisible()).collect(Collectors.toSet());
    }

    static public String getStatuteTypesDescription(final Registration registration) {
        return findStatuteTypes(registration).stream().map(s -> s.getName().getContent()).collect(Collectors.joining(", "));

    }

    static public String getVisibleStatuteTypesDescription(final Registration registration) {
        return findVisibleStatuteTypes(registration).stream().map(s -> s.getName().getContent()).collect(Collectors.joining(", "));

    }
    
}
