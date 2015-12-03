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

package org.fenixedu.ulisboa.specifications.domain.evaluation;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.EvaluationSeason;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.curriculum.EnrolmentEvaluationContext;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.treasury.util.LocalizedStringUtil;
import org.fenixedu.ulisboa.specifications.domain.exceptions.ULisboaSpecificationsDomainException;
import org.fenixedu.ulisboa.specifications.util.ULisboaSpecificationsUtil;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.core.AbstractDomainObjectServices;

abstract public class EvaluationSeasonServices {

    private static final Logger logger = LoggerFactory.getLogger(EvaluationSeasonServices.class);

    static private void init(final EvaluationSeason evaluationSeason, final boolean active,
            final boolean requiresEnrolmentEvaluation) {

        EvaluationSeasonInformation.create(evaluationSeason, active, requiresEnrolmentEvaluation);

        checkRules(evaluationSeason);
    }

    static private void checkRules(final EvaluationSeason evaluationSeason) {
        if (evaluationSeason.getInformation() == null) {
            throw new ULisboaSpecificationsDomainException("error.EvaluationSeason.evaluationSeasonInformation.required");
        }

        if (Strings.isNullOrEmpty(evaluationSeason.getCode())) {
            throw new ULisboaSpecificationsDomainException("error.EvaluationSeason.code.required");
        }

        if (LocalizedStringUtil.isTrimmedEmpty(evaluationSeason.getAcronym())) {
            throw new ULisboaSpecificationsDomainException("error.EvaluationSeason.acronym.required");
        }

        if (LocalizedStringUtil.isTrimmedEmpty(evaluationSeason.getName())) {
            throw new ULisboaSpecificationsDomainException("error.EvaluationSeason.name.required");
        }

        if (!checkNTrue(1, evaluationSeason.getNormal(), evaluationSeason.getImprovement(),
                evaluationSeason.getSpecialAuthorization(), evaluationSeason.getSpecial())) {
            throw new ULisboaSpecificationsDomainException("error.EvaluationSeason.type.not.unique");
        }

        checkSeasonExistsForName(evaluationSeason, evaluationSeason.getName());
    }

    static private void checkSeasonExistsForName(final EvaluationSeason evaluationSeason, final LocalizedString name) {

        for (final EvaluationSeason season : findByName(name).collect(Collectors.toSet())) {
            if (season != evaluationSeason) {
                throw new ULisboaSpecificationsDomainException("error.EvaluationSeason.duplicated.name");
            }
        }
    }

    static private boolean checkNTrue(int n, boolean... args) {
        assert args.length > 0;
        int count = 0;
        for (boolean b : args) {
            if (b)
                count++;
            if (count > n)
                return false;
        }
        return (count == n);
    }

    @Atomic
    static public void edit(final EvaluationSeason evaluationSeason, final String code, final LocalizedString acronym,
            final LocalizedString name, final boolean normal, final boolean improvement, final boolean special,
            final boolean specialAuthorization, final boolean active, final boolean requiresEnrolmentEvaluation) {

        checkSeasonExistsForName(evaluationSeason, name);

        evaluationSeason.setCode(code);
        evaluationSeason.setAcronym(acronym);
        evaluationSeason.setName(name);
        evaluationSeason.setNormal(normal);
        evaluationSeason.setImprovement(improvement);
        evaluationSeason.setSpecial(special);
        evaluationSeason.setSpecialAuthorization(specialAuthorization);

        evaluationSeason.getInformation().edit(active, requiresEnrolmentEvaluation);
        checkRules(evaluationSeason);
    }

    @Atomic
    static public EvaluationSeason create(final String code, final LocalizedString acronym, final LocalizedString name,
            final boolean normal, final boolean improvement, final boolean special, final boolean specialAuthorization,
            final boolean active, final boolean requiresEnrolmentEvaluation) {

        final EvaluationSeason evaluationSeason =
                new EvaluationSeason(acronym, name, normal, improvement, specialAuthorization, special);
        evaluationSeason.setCode(code);

        init(evaluationSeason, active, requiresEnrolmentEvaluation);
        return evaluationSeason;
    }

