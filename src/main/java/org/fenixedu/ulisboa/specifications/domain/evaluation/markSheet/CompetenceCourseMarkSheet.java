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
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.EnrolmentEvaluation;
import org.fenixedu.academic.domain.EvaluationSeason;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.ulisboa.specifications.domain.evaluation.EvaluationComparator;
import org.fenixedu.ulisboa.specifications.domain.evaluation.season.EvaluationSeasonServices;
import org.fenixedu.ulisboa.specifications.domain.exceptions.ULisboaSpecificationsDomainException;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import com.google.common.collect.Sets;

import pt.ist.fenixframework.Atomic;

public class CompetenceCourseMarkSheet extends CompetenceCourseMarkSheet_Base {

    protected CompetenceCourseMarkSheet() {
        super();
    }

    protected void init(final ExecutionSemester executionSemester, final CompetenceCourse competenceCourse,
            final ExecutionCourse executionCourse, final EvaluationSeason evaluationSeason, final LocalDate evaluationDate,
            final Person certifier, final Set<Shift> shifts) {

        setExecutionSemester(executionSemester);
        setCompetenceCourse(competenceCourse);
        setExecutionCourse(executionCourse);
        setEvaluationSeason(evaluationSeason);
        setEvaluationDate(evaluationDate);
        setCertifier(certifier);
        getShiftSet().addAll(shifts);
        checkRules();
    }

    private void checkRules() {
        if (getExecutionCourse() == null) {
            throw new ULisboaSpecificationsDomainException("error.CompetenceCourseMarkSheet.executionCourse.required");
        }

        if (getEvaluationSeason() == null) {
            throw new ULisboaSpecificationsDomainException("error.CompetenceCourseMarkSheet.evaluationSeason.required");
        }

        if (getCompetenceCourse() == null) {
            throw new ULisboaSpecificationsDomainException("error.CompetenceCourseMarkSheet.competenceCourse.required");
        }

        if (getExecutionSemester() == null) {
            throw new ULisboaSpecificationsDomainException("error.CompetenceCourseMarkSheet.executionSemester.required");
        }

        if (getCertifier() == null) {
            throw new ULisboaSpecificationsDomainException("error.CompetenceCourseMarkSheet.certifier.required");
        }

        if (getEvaluationDate() == null) {
            throw new ULisboaSpecificationsDomainException("error.CompetenceCourseMarkSheet.evaluationDate.required");
        }

        // TODO legidio, needed in future? 
        // checkIfTeacherIsResponsibleOrCoordinator;
        checkIfEvaluationDateIsInExamsPeriod();
        checkIfEvaluationsDateIsEqualToMarkSheetEvaluationDate();
    }

    protected void checkIfEvaluationDateIsInExamsPeriod() {
// TODO legidio
//        CurricularCourse curricularCourse = getCompetenceCourse();
//        ExecutionDegree executionDegree = getExecutionDegree(getCurricularCourse(), getExecutionPeriod());
//        ExecutionSemester executionSemester = getExecutionSemester();
//        Date evaluationDate = getEvaluationDate();
//
//        if (executionDegree == null) {
//            if (!markSheetType.equals(MarkSheetType.IMPROVEMENT) || !curricularCourse.getDegreeCurricularPlan()
//                    .canSubmitImprovementMarkSheets(executionSemester.getExecutionYear())) {
//                throw new ULisboaSpecificationsDomainException("error.evaluationDateNotInExamsPeriod");
//            }
//
//        } else if (!executionDegree.isEvaluationDateInExamPeriod(evaluationDate, executionSemester, markSheetType)) {
//
//            OccupationPeriod occupationPeriod = executionDegree.getOccupationPeriodFor(executionSemester, markSheetType);
//            if (occupationPeriod == null) {
//                throw new ULisboaSpecificationsDomainException("error.evaluationDateNotInExamsPeriod");
//            } else {
//                throw new ULisboaSpecificationsDomainException(
//                        "error.evaluationDateNotInExamsPeriod.withEvaluationDateAndPeriodDates",
//                        DateFormatUtil.format("dd/MM/yyyy", evaluationDate),
//                        occupationPeriod.getStartYearMonthDay().toString("dd/MM/yyyy"),
//                        occupationPeriod.getEndYearMonthDay().toString("dd/MM/yyyy"));
//            }
//        }
    }

