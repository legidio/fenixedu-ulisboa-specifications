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

import java.util.stream.Collectors;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.bennu.IBean;

public class MarkBean implements IBean, Comparable<MarkBean> {

    private Enrolment enrolment;
    private Integer studentNumber;
    private String studentName;
    private String gradeValue;
    private String degreeName;
    private String shifts;

    public MarkBean(final Enrolment enrolment) {
        setEnrolment(enrolment);

        final Student student = enrolment.getStudent();
        this.studentNumber = student.getNumber();
        this.studentName = student.getPerson().getFirstAndLastName();
        this.degreeName = enrolment.getStudentCurricularPlan().getDegree().getPresentationName();
        this.shifts = enrolment.getRegistration().getShiftEnrolmentsSet().stream().map(i -> i.getShift().getNome())
                .collect(Collectors.joining(", "));
    }

    public Enrolment getEnrolment() {
        return enrolment;
    }

    public void setEnrolment(final Enrolment input) {
        this.enrolment = input;
    }

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getGradeValue() {
        return gradeValue;
    }

    public void setGradeValue(String gradeValue) {
        this.gradeValue = gradeValue;
    }

    public String getDegreeName() {
        return degreeName;
    }

    public void setDegreeName(String degreeName) {
        this.degreeName = degreeName;
    }

    public String getShifts() {
        return shifts;
    }

    public void setShifts(String shifts) {
        this.shifts = shifts;
    }

    @Override
    public int compareTo(final MarkBean o) {
        // TODO legidio
        return 0;
    }

}