    static public Stream<EvaluationSeason> findByCode(final String code) {
        return EvaluationSeason.all().filter(i -> code.equalsIgnoreCase(i.getCode()));
    }

    static public Stream<EvaluationSeason> findByAcronym(final LocalizedString acronym) {
        return EvaluationSeason.all().filter(i -> acronym.equals(i.getAcronym()));
    }

    static public Stream<EvaluationSeason> findByName(final LocalizedString name) {
        return EvaluationSeason.all().filter(i -> name.equals(i.getName()));
    }

    static public Stream<EvaluationSeason> findBySeasonOrder(final Integer seasonOrder) {
        return EvaluationSeason.all().filter(i -> seasonOrder.equals(getSeasonOrder(i)));
    }

    static public Stream<EvaluationSeason> findByActive(final boolean active) {
        return EvaluationSeason.all().sorted(SEASON_ORDER_COMPARATOR).filter(i -> active == getActive(i));
    }

    static public Stream<EvaluationSeason> findByRequiresEnrolmentEvaluation(final boolean requiresEnrolmentEvaluation) {
        return EvaluationSeason.all().filter(i -> requiresEnrolmentEvaluation == getRequiresEnrolmentEvaluation(i));
    }

    static public LocalizedString getDescriptionI18N(final EvaluationSeason input) {
        LocalizedString result = new LocalizedString();

        if (input != null) {
            result = result.append(input.getName());
            result = result.append(" [");
            result = result.append(getEnrolmentEvaluationType(input).getDescriptionI18N());
            result = result.append("]");
        }

        return result;
    }

    static public Set<Enrolment> getEnrolmentsForGradeSubmission(final CurricularCourse curricularCourse,
            final LocalDate evaluationDate, final ExecutionSemester executionSemester) {

        final Set<Enrolment> result = new HashSet<Enrolment>();
        for (final CurriculumModule curriculumModule : curricularCourse.getCurriculumModulesSet()) {

            if (!curriculumModule.isEnrolment()) {
                continue;
            }

            final Enrolment enrolment = (Enrolment) curriculumModule;
// TODO
//            if (!isEvaluatesOnlyApprovedEnrolments() && !enrolment.isValid(executionSemester)) {
//                continue;
//            }
//
//            if (!isEnrolmentCandidateForEvaluation(enrolment, evaluationDate, executionSemester)) {
//                continue;
//            }
//
//            final EnrolmentEvaluation evaluation =
//                    enrolment.getActiveEvaluationBySeason(this, evaluationDate, executionSemester, false);
//
//            if (evaluation != null && evaluation.hasMarkSheet()) {
//                continue;
//            }

            result.add(enrolment);
        }

        return result;
    }

    static public Set<Enrolment> getEnrolmentsForGradeSubmission(final Collection<CurricularCourse> curricularCourses,
            final LocalDate evaluationDate, final ExecutionSemester executionSemester) {

        final Set<Enrolment> result = new HashSet<Enrolment>();

        for (CurricularCourse curricularCourse : curricularCourses) {
            result.addAll(getEnrolmentsForGradeSubmission(curricularCourse, evaluationDate, executionSemester));
        }

        return result;

    }

