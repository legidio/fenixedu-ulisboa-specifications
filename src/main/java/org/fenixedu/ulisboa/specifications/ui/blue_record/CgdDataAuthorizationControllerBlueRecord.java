package org.fenixedu.ulisboa.specifications.ui.blue_record;

import java.util.Optional;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.fenixedu.ulisboa.specifications.domain.student.access.StudentAccessServices;
import org.fenixedu.ulisboa.specifications.ui.firstTimeCandidacy.misc.CgdDataAuthorizationController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@BennuSpringController(value = BlueRecordEntryPoint.class)
@RequestMapping(CgdDataAuthorizationControllerBlueRecord.CONTROLLER_URL)
public class CgdDataAuthorizationControllerBlueRecord extends CgdDataAuthorizationController {

    public static final String CONTROLLER_URL =
            "/fenixedu-ulisboa-specifications/blueRecord/{executionYearId}/cgddataauthorization";

    @Override
    public String back(@PathVariable("executionYearId") final ExecutionYear executionYear, final Model model,
            final RedirectAttributes redirectAttributes) {
        addControllerURLToModel(executionYear, model);
        String url = MotivationsExpectationsFormControllerBlueRecord.CONTROLLER_URL;
        return redirect(urlWithExecutionYear(url, executionYear), model, redirectAttributes);
    }

    @Override
    public String cgddataauthorization(@PathVariable("executionYearId") final ExecutionYear executionYear, final Model model,
            final RedirectAttributes redirectAttributes) {
        addControllerURLToModel(executionYear, model);
        if (isFormIsFilled(executionYear, model)) {
            return nextScreen(executionYear, model, redirectAttributes);
        }

        return "fenixedu-ulisboa-specifications/firsttimecandidacy/cgddataauthorization";
    }

    protected String nextScreen(final ExecutionYear executionYear, final Model model,
            final RedirectAttributes redirectAttributes) {
        return redirect(urlWithExecutionYear(BlueRecordEnd.CONTROLLER_URL, executionYear), model, redirectAttributes);
    }

    @Override
    protected String nextScreen(final ExecutionYear executionYear, final Model model, final RedirectAttributes redirectAttributes,
            final boolean wsCallSuccess) {
        return redirect(urlWithExecutionYear(BlueRecordEnd.CONTROLLER_URL, executionYear), model, redirectAttributes);
    }

    @Override
    @RequestMapping(value = "/authorize")
    public String cgddataauthorizationToAuthorize(@PathVariable("executionYearId") final ExecutionYear executionYear,
            final Model model, final RedirectAttributes redirectAttributes) {
        addControllerURLToModel(executionYear, model);
        if (isFormIsFilled(executionYear, model)) {
            return nextScreen(executionYear, model, redirectAttributes);
        }

        authorizeSharingDataWithCGD(true);

        final Registration registration = findFirstTimeRegistration(executionYear);

        boolean wsCallSuccess;
        try {
            wsCallSuccess = StudentAccessServices.triggerSyncRegistrationToExternal(registration);
        } catch (Exception e) {
            wsCallSuccess = false;
        }

        if (wsCallSuccess) {
            return nextScreen(executionYear, model, redirectAttributes);
        } else {
            final String url =
                    String.format("/fenixedu-ulisboa-specifications/blueRecord/%s/cgddataauthorization/showmodelo43download/true",
                            executionYear.getExternalId());

            return redirect(url, model, redirectAttributes);
        }
    }

    @Override
    @RequestMapping(value = "/unauthorize")
    public String cgddataauthorizationToUnauthorize(@PathVariable("executionYearId") final ExecutionYear executionYear,
            final Model model, final RedirectAttributes redirectAttributes) {
        addControllerURLToModel(executionYear, model);
        if (isFormIsFilled(executionYear, model)) {
            return nextScreen(executionYear, model, redirectAttributes);
        }

        authorizeSharingDataWithCGD(false);

        final String url =
                String.format("/fenixedu-ulisboa-specifications/blueRecord/%s/cgddataauthorization/showmodelo43download/false",
                        executionYear.getExternalId());

        return redirect(url, model, redirectAttributes);
    }

    @Override
    protected String getPrintURL(final ExecutionYear executionYear) {
        return String.format("/fenixedu-ulisboa-specifications/blueRecord/%s/cgddataauthorization/printmodelo43",
                executionYear.getExternalId());
    }

    @Override
    protected String getControllerURL() {
        return CONTROLLER_URL;
    }

    @Override
    public boolean isFormIsFilled(final ExecutionYear executionYear, final Student student) {
        return super.isFormIsFilled(executionYear, student);
    }

    @Override
    public Optional<String> accessControlRedirect(final ExecutionYear executionYear, final Model model,
            final RedirectAttributes redirectAttributes) {
        return Optional.empty();
    }

}
