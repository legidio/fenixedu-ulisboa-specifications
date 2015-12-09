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
import org.fenixedu.ulisboa.specifications.domain.evaluation.season.rule.PreviousSeasonBlockingGrade;
import org.fenixedu.ulisboa.specifications.domain.evaluation.season.rule.PreviousSeasonMinimumGrade;
import org.fenixedu.ulisboa.specifications.util.ULisboaSpecificationsUtil;

public class EvaluationSeasonRuleBean implements IBean {

    private EvaluationSeason season;

    String evaluationSeasonRuleSubclass;

    private String gradeValue;

    private GradeScale gradeScale;

    private List<TupleDataSourceBean> gradeScaleDataSource;

    public EvaluationSeason getSeason() {
        return season;
    }

    public void setSeason(EvaluationSeason evaluationSeason) {
        season = evaluationSeason;
    }

    public String getEvaluationSeasonRuleSubclass() {
        return this.evaluationSeasonRuleSubclass;
    }

    public void setEvaluationSeasonRuleSubclass(final String input) {
        this.evaluationSeasonRuleSubclass = input;
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

    public List<TupleDataSourceBean> getGradeScaleDataSource() {
        return gradeScaleDataSource;
    }

    public void setGradeScaleDataSource(List<TupleDataSourceBean> gradeScaleDataSource) {
        this.gradeScaleDataSource = gradeScaleDataSource;
    }

    public EvaluationSeasonRuleBean() {
        init();
    }

    public EvaluationSeasonRuleBean(final EvaluationSeason season, final Class<? extends EvaluationSeasonRule> clazz) {
        this();
        this.setSeason(season);
        this.setEvaluationSeasonRuleSubclass(clazz == null ? null : clazz.getSimpleName());
    }

    public EvaluationSeasonRuleBean(final EvaluationSeasonRule input) {
        this(input.getSeason(), input.getClass());
    }

    public EvaluationSeasonRuleBean(final PreviousSeasonBlockingGrade input) {
        this((EvaluationSeasonRule) input);

        init(input.getBlocking());
    }

    public EvaluationSeasonRuleBean(final PreviousSeasonMinimumGrade input) {
        this((EvaluationSeasonRule) input);

        init(input.getMinimum());
    }

    private void init(final Grade grade) {
        if (grade != null) {
            setGradeValue(grade.getValue());
            setGradeScale(grade.getGradeScale());
        }
    }

    private void init() {
        this.gradeScaleDataSource = Arrays.<GradeScale> asList(GradeScale.values()).stream()
                .map(l -> new TupleDataSourceBean(((GradeScale) l).name(), ((GradeScale) l).getDescription()))
                .collect(Collectors.<TupleDataSourceBean> toList());
    }

    public LocalizedString getDescriptionI18N() {
        return ULisboaSpecificationsUtil.bundleI18N(getEvaluationSeasonRuleSubclass());
    }

    public LocalizedString getSeasonDescriptionI18N() {
        return EvaluationSeasonServices.getDescriptionI18N(getSeason());
    }

    public Grade getGrade() {
        try {
            return Grade.createGrade(getGradeValue(), getGradeScale());
        } catch (final Throwable t) {
            return Grade.createEmptyGrade();
        }
    }

}
