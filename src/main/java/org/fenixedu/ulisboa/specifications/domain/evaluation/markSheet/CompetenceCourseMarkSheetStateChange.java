/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: luis.egidio@qub-it.com
 *
 * 
 * This file is part of FenixEdu Specifications.
 *
 * FenixEdu Specifications is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Specifications is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Specifications.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.fenixedu.ulisboa.specifications.domain.evaluation.markSheet;

import java.util.Collection;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.ulisboa.specifications.domain.exceptions.ULisboaSpecificationsDomainException;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class CompetenceCourseMarkSheetStateChange extends CompetenceCourseMarkSheetStateChange_Base {

    protected CompetenceCourseMarkSheetStateChange() {
        super();
    }

    protected void init(final CompetenceCourseMarkSheet competenceCourseMarkSheet, final CompetenceCourseMarkSheetStateEnum state,
            final DateTime date, final java.lang.String reason, final boolean byTeacher) {
        setCompetenceCourseMarkSheet(competenceCourseMarkSheet);
        setState(state);
        setDate(date);
        setReason(reason);
        setByTeacher(byTeacher);
        checkRules();
    }

    private void checkRules() {
        if (getCompetenceCourseMarkSheet() == null) {
            throw new ULisboaSpecificationsDomainException(
                    "error.CompetenceCourseMarkSheetStateChange.competenceCourseMarkSheet.required");
        }
    }

    @Atomic
    public void edit(final CompetenceCourseMarkSheet competenceCourseMarkSheet, final CompetenceCourseMarkSheetStateEnum state,
            final DateTime date, final java.lang.String reason, final boolean byTeacher) {
        setCompetenceCourseMarkSheet(competenceCourseMarkSheet);
        setState(state);
        setDate(date);
        setReason(reason);
        setByTeacher(byTeacher);
        checkRules();
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);
    }

    @Atomic
    public void delete() {
        ULisboaSpecificationsDomainException.throwWhenDeleteBlocked(getDeletionBlockers());
        deleteDomainObject();
    }

    @Atomic
    public static CompetenceCourseMarkSheetStateChange create(final CompetenceCourseMarkSheet competenceCourseMarkSheet,
            final CompetenceCourseMarkSheetStateEnum state, final DateTime date, final java.lang.String reason,
            final boolean byTeacher) {
        CompetenceCourseMarkSheetStateChange competenceCourseMarkSheetStateChange = new CompetenceCourseMarkSheetStateChange();
        competenceCourseMarkSheetStateChange.init(competenceCourseMarkSheet, state, date, reason, byTeacher);
        return competenceCourseMarkSheetStateChange;
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Stream<CompetenceCourseMarkSheetStateChange> findByCompetenceCourseMarkSheet(
            final CompetenceCourseMarkSheet competenceCourseMarkSheet) {
        return competenceCourseMarkSheet.getStateChangeSet().stream()
                .filter(i -> competenceCourseMarkSheet.equals(i.getCompetenceCourseMarkSheet()));
    }

}
