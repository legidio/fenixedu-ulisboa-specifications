/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: xpto@qub-it.com
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

package org.fenixedu.ulisboa.specifications.dto.evaluation.season;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.EvaluationSeason;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.GradeScale;
import org.fenixedu.bennu.IBean;
import org.fenixedu.bennu.TupleDataSourceBean;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.ulisboa.specifications.domain.evaluation.season.EvaluationSeasonServices;
import org.fenixedu.ulisboa.specifications.domain.evaluation.season.rule.EvaluationSeasonRule;

public class EvaluationSeasonRuleBean implements IBean {

    private EvaluationSeason season;

    private String gradeValue;

    private GradeScale gradeScale;

    public EvaluationSeason getSeason() {
        return season;
    }

    public void setSeason(EvaluationSeason evaluationSeason) {
        season = evaluationSeason;
    }

    public String getGradeValue() {
        return gradeValue;
    }

    public void setGradeValue(String gradeValue) {
        this.gradeValue = gradeValue;
    }

    public GradeScale getGradeScale() {
        return gradeScale;
    }

    public void setGradeScale(GradeScale gradeScale) {
        this.gradeScale = gradeScale;
    }

    public EvaluationSeasonRuleBean() {

    }

    public EvaluationSeasonRuleBean(EvaluationSeasonRule evaluationSeasonRule) {
        this.setSeason(evaluationSeasonRule.getSeason());
    }

    public EvaluationSeasonRuleBean(EvaluationSeason evaluationSeason) {
        this.setSeason(evaluationSeason);
    }

    public List<TupleDataSourceBean> getGradeScaleDataSource() {
        return Arrays.<GradeScale> asList(GradeScale.values()).stream()
                .map(l -> new TupleDataSourceBean(((GradeScale) l).name(), ((GradeScale) l).getDescription()))
                .collect(Collectors.<TupleDataSourceBean> toList());
    }

    public LocalizedString getSeasonLocalizedStringI18N() {
        return EvaluationSeasonServices.getDescriptionI18N(getSeason());
    }

    public Grade getGrade() {
        return Grade.createGrade(getGradeValue(), getGradeScale());
    }

}
