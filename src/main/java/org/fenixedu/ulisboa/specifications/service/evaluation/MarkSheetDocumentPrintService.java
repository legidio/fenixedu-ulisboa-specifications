package org.fenixedu.ulisboa.specifications.service.evaluation;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.qubdocs.util.reports.helpers.DateHelper;
import org.fenixedu.qubdocs.util.reports.helpers.EnumerationHelper;
import org.fenixedu.qubdocs.util.reports.helpers.LanguageHelper;
import org.fenixedu.qubdocs.util.reports.helpers.MoneyHelper;
import org.fenixedu.qubdocs.util.reports.helpers.NumbersHelper;
import org.fenixedu.qubdocs.util.reports.helpers.StringsHelper;
import org.fenixedu.ulisboa.specifications.domain.evaluation.config.MarkSheetSettings;
import org.fenixedu.ulisboa.specifications.domain.evaluation.markSheet.CompetenceCourseMarkSheet;
import org.joda.time.DateTime;

import com.google.common.collect.Lists;
import com.qubit.terra.docs.core.DocumentGenerator;
import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IFieldsExporter;
import com.qubit.terra.docs.util.IReportDataProvider;

public class MarkSheetDocumentPrintService {

    public static class CompetenceCourseMarkSheetDataProvider implements IReportDataProvider {

        private static final String MARK_SHEET_KEY = "markSheet";

        private static final String INSTITUTION_NAME_KEY = "institutionName";

        private static final String MARK_SHEET_EVALUATIONS_KEY = "markSheetEvaluations";

        private static final String CURRENT_DATE_TIME_KEY = "currentDateTime";

        private CompetenceCourseMarkSheet competenceCourseMarkSheet;

        public static class EvaluationLine {

            private Integer studentNumber;

            private String studentName;

            private String grade;

            private String statutes;

            private EvaluationLine(Integer studentNumber, String studentName, String grade, String statutes) {
                super();
                this.studentNumber = studentNumber;
                this.studentName = studentName;
                this.grade = grade;
                this.statutes = statutes;
            }

            public Integer getStudentNumber() {
                return studentNumber;
            }

            public String getStudentName() {
                return studentName;
            }

            public String getGrade() {
                return grade;
            }

            public String getStatutes() {
                return statutes;
            }

            public void setStudentNumber(Integer studentNumber) {
                this.studentNumber = studentNumber;
            }

            public void setStudentName(String studentName) {
                this.studentName = studentName;
            }

            public void setGrade(String grade) {
                this.grade = grade;
            }

            public void setStatutes(String statutes) {
                this.statutes = statutes;
            }

        }

        public CompetenceCourseMarkSheetDataProvider(CompetenceCourseMarkSheet competenceCourseMarkSheet) {
            this.competenceCourseMarkSheet = competenceCourseMarkSheet;
        }

        @Override
        public boolean handleKey(String key) {
            return key.equals(MARK_SHEET_KEY) || key.equals(INSTITUTION_NAME_KEY) || key.equals(MARK_SHEET_EVALUATIONS_KEY)
                    || key.equals(CURRENT_DATE_TIME_KEY);
        }

        @Override
        public Object valueForKey(String key) {

            if (key.equals(MARK_SHEET_KEY)) {
                return competenceCourseMarkSheet;
            }

            if (key.equals(INSTITUTION_NAME_KEY)) {
                return Bennu.getInstance().getInstitutionUnit().getNameI18n();
            }

            if (key.equals(MARK_SHEET_EVALUATIONS_KEY)) {

                //TODO: finish
                final List<EvaluationLine> example = Lists.newArrayList();
                for (int i = 1; i < 100; i++) {
                    example.add(new EvaluationLine(i, "teste " + i, "10", (i % 2 == 0 ? "Estatuto 1, Estatuto 2" : "")));
                }

                return example;
            }

            if (key.equals(CURRENT_DATE_TIME_KEY)) {
                return new DateTime();
            }

            return null;

        }

        @Override
        public void registerFieldsAndImages(IDocumentFieldsData arg0) {
            arg0.registerCollectionAsField(MARK_SHEET_EVALUATIONS_KEY);
        }

        @Override
        public void registerFieldsMetadata(IFieldsExporter arg0) {

        }

    }

    public static final String PDF = DocumentGenerator.PDF;

    private static void registerHelpers(DocumentGenerator generator) {
        generator.registerHelper("dates", new DateHelper());
        generator.registerHelper("lang", new LanguageHelper());
        generator.registerHelper("numbers", new NumbersHelper());
        generator.registerHelper("enumeration", new EnumerationHelper());
        generator.registerHelper("strings", new StringsHelper());
        generator.registerHelper("money", new MoneyHelper());
    }

    public static byte[] print(CompetenceCourseMarkSheet competenceCourseMarkSheet) {

        competenceCourseMarkSheet.markAsPrinted();

        final DocumentGenerator generator = DocumentGenerator.create(
                new ByteArrayInputStream(MarkSheetSettings.getInstance().getTemplateFile().getContent()), DocumentGenerator.PDF);

        registerHelpers(generator);
        generator.registerDataProvider(new CompetenceCourseMarkSheetDataProvider(competenceCourseMarkSheet));

        return generator.generateReport();
    }

}
