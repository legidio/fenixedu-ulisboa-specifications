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

import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.EvaluationSeason;
import org.fenixedu.bennu.IBean;
import org.fenixedu.bennu.TupleDataSourceBean;
import org.fenixedu.ulisboa.specifications.domain.evaluation.markSheet.CompetenceCourseMarkSheet;
import org.fenixedu.ulisboa.specifications.domain.evaluation.markSheet.CompetenceCourseMarkSheetStateChange;

public class CompetenceCourseMarkSheetBean implements IBean {

    private CompetenceCourseMarkSheet rectified;
    private List<TupleDataSourceBean> rectifiedDataSource;
    private EvaluationSeason evaluationSeason;
    private List<TupleDataSourceBean> evaluationSeasonDataSource;
    private org.joda.time.DateTime evaluationDate;
    private java.lang.String checkSum;
    private boolean printed;

    private List<CompetenceCourseMarkSheetStateChange> stateChange;
    private List<TupleDataSourceBean> stateChangeDataSource;
    private String reason;

    public CompetenceCourseMarkSheet getRectified() {
        return rectified;
    }

    public void setRectified(CompetenceCourseMarkSheet value) {
        rectified = value;
    }

    public List<TupleDataSourceBean> getRectifiedDataSource() {
        return rectifiedDataSource;
    }

    public void setRectifiedDataSource(List<CompetenceCourseMarkSheet> value) {
        this.rectifiedDataSource = value.stream().map(x -> {
            TupleDataSourceBean tuple = new TupleDataSourceBean();
            tuple.setId(x.getExternalId()); //CHANGE_ME
            tuple.setText(x.toString()); //CHANGE_ME
            return tuple;
        }).collect(Collectors.toList());
    }

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
        this.evaluationSeasonDataSource = value.stream().map(x -> {
            TupleDataSourceBean tuple = new TupleDataSourceBean();
            tuple.setId(x.getExternalId()); //CHANGE_ME
            tuple.setText(x.toString()); //CHANGE_ME
            return tuple;
        }).collect(Collectors.toList());
    }

    public org.joda.time.DateTime getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(org.joda.time.DateTime value) {
        evaluationDate = value;
    }

    public java.lang.String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(java.lang.String value) {
        checkSum = value;
    }

    public boolean getPrinted() {
        return printed;
    }

    public void setPrinted(boolean value) {
        printed = value;
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
        this.stateChangeDataSource = value.stream().map(x -> {
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

    public CompetenceCourseMarkSheetBean() {

    }

    public CompetenceCourseMarkSheetBean(CompetenceCourseMarkSheet competenceCourseMarkSheet) {
        this.setEvaluationSeason(competenceCourseMarkSheet.getEvaluationSeason());
        this.setEvaluationDate(competenceCourseMarkSheet.getEvaluationDate());
        this.setCheckSum(competenceCourseMarkSheet.getCheckSum());
        this.setPrinted(competenceCourseMarkSheet.getPrinted());
        this.setEvaluationDate(competenceCourseMarkSheet.getEvaluationDate());
        this.setCheckSum(competenceCourseMarkSheet.getCheckSum());
        this.setPrinted(competenceCourseMarkSheet.getPrinted());
    }

}