    protected boolean isEnrolmentCandidateForEvaluation(final Enrolment enrolment, final LocalDate evaluationDate,
            final ExecutionSemester executionSemester) {

// TODO
        return true;
//
//        if (isEnrolmentEvaluatedInSeason(enrolment, evaluationDate, executionSemester)) {
//            return false;
//        }
//
//        // only evaluations before the input evaluation date should be investigated
//        final List<EnrolmentEvaluation> evaluations = enrolment.getAllFinalEnrolmentEvaluations(evaluationDate);
//        final EnrolmentEvaluation latestEvaluation = Enrolment.getLatestEnrolmentEvaluation(evaluations);
//        final boolean isApproved = latestEvaluation != null && latestEvaluation.isApproved();
//
//        // this evaluation season is for not approved enrolments
//        if (!isEvaluatesOnlyApprovedEnrolments()) {
//
//            if (isApproved) {
//                return false;
//            }
//        }
//
//        // this evaluation season is for approved enrolments
//        if (isEvaluatesOnlyApprovedEnrolments()) {
//
//            if (!isApproved) {
//                return false;
//            }
//        }
//
//        if (latestEvaluation != null && latestEvaluation.isSubsequentEvaluationPrevented()) {
//            return false;
//        }
//
//        if (isRequiresEvaluationOnPreviousSeason()) {
//            if (latestEvaluation == null) {
//                return false;
//            }
//
//            boolean exclude = true;
//
//            final EvaluationSeason previousSeason = getPreviousSeason();
//            if (previousSeason != null) {
//
//                // WARNING: we should be using EnrolmentEvaluation.find API, but for simplicity reasons we're dealing with this "manually" 
//                for (final EnrolmentEvaluation iter : evaluations) {
//                    if (iter.getEvaluationSeason() == previousSeason) {
//                        exclude = false;
//                        break;
//                    }
//                }
//            }
//
//            if (exclude) {
//                return false;
//            }
//        }
//
//        final EnrolmentEvaluation temporaryEvaluation =
//                enrolment.getActiveEvaluationBySeason(this, evaluationDate, executionSemester, false);
//
//        if (isRequiresEnrolmentEvaluation() && isEvaluatesOnlyApprovedEnrolments() && temporaryEvaluation != null) {
//            return temporaryEvaluation.getExecutionPeriod() == executionSemester;
//        }
//
//        if (isRequiresEnrolmentEvaluation() && isSupportsMultipleEvaluation() && temporaryEvaluation != null) {
//            return evaluationDate != null && temporaryEvaluation.getExamDateYearMonthDay().toLocalDate().isEqual(evaluationDate);
//        }
//
//        return !this.isRequiresEnrolmentEvaluation() || temporaryEvaluation != null;

    }

    public void validateEnrolment(final Enrolment enrolment, final LocalDate evaluationDate,
            final ExecutionSemester executionSemester, final EnrolmentEvaluationContext context) {
// TODO
//
//        if (isEnrolmentEvaluatedInSeason(enrolment, evaluationDate, executionSemester)) {
//            throw new ULisboaSpecificationsDomainException("error.EvaluationSeason.enrolment.evaluated.in.this.season");
//        }
//
//        if (context == EnrolmentEvaluationContext.MARK_SHEET_EVALUATION) {
//            if (isAlreadyEnroledInSeason(enrolment, evaluationDate, executionSemester)) {
//                throw new ULisboaSpecificationsDomainException("error.EvaluationSeason.already.enroled.in.this.season");
//            }
//
//            if (enrolment.isApproved() && !isEvaluatesOnlyApprovedEnrolments()) {
//                throw new ULisboaSpecificationsDomainException("error.EvaluationSeason.evaluation.already.approved");
//            }
//        }
    }

