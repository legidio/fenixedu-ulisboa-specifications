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
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.EvaluationSeason;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.bennu.IBean;
import org.fenixedu.bennu.TupleDataSourceBean;
import org.fenixedu.ulisboa.specifications.domain.evaluation.season.EvaluationSeasonPeriod;
import org.fenixedu.ulisboa.specifications.domain.evaluation.season.EvaluationSeasonPeriod.EvaluationSeasonPeriodType;
import org.fenixedu.ulisboa.specifications.domain.evaluation.season.EvaluationSeasonServices;
import org.joda.time.LocalDate;

import com.google.common.collect.Lists;

import edu.emory.mathcs.backport.java.util.Collections;

public class EvaluationSeasonPeriodBean implements IBean {

    private EvaluationSeasonPeriod period;

    private ExecutionYear executionYear;

    private List<TupleDataSourceBean> executionYearDataSource;

    private EvaluationSeasonPeriodType periodType;

    private List<TupleDataSourceBean> periodTypeDataSource;

    private EvaluationSeason season;

    private List<TupleDataSourceBean> seasonDataSource;

    private ExecutionSemester executionSemester;

    private List<TupleDataSourceBean> executionSemesterDataSource;

    private Set<DegreeType> degreeTypes;

    private List<TupleDataSourceBean> degreeTypesDataSource;

    private LocalDate start;

    private LocalDate end;

    public EvaluationSeasonPeriod getPeriod() {
        return period;
    }

    public void setPeriod(final EvaluationSeasonPeriod input) {
        this.period = input;
    }

    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public List<TupleDataSourceBean> getExecutionYearDataSource() {
        return executionYearDataSource;
    }

    public void setExecutionYearDataSource(final List<TupleDataSourceBean> input) {
        this.executionYearDataSource = input;
    }

    public EvaluationSeasonPeriodType getPeriodType() {
        return periodType;
    }

    public void setPeriodType(final EvaluationSeasonPeriodType input) {
        this.periodType = input;
    }

    public List<TupleDataSourceBean> getPeriodTypeDataSource() {
        return periodTypeDataSource;
    }

    public void setPeriodTypeDataSource(final List<TupleDataSourceBean> input) {
        this.periodTypeDataSource = input;
    }

    public EvaluationSeason getSeason() {
        return season;
    }

    public void setSeason(final EvaluationSeason input) {
        season = input;
    }

    public List<TupleDataSourceBean> getSeasonDataSource() {
        return seasonDataSource;
    }

    public void setSeasonDataSource(final List<TupleDataSourceBean> input) {
        this.seasonDataSource = input;
    }

    public ExecutionSemester getExecutionSemester() {
        return executionSemester;
    }

    public void setExecutionSemester(final ExecutionSemester input) {
        this.executionSemester = input;
    }

    public List<TupleDataSourceBean> getExecutionSemesterDataSource() {
        return executionSemesterDataSource;
    }

    public void setExecutionSemesterDataSource(final List<TupleDataSourceBean> input) {
        this.executionSemesterDataSource = input;
    }

    public Set<DegreeType> getDegreeTypes() {
        return degreeTypes;
    }

    public void setDegreeTypes(final Set<DegreeType> input) {
        this.degreeTypes = input;
    }

    public List<TupleDataSourceBean> getDegreeTypesDataSource() {
        return degreeTypesDataSource;
    }

    public void setDegreeTypesDataSource(final List<TupleDataSourceBean> input) {
        this.degreeTypesDataSource = input;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(final LocalDate input) {
        this.start = input;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(final LocalDate input) {
        this.end = input;
    }

    public EvaluationSeasonPeriodBean() {
        init();
    }

    public EvaluationSeasonPeriodBean(final EvaluationSeasonPeriod period) {
        this();
        setPeriod(period);
    }

    private void init() {
        final List<ExecutionYear> years = ExecutionYear.readOpenExecutionYears().stream()
                .filter(i -> !i.getExecutionDegreesSet().isEmpty()).sorted().collect(Collectors.toList());
        Collections.sort(years, Collections.reverseOrder());

        setExecutionYearDataSource(years.stream()
                .map(l -> new TupleDataSourceBean(((ExecutionYear) l).getExternalId(), ((ExecutionYear) l).getQualifiedName()))
                .collect(Collectors.<TupleDataSourceBean> toList()));

        final List<TupleDataSourceBean> semesters = Lists.newArrayList();
        for (ExecutionYear executionYear : years) {
            for (ExecutionSemester semester : executionYear.getExecutionPeriodsSet()) {
                semesters.add(new TupleDataSourceBean(semester.getExternalId(), semester.getQualifiedName()));
            }
        }
        setExecutionSemesterDataSource(semesters);

        setPeriodTypeDataSource(Arrays.<EvaluationSeasonPeriodType> asList(EvaluationSeasonPeriodType.values()).stream()
                .map(l -> new TupleDataSourceBean(((EvaluationSeasonPeriodType) l).name(),
                        ((EvaluationSeasonPeriodType) l).getDescriptionI18N().getContent()))
                .collect(Collectors.<TupleDataSourceBean> toList()));

        setSeasonDataSource(EvaluationSeason.all().sorted(EvaluationSeasonServices.SEASON_ORDER_COMPARATOR)
                .map(l -> new TupleDataSourceBean(((EvaluationSeason) l).getExternalId(),
                        EvaluationSeasonServices.getDescriptionI18N((EvaluationSeason) l).getContent()))
                .collect(Collectors.<TupleDataSourceBean> toList()));

        setDegreeTypesDataSource(DegreeType.all().sorted()
                .map(l -> new TupleDataSourceBean(((DegreeType) l).getExternalId(), ((DegreeType) l).getName().getContent()))
                .collect(Collectors.<TupleDataSourceBean> toList()));
    }

}
