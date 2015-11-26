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

import org.fenixedu.academic.domain.EvaluationSeason;
import org.fenixedu.ulisboa.specifications.domain.exceptions.ULisboaSpecificationsDomainException;

import pt.ist.fenixframework.Atomic;

public class EvaluationSeasonInformation extends EvaluationSeasonInformation_Base {

    protected EvaluationSeasonInformation() {
        super();
    }

    protected void init(final EvaluationSeason evaluationSeason, final Integer seasonOrder, final Boolean active,
            final Boolean requiresEnrolmentEvaluation) {
        setSeason(evaluationSeason);
        setSeasonOrder(seasonOrder);
        setActive(active);
        setRequiresEnrolmentEvaluation(requiresEnrolmentEvaluation);
        checkRules();
    }

    private void checkRules() {
        if (getSeason() == null) {
            throw new ULisboaSpecificationsDomainException("error.EvaluationSeasonInformation.evaluationSeason.required");
        }
    }

    @Atomic
    public void edit(final EvaluationSeason evaluationSeason, final Integer seasonOrder, final Boolean active,
            final Boolean requiresEnrolmentEvaluation) {
        setSeason(evaluationSeason);
        setSeasonOrder(seasonOrder);
        setActive(active);
        setRequiresEnrolmentEvaluation(requiresEnrolmentEvaluation);
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
    public static EvaluationSeasonInformation create(final EvaluationSeason evaluationSeason, final Integer seasonOrder,
            final Boolean active, final Boolean requiresEnrolmentEvaluation) {
        EvaluationSeasonInformation evaluationSeasonInformation = new EvaluationSeasonInformation();
        evaluationSeasonInformation.init(evaluationSeason, seasonOrder, active, requiresEnrolmentEvaluation);
        return evaluationSeasonInformation;
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

}