    public void validateEvaluation(final Enrolment enrolment, final LocalDate evaluationDate,
            final ExecutionSemester executionSemester, final EnrolmentEvaluationContext context) {

// TODO
//        if (isEnrolmentEvaluatedInSeason(enrolment, evaluationDate, executionSemester)) {
//            throw new ULisboaSpecificationsDomainException("error.EvaluationSeason.enrolment.evaluated.in.this.season",
//                    enrolment.getStudent().getNumber().toString());
//        }
//
//        if (isRequiresEvaluationOnPreviousSeason() && !isFirst()
//                && !getPreviousSeason().isEnrolmentEvaluatedInSeason(enrolment, evaluationDate, executionSemester)) {
//            throw new ULisboaSpecificationsDomainException("error.EvaluationSeason.enrolment.must.be.evaluated.on.previous.season",
//                    enrolment.getStudent().getNumber().toString());
//        }
//
//        // only evaluations before the input evaluation date should be investigated
//        final List<EnrolmentEvaluation> evaluations = enrolment.getAllFinalEnrolmentEvaluations(evaluationDate);
//        final EnrolmentEvaluation latestEvaluation = Enrolment.getLatestEnrolmentEvaluation(evaluations);
//        final boolean isApproved = latestEvaluation != null && latestEvaluation.isApproved();
//
//        if (context == EnrolmentEvaluationContext.MARK_SHEET_EVALUATION) {
//
//            // this evaluation season is for not approved enrolments
//            if (!isEvaluatesOnlyApprovedEnrolments()) {
//
//                if (isApproved) {
//
//                    throw new ULisboaSpecificationsDomainException("error.EvaluationSeason.evaluation.already.approved",
//                            enrolment.getStudent().getNumber().toString());
//                }
//            }
//
//            final EnrolmentEvaluation temporaryEvaluation =
//                    enrolment.getActiveEvaluationBySeason(this, evaluationDate, executionSemester, false);
//
//            // @RequestReview, different from isEnrolmentCandidateForEvaluation?
//            if (isRequiresEnrolmentEvaluation() && temporaryEvaluation == null) {
//                throw new ULisboaSpecificationsDomainException("error.EvaluationSeason.enrolment.evaluation.required",
//                        enrolment.getStudent().getNumber().toString());
//            }
//        }
//
//        if (latestEvaluation != null && latestEvaluation.isSubsequentEvaluationPrevented()) {
//            throw new ULisboaSpecificationsDomainException("error.EvaluationSeason.subsequent.evaluation.locked");
//        }
    }

    static public EvaluationSeason getPreviousSeason(final EvaluationSeason input) {
        EvaluationSeason result = null;

        for (final EvaluationSeason iter : EvaluationSeason.all().collect(Collectors.toSet())) {
            if (iter == input) {
                continue;
            }

            if (getEnrolmentEvaluationType(iter) != getEnrolmentEvaluationType(input)) {
                continue;
            }

            if (getSeasonOrder(iter) > getSeasonOrder(input)) {
                continue;
            }

            if (result != null && getSeasonOrder(result) > getSeasonOrder(iter)) {
                continue;
            }

            result = iter;
        }

        return result;
    }

    static public EvaluationSeason getNextSeason(final EvaluationSeason input) {
        EvaluationSeason result = null;

        for (final EvaluationSeason iter : EvaluationSeason.all().collect(Collectors.toSet())) {
            if (iter == input) {
                continue;
            }

            if (getEnrolmentEvaluationType(iter) != getEnrolmentEvaluationType(input)) {
                continue;
            }

            if (getSeasonOrder(iter) < getSeasonOrder(input)) {
                continue;
            }

            if (result != null && getSeasonOrder(result) < getSeasonOrder(iter)) {
                continue;
            }

            result = iter;
        }

        return result;
    }

    static public EvaluationSeason getFirstSeasonInChain(final EvaluationSeason input) {
        if (isFirst(input)) {
            return input;
        }

        return getFirstSeasonInChain(getPreviousSeason(input));
    }

    static public boolean isFirst(final EvaluationSeason input) {
        return getPreviousSeason(input) == null;
    }

    static public boolean isLast(final EvaluationSeason input) {
        return getNextSeason(input) == null;
    }

    static public boolean isDeletable(final EvaluationSeason evaluationSeason) {
        return evaluationSeason.getMarkSheetSet().size() == 0 && evaluationSeason.getEvaluationSet().size() == 0;
    }

    static public boolean getActive(final EvaluationSeason input) {
        return input.getInformation().getActive();
    }

    static public boolean getRequiresEnrolmentEvaluation(final EvaluationSeason input) {
        return input.getInformation().getRequiresEnrolmentEvaluation();
    }

    static public Integer getSeasonOrder(final EvaluationSeason input) {
        final EvaluationSeasonInformation information = input.getInformation();

        // bulletproof in order to be bootstrap-safe
        return information == null || information.getSeasonOrder() == null ? 0 : information.getSeasonOrder();
    }

