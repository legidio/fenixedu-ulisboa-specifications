/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: joao.roxo@qub-it.com 
 *               nuno.pinheiro@qub-it.com
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
package org.fenixedu.ulisboa.specifications.ui.firstTimeCandidacy;

import static org.fenixedu.bennu.FenixeduUlisboaSpecificationsSpringConfiguration.BUNDLE;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.treasury.ITuitionTreasuryEvent;
import org.fenixedu.academic.domain.treasury.TreasuryBridgeAPIFactory;
import org.fenixedu.academictreasury.ui.customer.CustomerAccountingController;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.fenixedu.ulisboa.specifications.ui.FenixeduUlisboaSpecificationsBaseController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;

@BennuSpringController(value = FirstTimeCandidacyController.class)
@RequestMapping(ShowTuitionController.CONTROLLER_URL)
public class ShowTuitionController extends FenixeduUlisboaSpecificationsBaseController {

    public static final String CONTROLLER_URL = "/fenixedu-ulisboa-specifications/firsttimecandidacy/showtuition";

    @RequestMapping(value = "/back", method = RequestMethod.GET)
    public String back(Model model, RedirectAttributes redirectAttributes) {
        if (ShowScheduledClassesController.shouldBeSkipped()) {
            return redirect(ShowSelectedCoursesController.CONTROLLER_URL, model, redirectAttributes);
        }
        return redirect(ShowScheduledClassesController.CONTROLLER_URL, model, redirectAttributes);
    }

    @RequestMapping
    public String showtuition(Model model, RedirectAttributes redirectAttributes) {
        if (!FirstTimeCandidacyController.isPeriodOpen()) {
            return redirect(FirstTimeCandidacyController.CONTROLLER_URL, model, redirectAttributes);
        }
        Registration registration = FirstTimeCandidacyController.getCandidacy().getRegistration();

        //Temporary Fix: due to asyncronous calls that create tuition debts from rules in the academic-treasury
        // it is necessary to invoke the tuition creation twice, in two diferent transactions.
        createTuitions(registration);
        createTuitions(registration);

        try {
            checkTuitions(registration);
        } catch (Exception ex) {
            ex.printStackTrace();
            addErrorMessage(BundleUtil.getString(BUNDLE, "error.could.not.create.tuitions"), model);
        }
        CustomerAccountingController customerAccountingController = new CustomerAccountingController();
        customerAccountingController.readCustomer(model, redirectAttributes);

        addInfoMessage(BundleUtil.getString(BUNDLE, "label.firstTimeCandidacy.showTuition.info"), model);
        return "fenixedu-ulisboa-specifications/firsttimecandidacy/showtuition";
    }

    private void checkTuitions(Registration registration) {
        ITuitionTreasuryEvent event =
                TreasuryBridgeAPIFactory.implementation().getTuitionForRegistrationTreasuryEvent(registration,
                        ExecutionYear.readCurrentExecutionYear());
        if (event == null) {
            throw new RuntimeException("Tuitions are not properly configured!");
        }
    }

    @Atomic
    private void createTuitions(Registration registration) {
        TreasuryBridgeAPIFactory.implementation().createAcademicDebts(registration);
    }

    @RequestMapping(value = "/continue")
    public String showtuitionToContinue(Model model, RedirectAttributes redirectAttributes) {
        if (!FirstTimeCandidacyController.isPeriodOpen()) {
            return redirect(FirstTimeCandidacyController.CONTROLLER_URL, model, redirectAttributes);
        }
        return redirect(CgdDataAuthorizationController.CONTROLLER_URL, model, redirectAttributes);
    }
}
