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

package org.fenixedu.ulisboa.specifications.dto.evaluation.markSheet;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.EvaluationSeason;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.bennu.IBean;
import org.fenixedu.bennu.TupleDataSourceBean;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.ulisboa.specifications.domain.evaluation.markSheet.CompetenceCourseMarkSheet;
import org.fenixedu.ulisboa.specifications.domain.evaluation.markSheet.CompetenceCourseMarkSheetStateChange;
import org.joda.time.LocalDate;

import com.google.common.collect.Sets;

public class CompetenceCourseMarkSheetBean implements IBean {

    private EvaluationSeason evaluationSeason;
    private List<TupleDataSourceBean> evaluationSeasonDataSource;
    private LocalDate evaluationDate;

    private List<CompetenceCourseMarkSheetStateChange> stateChange;
    private List<TupleDataSourceBean> stateChangeDataSource;
    private String reason;

    private ExecutionSemester executionSemester;
    private List<TupleDataSourceBean> executionSemesterDataSource;

    private CompetenceCourse competenceCourse;
    private List<TupleDataSourceBean> competenceCourseDataSource;

    private Person certifier;
    private List<TupleDataSourceBean> certifierDataSource;

    private Set<Shift> shifts;
    private List<TupleDataSourceBean> shiftsDataSource;

    public EvaluationSeason getEvaluationSeason() {
        return evaluationSeason;
    }

    public void setEvaluationSeason(EvaluationSeason value) {
        evaluationSeason = value;
    }

    public List<TupleDataSourceBean> getEvaluationSeasonDataSource() {
        return evaluationSeasonDataSource;
    }

    public void setEvaluationSeasonDataSource(List<EvaluationSeason> value) {
        this.evaluationSeasonDataSource =
                value.stream().sorted((x, y) -> x.getName().getContent().compareTo(y.getName().getContent())).map(x ->
                {
                    TupleDataSourceBean tuple = new TupleDataSourceBean();
                    tuple.setId(x.getExternalId());
                    tuple.setText(x.getName().getContent());
                    return tuple;
                }).collect(Collectors.toList());
    }

