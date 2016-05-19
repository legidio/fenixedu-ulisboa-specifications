/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 *
 * 
 * This file is part of FenixEdu fenixedu-ulisboa-specifications.
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
package org.fenixedu.ulisboa.specifications.domain.curricularRules.executors.ruleExecutors;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.curricularRules.CurricularRule;
import org.fenixedu.academic.domain.curricularRules.CurricularRuleNotPersistent;
import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.curricularRules.ICurricularRule.CurricularRuleExecutorFinder;
import org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors.CurricularRuleExecutor;
import org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors.CurricularRuleExecutor.CurricularRuleApprovalExecutor;
import org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors.CurricularRuleExecutorFactory;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;
import org.fenixedu.ulisboa.specifications.ULisboaConfiguration;
import org.fenixedu.ulisboa.specifications.domain.CompetenceCourseServices;
import org.fenixedu.ulisboa.specifications.domain.curricularRules.CurriculumAggregatorApproval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract public class CurricularRuleExecutorInitializer {

    static private final Logger logger = LoggerFactory.getLogger(CurricularRuleExecutorInitializer.class);

    private static Map<Class<? extends ICurricularRule>, CurricularRuleExecutor> executors =
            new HashMap<Class<? extends ICurricularRule>, CurricularRuleExecutor>();

    static public void init() {

        registerCurricularRuleExecutors();

        CurricularRule.setCurricularRuleExecutorFinder(CURRICULAR_RULE_EXECUTOR_FINDER);
        CurricularRuleNotPersistent.setCurricularRuleExecutorFinder(CURRICULAR_RULE_EXECUTOR_FINDER);
        logger.info("CurricularRuleExecutorFinder: Overriding default");

        CurricularRuleExecutor.setCurricularRuleApprovalExecutor(CURRICULAR_RULE_APPROVAL_EXECUTOR);
        logger.info("CurricularRuleApprovalExecutor: Overriding default");
    }

    static final private void registerCurricularRuleExecutors() {
        put(CurriculumAggregatorApproval.class, new CurriculumAggregatorApprovalExecutor());
    }

    static public CurricularRuleExecutor put(final Class<? extends ICurricularRule> clazz,
            final CurricularRuleExecutor curricularRuleExecutor) {
        if (!executors.containsKey(clazz)) {
            executors.put(clazz, curricularRuleExecutor);
        }

        return curricularRuleExecutor;
    }

    static private Supplier<CurricularRuleExecutorFinder> CURRICULAR_RULE_EXECUTOR_FINDER =
            () -> new CurricularRuleExecutorFinder() {

                @Override
                public CurricularRuleExecutor find(final ICurricularRule input) {
                    return findExecutor(input);
                }
            };

    static private CurricularRuleExecutor findExecutor(final ICurricularRule curricularRule) {
        return findExecutor(curricularRule.getClass());
    }

    static private CurricularRuleExecutor findExecutor(final Class<? extends ICurricularRule> clazz) {
        CurricularRuleExecutor result = executors.get(clazz);
        if (result == null) {
            result = CurricularRuleExecutorFactory.findExecutor(clazz);
        }

        return result;
    }

    static private Supplier<CurricularRuleApprovalExecutor> CURRICULAR_RULE_APPROVAL_EXECUTOR =
            () -> new CurricularRuleApprovalExecutor() {

                @Override
                public boolean isApproved(final EnrolmentContext enrolmentContext, final CurricularCourse curricularCourse) {

                    final StudentCurricularPlan plan = enrolmentContext.getStudentCurricularPlan();
                    if (ULisboaConfiguration.getConfiguration().getCurricularRulesApprovalsAwareOfCompetenceCourse()) {

                        return CompetenceCourseServices.isCompetenceCourseApproved(plan, curricularCourse);

                    } else {
                        return plan.isApproved(curricularCourse);
                    }
                }

                @Override
                public boolean isApproved(final EnrolmentContext enrolmentContext, final CurricularCourse curricularCourse,
                        final ExecutionSemester executionSemester) {

                    final StudentCurricularPlan plan = enrolmentContext.getStudentCurricularPlan();

                    if (ULisboaConfiguration.getConfiguration().getCurricularRulesApprovalsAwareOfCompetenceCourse()) {

                        return CompetenceCourseServices.isCompetenceCourseApproved(plan, curricularCourse, executionSemester);

                    } else {
                        return plan.isApproved(curricularCourse, executionSemester);
                    }
                }
            };

    public static CurricularRuleExecutor register(final Class<? extends ICurricularRule> clazz,
            final CurricularRuleExecutor curricularRuleExecutor) {
        if (!executors.containsKey(clazz)) {
            executors.put(clazz, curricularRuleExecutor);
        }

        return curricularRuleExecutor;
    }

}