    private void checkIfEvaluationsDateIsEqualToMarkSheetEvaluationDate() {
        for (final EnrolmentEvaluation iter : getEnrolmentEvaluationSet()) {
            if (!iter.getExamDateYearMonthDay().toLocalDate().isEqual(getEvaluationDate())) {
                throw new ULisboaSpecificationsDomainException(
                        "error.MarkSheet.evaluations.examDate.must.be.equal.marksheet.evaluationDate");
            }
        }
    }

    @Atomic
    public void edit(final LocalDate evaluationDate, final Person certifier) {
        init(getExecutionSemester(), getCompetenceCourse(), getExecutionCourse(), getEvaluationSeason(), evaluationDate,
                certifier, getShiftSet());
        checkRules();
    }

    public void editEvaluations() {
        // TODO Auto-generated method stub

        if (getEvaluationDate() == null) {
            throw new ULisboaSpecificationsDomainException("error.CompetenceCourseMarkSheet.evaluationDate.required");
        }

        if (getEnrolmentEvaluationSet().isEmpty()) {
            throw new ULisboaSpecificationsDomainException("error.CompetenceCourseMarkSheet.enrolmentEvaluations.required");
        }
    }

    public CompetenceCourseMarkSheet rectify(String string) {
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
    public static CompetenceCourseMarkSheet create(final ExecutionSemester executionSemester,
            final CompetenceCourse competenceCourse, final ExecutionCourse executionCourse,
            final EvaluationSeason evaluationSeason, final LocalDate evaluationDate, final Person certifier,
            final Set<Shift> shifts, final boolean byTeacher) {

        final CompetenceCourseMarkSheet result = new CompetenceCourseMarkSheet();
        result.init(executionSemester, competenceCourse, executionCourse, evaluationSeason, evaluationDate, certifier, shifts);
        CompetenceCourseMarkSheetStateChange.createEditionState(result, byTeacher);
        return result;
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

        final Set<CompetenceCourseMarkSheet> result = Sets.newHashSet();
        if (executionSemester != null && competenceCourse != null) {
            result.addAll(executionSemester.getCompetenceCourseMarkSheetSet());
        }

        return result.stream().filter(c -> c.getCompetenceCourse() == competenceCourse);
    }

    private CompetenceCourseMarkSheetStateChange getStateChange() {
        return getStateChangeSet().stream().max(CompetenceCourseMarkSheetStateChange::compareTo).get();
    }

    public boolean isConfirmed() {
        return getStateChange().isConfirmed();
    }

    public DateTime getCreationDate() {
        //TODO LE:
        return getStateDate();
    }

    public Person getCreator() {
        //TODO LE:
        return User.findByUsername(getVersioningUpdatedBy().getUsername()).getPerson();
    }

    public String getState() {
        //TODO:LE
        return getStateChange().getState().getDescriptionI18N().getContent();
    }

    public DateTime getStateDate() {
        //TODO:LE
        return getStateChange().getDate();
    }

    public boolean getRectified() {
        return !getRectificationSet().isEmpty();
    }

    @Atomic
    public void markAsPrinted() {
        super.setPrinted(true);
    }

    public ExecutionYear getExecutionYear() {
        return getExecutionSemester().getExecutionYear();
    }

    public String getShiftsDescription() {
        final StringBuilder result = new StringBuilder();

        for (final Shift shift : getShiftSet()) {
            result.append(shift.getNome()).append(", ");
        }

        if (result.toString().endsWith(", ")) {
            result.delete(result.length() - 2, result.length());
        }

        return result.toString();
	}

    public Set<Enrolment> getEnrolmentsNotInAnyMarkSheet() {
        final Set<Enrolment> result = Sets.newHashSet();

        final ExecutionSemester executionSemester = getExecutionSemester();
        final EvaluationSeason evaluationSeason = getEvaluationSeason();

        for (final CurricularCourse curricularCourse : getExecutionCourse().getAssociatedCurricularCoursesSet()) {
            for (final Enrolment enrolment : getEnrolmentsForGradeSubmission(curricularCourse)) {

                final Optional<EnrolmentEvaluation> finalEvaluation =
                        enrolment.getEnrolmentEvaluation(evaluationSeason, executionSemester, true);
                if (finalEvaluation.isPresent()) {
                    continue;
                }

                final Optional<EnrolmentEvaluation> temporaryEvaluation =
                        enrolment.getEnrolmentEvaluation(evaluationSeason, executionSemester, true);
                if (temporaryEvaluation.isPresent() && temporaryEvaluation.get().getCompetenceCourseMarkSheet() != null) {
                    continue;
                }

                result.add(enrolment);
            }
        }

        return result;
    }

    private Set<Enrolment> getEnrolmentsForGradeSubmission(final CurricularCourse curricularCourse) {
        final Set<Enrolment> result = new HashSet<Enrolment>();

        final ExecutionSemester executionSemester = getExecutionSemester();
        final EvaluationSeason season = getEvaluationSeason();

        for (final CurriculumModule curriculumModule : curricularCourse.getCurriculumModulesSet()) {

            if (!curriculumModule.isEnrolment()) {
                continue;
            }

            final Enrolment enrolment = (Enrolment) curriculumModule;

            if (!season.isImprovement() && !enrolment.isValid(executionSemester)) {
                continue;
            }

            if (!isEnrolmentCandidateForEvaluation(enrolment)) {
                continue;
            }

            final Optional<EnrolmentEvaluation> evaluation = enrolment.getEnrolmentEvaluation(season, executionSemester, false);
            if (evaluation.isPresent() && evaluation.get().getCompetenceCourseMarkSheet() != null) {
                continue;
            }

            result.add(enrolment);
        }

        return result;
    }

    private boolean isEnrolmentCandidateForEvaluation(final Enrolment enrolment) {
        final ExecutionSemester executionSemester = getExecutionSemester();
        final EvaluationSeason season = getEvaluationSeason();

        if (enrolment.isEvaluatedInSeason(season, executionSemester)) {
            return false;
        }

        final Collection<EnrolmentEvaluation> evaluations = getAllFinalEnrolmentEvaluations(enrolment);
        final EnrolmentEvaluation latestEvaluation = getLatestEnrolmentEvaluation(evaluations);
        final boolean isApproved = latestEvaluation != null && latestEvaluation.isApproved();

        // this evaluation season is for not approved enrolments
        if (!season.isImprovement() && isApproved) {
            return false;
        }

        // this evaluation season is for approved enrolments
        if (season.isImprovement() && !isApproved) {
            return false;
        }

        if (EvaluationSeasonServices.hasPreviousSeasonBlockingGrade(season, latestEvaluation)) {
            return false;
        }

        if (!EvaluationSeasonServices.hasRequiredPreviousSeasonMinimumGrade(season, evaluations)) {
            return false;
        }

        final Optional<EnrolmentEvaluation> temporaryEvaluation =
                enrolment.getEnrolmentEvaluation(season, executionSemester, false);

        if (EvaluationSeasonServices.isRequiresEnrolmentEvaluation(season) && season.isImprovement()
                && temporaryEvaluation.isPresent()) {

            return temporaryEvaluation.get().getExecutionPeriod() == executionSemester;

        } else {

            return !temporaryEvaluation.isPresent();
        }
    }

    /**
     * Returns final evaluations that took place before the evaluation date
     */
    private Collection<EnrolmentEvaluation> getAllFinalEnrolmentEvaluations(final Enrolment enrolment) {
        final Collection<EnrolmentEvaluation> evaluations = enrolment.getAllFinalEnrolmentEvaluations();

        for (final Iterator<EnrolmentEvaluation> iterator = evaluations.iterator(); iterator.hasNext();) {
            final EnrolmentEvaluation enrolmentEvaluation = iterator.next();

            final YearMonthDay examDate = enrolmentEvaluation.getExamDateYearMonthDay();
            if (examDate != null && !examDate.isBefore(getEvaluationDate())) {
                iterator.remove();
            }
        }
        return evaluations;
    }

    private EnrolmentEvaluation getLatestEnrolmentEvaluation(final Collection<EnrolmentEvaluation> evaluations) {
        return ((evaluations == null || evaluations.isEmpty()) ? null : Collections.<EnrolmentEvaluation> max(evaluations,
                new EvaluationComparator()));
    }

    private EnrolmentEvaluation getPreviousSeasonEnrolmentEvaluation(final Collection<EnrolmentEvaluation> evaluations) {
        if (evaluations != null) {

            for (final EnrolmentEvaluation iter : evaluations) {

            }
        }

        return ((evaluations == null || evaluations.isEmpty()) ? null : Collections.<EnrolmentEvaluation> max(evaluations,
                new EvaluationComparator()));
    }

}
