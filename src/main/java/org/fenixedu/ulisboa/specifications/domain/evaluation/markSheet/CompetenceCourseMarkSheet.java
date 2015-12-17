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
import java.util.Set;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.EvaluationSeason;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.ulisboa.specifications.domain.exceptions.ULisboaSpecificationsDomainException;
import org.joda.time.LocalDate;

import com.google.common.collect.Sets;

import pt.ist.fenixframework.Atomic;

public class CompetenceCourseMarkSheet extends CompetenceCourseMarkSheet_Base {

    protected CompetenceCourseMarkSheet() {
        super();
    }

    protected void init(final EvaluationSeason evaluationSeason, final LocalDate evaluationDate) {
        setEvaluationSeason(evaluationSeason);
        setEvaluationDate(evaluationDate);
        checkRules();
    }

    private void checkRules() {
        if (getRectified() == null) {
            throw new ULisboaSpecificationsDomainException("error.CompetenceCourseMarkSheet.rectified.required");
        }

        if (getEvaluationSeason() == null) {
            throw new ULisboaSpecificationsDomainException("error.CompetenceCourseMarkSheet.evaluationSeason.required");
        }
    }

    @Atomic
    public void edit(final CompetenceCourseMarkSheet rectified, final EvaluationSeason evaluationSeason,
            final LocalDate evaluationDate, final java.lang.String checkSum, final boolean printed) {
        setRectified(rectified);
        setEvaluationSeason(evaluationSeason);
        setEvaluationDate(evaluationDate);
        setCheckSum(checkSum);
        setPrinted(printed);
        checkRules();
    }

    public void editEvaluations() {
        // TODO Auto-generated method stub

    }

    public CompetenceCourseMarkSheet rectify() {
        // TODO Auto-generated method stub
        return null;
    }

    public void annul(String reason) {
        // TODO Auto-generated method stub

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
    public static CompetenceCourseMarkSheet create(final EvaluationSeason evaluationSeason, final LocalDate evaluationDate) {
        CompetenceCourseMarkSheet competenceCourseMarkSheet = new CompetenceCourseMarkSheet();
        competenceCourseMarkSheet.init(evaluationSeason, evaluationDate);
        return competenceCourseMarkSheet;
    }

    // @formatter: off
    /************
     * SERVICES
     * 
     * @param competenceCourse
     * @param executionSemester *
     ************/
    // @formatter: on

    public static Stream<CompetenceCourseMarkSheet> findBy(final ExecutionSemester executionSemester,
            final CompetenceCourse competenceCourse) {

        final Set<CompetenceCourseMarkSheet> result = Sets.<CompetenceCourseMarkSheet> newHashSet();
        if (executionSemester != null && competenceCourse != null) {
            executionSemester.getCompetenceCourseMarkSheetSet();
        }

        return result.stream();
    }

}