    static public void setSeasonOrder(final EvaluationSeason evaluationSeason, final Integer order) {
        evaluationSeason.getInformation().setSeasonOrder(order);
    }

    @Atomic
    static public void orderUp(final EvaluationSeason input) {
        if (isFirst(input)) {
            return;
        }

        final EvaluationSeason previousSeason = getPreviousSeason(input);
        final Integer temp = getSeasonOrder(previousSeason);
        setSeasonOrder(previousSeason, getSeasonOrder(input));
        setSeasonOrder(input, temp);
    }

    @Atomic
    static public void orderDown(final EvaluationSeason input) {
        if (isLast(input)) {
            return;
        }

        final EvaluationSeason previousSeason = getPreviousSeason(input);
        final Integer temp = getSeasonOrder(previousSeason);
        setSeasonOrder(previousSeason, getSeasonOrder(input));
        setSeasonOrder(input, temp);
    }

    @Atomic
    static public void delete(final EvaluationSeason evaluationSeason) {
        if (!isDeletable(evaluationSeason)) {
            throw new ULisboaSpecificationsDomainException("error.EvaluationSeason.not.empty.to.delete");
        }

        AbstractDomainObjectServices.deleteDomainObject(evaluationSeason);
    }

    static private enum EnrolmentEvaluationType {

        NORMAL("label.EvaluationSeason.normal"),

        SPECIAL_SEASON("label.EvaluationSeason.special"),

        IMPROVEMENT("label.EvaluationSeason.improvement"),

        SPECIAL_AUTHORIZATION("label.EvaluationSeason.specialAuthorization");

        private String descriptionKey;

        private EnrolmentEvaluationType(final String descriptionKey) {
            this.descriptionKey = descriptionKey;
        }

        public LocalizedString getDescriptionI18N() {
            return ULisboaSpecificationsUtil.bundleI18N(this.descriptionKey);
        }
    }

    static private EnrolmentEvaluationType getEnrolmentEvaluationType(final EvaluationSeason input) {
        if (input.isNormal()) {
            return EnrolmentEvaluationType.NORMAL;
        } else if (input.isSpecial()) {
            return EnrolmentEvaluationType.SPECIAL_SEASON;
        } else if (input.isImprovement()) {
            return EnrolmentEvaluationType.IMPROVEMENT;
        } else if (input.isSpecialAuthorization()) {
            return EnrolmentEvaluationType.SPECIAL_AUTHORIZATION;
        }

        throw new ULisboaSpecificationsDomainException("");
    }

    static private Integer getEnrolmentEvaluationTypePrecedence(final EvaluationSeason input) {
        return getEnrolmentEvaluationType(input).ordinal();
    }

    public static Comparator<EvaluationSeason> SEASON_ORDER_COMPARATOR = new Comparator<EvaluationSeason>() {

        @Override
        public int compare(EvaluationSeason o1, EvaluationSeason o2) {
            int result = getEnrolmentEvaluationTypePrecedence(o1).compareTo(getEnrolmentEvaluationTypePrecedence(o2));

            if (result == 0) {
                result = getSeasonOrder(o1).compareTo(getSeasonOrder(o2));
            }

            if (result == 0) {
                result = o1.compareTo(o2);
            }

            return result;
        }
    };

    @Atomic
    static public void initialize() {
        final List<EvaluationSeason> seasons =
                EvaluationSeason.all().sorted(SEASON_ORDER_COMPARATOR).collect(Collectors.toList());

        for (int i = 0; i < seasons.size(); i++) {
            final EvaluationSeason iter = seasons.get(i);

            if (iter.getInformation() == null) {
                logger.info("Init " + iter.getName().getContent());
                EvaluationSeasonInformation.create(iter, true, false).setSeasonOrder(i);
            }
        }
    }

    static public Integer maxOrder() {
        int result = 0;

        for (final EvaluationSeason iter : EvaluationSeason.all().collect(Collectors.toSet())) {
            final Integer order = getSeasonOrder(iter);
            if (order > result) {
                result = order;
            }
        }

        return result;
    }

}