    public LocalDate getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(LocalDate evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public List<CompetenceCourseMarkSheetStateChange> getStateChange() {
        return stateChange;
    }

    public void setStateChange(List<CompetenceCourseMarkSheetStateChange> value) {
        stateChange = value;
    }

    public List<TupleDataSourceBean> getStateChangeDataSource() {
        return stateChangeDataSource;
    }

    public void setStateChangeDataSource(List<CompetenceCourseMarkSheetStateChange> value) {
        this.stateChangeDataSource = value.stream().map(x ->
        {
            TupleDataSourceBean tuple = new TupleDataSourceBean();
            tuple.setId(x.getExternalId()); //CHANGE_ME
            tuple.setText(x.toString()); //CHANGE_ME
            return tuple;
        }).collect(Collectors.toList());
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ExecutionSemester getExecutionSemester() {
        return executionSemester;
    }

    public List<TupleDataSourceBean> getExecutionSemesterDataSource() {
        return executionSemesterDataSource;
    }

    public void setExecutionSemester(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

    public void setExecutionSemesterDataSource(List<ExecutionSemester> value) {
        this.executionSemesterDataSource = value.stream().map(x ->
        {
            TupleDataSourceBean tuple = new TupleDataSourceBean();
            tuple.setId(x.getExternalId());
            tuple.setText(x.getQualifiedName());

            return tuple;

        }).collect(Collectors.toList());
    }

    public CompetenceCourse getCompetenceCourse() {
        return competenceCourse;
    }

    public List<TupleDataSourceBean> getCompetenceCourseDataSource() {
        return competenceCourseDataSource;
    }

    public void setCompetenceCourse(CompetenceCourse competenceCourse) {
        this.competenceCourse = competenceCourse;
    }

    public void setCompetenceCourseDataSource(List<CompetenceCourse> value) {
        this.competenceCourseDataSource = value.stream().sorted(CompetenceCourse.COMPETENCE_COURSE_COMPARATOR_BY_NAME).map(x ->
        {
            TupleDataSourceBean tuple = new TupleDataSourceBean();
            tuple.setId(x.getExternalId());
            tuple.setText(x.getCode() + " - " + (x.getName().replace("'", " ").replace("\"", " ")));

            return tuple;

        }).collect(Collectors.toList());
    }

    public Person getCertifier() {
        return certifier;
    }

    public List<TupleDataSourceBean> getCertifierDataSource() {
        return certifierDataSource;
    }

    public void setCertifier(Person certifier) {
        this.certifier = certifier;
    }

    public void setCertifierDataSource(List<Person> value) {

        final Set<Person> competenceCourseTeachers = Sets.newHashSet();
        if (getCompetenceCourse() != null && getExecutionSemester() != null) {
            competenceCourseTeachers
                    .addAll(getCompetenceCourse().getExecutionCoursesByExecutionPeriod(getExecutionSemester()).stream()
                            .flatMap(e -> e.getProfessorshipsSet().stream()).map(p -> p.getPerson()).collect(Collectors.toSet()));
        }

        this.certifierDataSource = value.stream().sorted(Person.COMPARATOR_BY_NAME).map(x ->
        {
            TupleDataSourceBean tuple = new TupleDataSourceBean();
            tuple.setId(x.getExternalId());
            tuple.setText(
                    (competenceCourseTeachers.contains(x) ? "* " : "") + x.getFirstAndLastName() + " (" + x.getUsername() + ")");

            return tuple;

        }).collect(Collectors.toList());
    }

    public Set<Shift> getShifts() {
        return shifts;
    }

    public void setShifts(Set<Shift> shifts) {
        this.shifts = shifts;
    }

    public List<TupleDataSourceBean> getShiftsDataSource() {
        return shiftsDataSource;
    }

    public void setShiftsDataSource(List<Shift> value) {
        this.shiftsDataSource = value.stream().sorted(Shift.SHIFT_COMPARATOR_BY_NAME).map(x ->
        {
            TupleDataSourceBean tuple = new TupleDataSourceBean();
            tuple.setId(x.getExternalId());
            tuple.setText(x.getNome());

            return tuple;

        }).collect(Collectors.toList());
    }

    public CompetenceCourseMarkSheetBean() {
        update();
    }

    public void update() {
        setExecutionSemesterDataSource(ExecutionSemester.readNotClosedExecutionPeriods().stream()
                .sorted(ExecutionSemester.COMPARATOR_BY_BEGIN_DATE.reversed()).collect(Collectors.toList()));

        setCompetenceCourseDataSource(getExecutionSemester() != null ? getExecutionSemester().getAssociatedExecutionCoursesSet()
                .stream().flatMap(e -> e.getCompetenceCourses().stream()).collect(Collectors.toList()) : Collections.EMPTY_LIST);

        setEvaluationSeasonDataSource(EvaluationSeason.all().filter(e -> e.getInformation().getActive())
                .sorted((x, y) -> x.getName().getContent().compareTo(y.getName().getContent())).collect(Collectors.toList()));

        setCertifierDataSource(Bennu.getInstance().getTeachersSet().stream().map(t -> t.getPerson())
                .sorted(Person.COMPARATOR_BY_NAME).collect(Collectors.toList()));

        setShiftsDataSource(getCompetenceCourse() != null ? getCompetenceCourse()
                .getExecutionCoursesByExecutionPeriod(getExecutionSemester()).stream()
                .flatMap(e -> e.getAssociatedShifts().stream()).collect(Collectors.toList()) : Collections.EMPTY_LIST);

    }

    public CompetenceCourseMarkSheetBean(CompetenceCourseMarkSheet competenceCourseMarkSheet) {
        setEvaluationDate(competenceCourseMarkSheet.getEvaluationDate());
        setEvaluationSeason(competenceCourseMarkSheet.getEvaluationSeason());
        setExecutionSemester(competenceCourseMarkSheet.getExecutionSemester());
        setCompetenceCourse(competenceCourseMarkSheet.getCompetenceCourse());
        setCertifier(competenceCourseMarkSheet.getCertifier());
        setShifts(Sets.newHashSet(competenceCourseMarkSheet.getShiftSet()));
        
        update();
    }

}
