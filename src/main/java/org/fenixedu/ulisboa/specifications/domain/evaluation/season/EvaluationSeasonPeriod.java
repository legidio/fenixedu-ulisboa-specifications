package org.fenixedu.ulisboa.specifications.domain.evaluation.season;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.CurricularYearList;
import org.fenixedu.academic.domain.EvaluationSeason;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.OccupationPeriod;
import org.fenixedu.academic.domain.OccupationPeriodReference;
import org.fenixedu.academic.domain.OccupationPeriodType;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.util.date.IntervalTools;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.ulisboa.specifications.domain.exceptions.ULisboaSpecificationsDomainException;
import org.fenixedu.ulisboa.specifications.util.ULisboaSpecificationsUtil;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

import com.google.common.collect.Sets;

import pt.ist.fenixframework.Atomic;

public class EvaluationSeasonPeriod extends EvaluationSeasonPeriod_Base {

    protected EvaluationSeasonPeriod() {
        super();
    }

    @Atomic
    static public EvaluationSeasonPeriod create(final ExecutionSemester executionSemester,
            final EvaluationSeasonPeriodType periodType, final EvaluationSeason evaluationSeason,
            final Set<DegreeType> degreeTypes, final LocalDate start, final LocalDate end) {

        final EvaluationSeasonPeriod result = new EvaluationSeasonPeriod();
        result.setExecutionSemester(executionSemester);
        result.setSeason(evaluationSeason);
        result.setOccupationPeriod(new OccupationPeriod(IntervalTools.getInterval(start, end)));
        result.createInitialReferences(degreeTypes, periodType);
        result.checkRules();
        return result;
    }

    private void createInitialReferences(final Set<DegreeType> degreeTypes, final EvaluationSeasonPeriodType periodType) {

        final ExecutionYear executionYear = getExecutionYear();
        final List<ExecutionDegree> executionDegrees = ExecutionDegree.getAllByExecutionYearAndDegreeType(executionYear,
                degreeTypes.toArray(new DegreeType[degreeTypes.size()]));

        createReferences(Sets.newHashSet(executionDegrees), periodType);
    }

    private void createReferences(final Set<ExecutionDegree> executionDegrees, final EvaluationSeasonPeriodType periodType) {
        final int semester = getExecutionSemester().getSemester();
        final CurricularYearList all = CurricularYearList.internalize("-1");

        for (final ExecutionDegree executionDegree : executionDegrees) {

            final OccupationPeriodReference reference =
                    new OccupationPeriodReference(getOccupationPeriod(), executionDegree, periodType.translate(), semester, all);

            // remove all evaluation seasons wrongly deduced by the type given to the constructor...
            // ...and set the correct evaluation season
            reference.getEvaluationSeasonSet().clear();
            reference.addEvaluationSeason(getSeason());
        }
    }

    private void checkRules() {
        checkConsistencySeason();
        checkDuplicates();
    }

    /**
     * All OccupationPeriodReference must have exactly one season
     */
    private void checkConsistencySeason() {
        for (final OccupationPeriodReference reference : getOccupationPeriod().getExecutionDegreesSet()) {
            for (final EvaluationSeason season : reference.getEvaluationSeasonSet()) {
                if (season != getSeason()) {
                    throw new ULisboaSpecificationsDomainException("error.EvaluationSeasonPeriod.evaluationSeason.inconsistent");
                }
            }
        }
    }

    /**
     * For a given ExecutionYear and EvaluationSeasonPeriodType, one OccupationPeriod (considering all of it's Intervals) can only
     * be duplicated if the EvaluationSeason is different
     */
    private void checkDuplicates() {
        for (final EvaluationSeasonPeriod iter : findAll(getExecutionYear(), getPeriodType())) {
            if (iter != this && iter.getSeason() == getSeason()) {

                if (iter.getOccupationPeriod().isEqualTo(getOccupationPeriod())) {
                    throw new ULisboaSpecificationsDomainException("error.EvaluationSeasonPeriod.occupationPeriod.duplicate");
                }
            }
        }
    }

    @Atomic
    public void editDegrees(final Set<ExecutionDegree> executionDegrees) {
        updateReferences(executionDegrees);
        checkRules();
    }

    private void updateReferences(final Set<ExecutionDegree> input) {

        // Step 1, Remove unwanted references
        final Set<OccupationPeriodReference> references = getOccupationPeriod().getExecutionDegreesSet();
        for (final Iterator<OccupationPeriodReference> iterator = references.iterator(); iterator.hasNext();) {
            final OccupationPeriodReference reference = iterator.next();
            final ExecutionDegree executionDegree = reference.getExecutionDegree();

            if (input.contains(executionDegree)) {
                // nothing to be done, existing references will not be updated 
                input.remove(executionDegree);

            } else {
                reference.delete();
                iterator.remove();
            }
        }

        // Step 2, Add new references
        createReferences(input, getPeriodType());
    }

    @Atomic
    public void editIntervals(final Iterator<Interval> intervals) {
        getOccupationPeriod().editDates(intervals);
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

    static public Set<EvaluationSeasonPeriod> findAll(final ExecutionYear executionYear,
            final EvaluationSeasonPeriodType periodType) {

        final Set<EvaluationSeasonPeriod> result = Sets.<EvaluationSeasonPeriod> newHashSet();
        if (executionYear != null && periodType != null) {

            for (final ExecutionSemester semester : executionYear.getExecutionPeriodsSet()) {
                for (final EvaluationSeasonPeriod period : semester.getEvaluationSeasonPeriodSet()) {

                    if (period.getPeriodType() == periodType) {
                        result.add(period);
                    }
                }
            }
        }

        return result;
    }

    @SuppressWarnings("deprecation")
    static public enum EvaluationSeasonPeriodType {

        GRADE_SUBMISSION {

            @Override
            protected OccupationPeriodType translate() {
                // Note: OccupationPeriodType.XPTO_SPECIAL_SEASON is never persisted
                return OccupationPeriodType.GRADE_SUBMISSION;
            }
        },

        EXAMS {

            @Override
            protected OccupationPeriodType translate() {
                // Note: OccupationPeriodType.XPTO_SPECIAL_SEASON is never persisted
                return OccupationPeriodType.EXAMS;
            }
        };

        public LocalizedString getDescriptionI18N() {
            return ULisboaSpecificationsUtil.bundleI18N(name());
        }

        abstract protected OccupationPeriodType translate();

        static protected EvaluationSeasonPeriodType get(final OccupationPeriod input) {
            switch (input.getExecutionDegreesSet().iterator().next().getPeriodType()) {

            case EXAMS:
            case EXAMS_SPECIAL_SEASON:
                return EvaluationSeasonPeriodType.EXAMS;
            case GRADE_SUBMISSION:
            case GRADE_SUBMISSION_SPECIAL_SEASON:
                return EvaluationSeasonPeriodType.GRADE_SUBMISSION;
            default:
                throw new RuntimeException();
            }
        }

    }

    public EvaluationSeasonPeriodType getPeriodType() {
        return EvaluationSeasonPeriodType.get(getOccupationPeriod());
    }

    private ExecutionYear getExecutionYear() {
        return getExecutionSemester().getExecutionYear();
    }

    public boolean isSeason(final EvaluationSeason season) {
        return season != null && season == getSeason();
    }

}
