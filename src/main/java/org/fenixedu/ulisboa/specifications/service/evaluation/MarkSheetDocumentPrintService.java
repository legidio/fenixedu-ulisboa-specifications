package org.fenixedu.ulisboa.specifications.service.evaluation;

import java.io.ByteArrayInputStream;

import org.fenixedu.qubdocs.util.reports.helpers.DateHelper;
import org.fenixedu.qubdocs.util.reports.helpers.EnumerationHelper;
import org.fenixedu.qubdocs.util.reports.helpers.LanguageHelper;
import org.fenixedu.qubdocs.util.reports.helpers.MoneyHelper;
import org.fenixedu.qubdocs.util.reports.helpers.NumbersHelper;
import org.fenixedu.qubdocs.util.reports.helpers.StringsHelper;
import org.fenixedu.ulisboa.specifications.domain.evaluation.config.MarkSheetSettings;
import org.fenixedu.ulisboa.specifications.domain.evaluation.markSheet.CompetenceCourseMarkSheet;

import com.qubit.terra.docs.core.DocumentGenerator;
import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IFieldsExporter;
import com.qubit.terra.docs.util.IReportDataProvider;

public class MarkSheetDocumentPrintService {

    public static class CompetenceCourseMarkSheetDataProvider implements IReportDataProvider {

        private static final String KEY = "markSheet";

        private CompetenceCourseMarkSheet competenceCourseMarkSheet;

        public CompetenceCourseMarkSheetDataProvider(CompetenceCourseMarkSheet competenceCourseMarkSheet) {
            this.competenceCourseMarkSheet = competenceCourseMarkSheet;
        }

        @Override
        public boolean handleKey(String key) {
            return key.equals(KEY);
        }

        @Override
        public Object valueForKey(String key) {
            return competenceCourseMarkSheet;
        }

        @Override
        public void registerFieldsAndImages(IDocumentFieldsData arg0) {

        }

        @Override
        public void registerFieldsMetadata(IFieldsExporter arg0) {

        }

    }

    private static void registerHelpers(DocumentGenerator generator) {
        generator.registerHelper("dates", new DateHelper());
        generator.registerHelper("lang", new LanguageHelper());
        generator.registerHelper("numbers", new NumbersHelper());
        generator.registerHelper("enumeration", new EnumerationHelper());
        generator.registerHelper("strings", new StringsHelper());
        generator.registerHelper("money", new MoneyHelper());
    }

    public static byte[] print(CompetenceCourseMarkSheet competenceCourseMarkSheet) {

        final DocumentGenerator generator = DocumentGenerator.create(
                new ByteArrayInputStream(MarkSheetSettings.getInstance().getTemplateFile().getContent()), DocumentGenerator.PDF);

        registerHelpers(generator);
        generator.registerDataProvider(new CompetenceCourseMarkSheetDataProvider(competenceCourseMarkSheet));

        return generator.generateReport();
    }

}
