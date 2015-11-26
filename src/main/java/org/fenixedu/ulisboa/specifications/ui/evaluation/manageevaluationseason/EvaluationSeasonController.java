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
package org.fenixedu.ulisboa.specifications.ui.evaluation.manageevaluationseason;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.EvaluationSeason;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.ulisboa.specifications.dto.evaluation.EvaluationSeasonBean;
import org.fenixedu.ulisboa.specifications.ui.FenixeduUlisboaSpecificationsBaseController;
import org.fenixedu.ulisboa.specifications.ui.FenixeduUlisboaSpecificationsController;
import org.fenixedu.ulisboa.specifications.util.ULisboaSpecificationsUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;

@SpringFunctionality(app = FenixeduUlisboaSpecificationsController.class, title = "label.title.evaluation.manageEvaluationSeason",
        accessGroup = "logged")
@RequestMapping(EvaluationSeasonController.CONTROLLER_URL)
public class EvaluationSeasonController extends FenixeduUlisboaSpecificationsBaseController {

    public static final String CONTROLLER_URL =
            "/fenixedu-ulisboa-specifications/evaluation/manageevaluationseason/evaluationseason";

    private static final String JSP_PATH = CONTROLLER_URL.substring(1);

    private String jspPage(final String page) {
        return JSP_PATH + "/" + page;
    }

    @RequestMapping
    public String home(final Model model) {
        return "forward:" + CONTROLLER_URL + "/";
    }

    private EvaluationSeasonBean getEvaluationSeasonBean(final Model model) {
        return (EvaluationSeasonBean) model.asMap().get("evaluationSeasonBean");
    }

    private void setEvaluationSeasonBean(final EvaluationSeasonBean bean, final Model model) {
        model.addAttribute("evaluationSeasonBeanJson", getBeanJson(bean));
        model.addAttribute("evaluationSeasonBean", bean);
    }

    private EvaluationSeason getEvaluationSeason(final Model model) {
        return (EvaluationSeason) model.asMap().get("evaluationSeason");
    }

    private void setEvaluationSeason(final EvaluationSeason evaluationSeason, final Model model) {
        model.addAttribute("evaluationSeason", evaluationSeason);
    }

    @Atomic
    public void deleteEvaluationSeason(final EvaluationSeason evaluationSeason) {
        // CHANGE_ME: Do the processing for deleting the evaluationSeason
        // Do not catch any exception here

        // evaluationSeason.delete();
    }

    private static final String _SEARCH_URI = "/";
    public static final String SEARCH_URL = CONTROLLER_URL + _SEARCH_URI;

    @RequestMapping(value = _SEARCH_URI)
    public String search(@RequestParam(value = "name", required = false) LocalizedString name,
            @RequestParam(value = "active", required = false) Boolean active, final Model model) {
        List<EvaluationSeason> searchevaluationseasonResultsDataSet = filterSearchEvaluationSeason(name, active);

        model.addAttribute("searchevaluationseasonResultsDataSet", searchevaluationseasonResultsDataSet);
        return jspPage("search");
    }

    private Stream<EvaluationSeason> getSearchUniverseSearchEvaluationSeasonDataSet() {
        return EvaluationSeason.all();
    }

    private List<EvaluationSeason> filterSearchEvaluationSeason(LocalizedString name, Boolean active) {

        return getSearchUniverseSearchEvaluationSeasonDataSet()
                .filter(evaluationSeason -> name == null || name.isEmpty() || name.getLocales().stream()
                        .allMatch(locale -> evaluationSeason.getName().getContent(locale) != null && evaluationSeason.getName()
                                .getContent(locale).toLowerCase().contains(name.getContent(locale).toLowerCase())))
                                .filter(evaluationSeason -> active == null || active.equals(evaluationSeason.getInformation().getActive()))
                .collect(Collectors.toList());
    }

    private static final String _SEARCH_TO_VIEW_ACTION_URI = "/search/view/";
    public static final String SEARCH_TO_VIEW_ACTION_URL = CONTROLLER_URL + _SEARCH_TO_VIEW_ACTION_URI;

    @RequestMapping(value = _SEARCH_TO_VIEW_ACTION_URI + "{oid}")
    public String processSearchToViewAction(@PathVariable("oid") final EvaluationSeason evaluationSeason, final Model model,
            final RedirectAttributes redirectAttributes) {

        return redirect(READ_URL + evaluationSeason.getExternalId(), model, redirectAttributes);
    }

    private static final String _READ_URI = "/read/";
    public static final String READ_URL = CONTROLLER_URL + _READ_URI;

    @RequestMapping(value = _READ_URI + "{oid}")
    public String read(@PathVariable("oid") final EvaluationSeason evaluationSeason, final Model model) {
        setEvaluationSeason(evaluationSeason, model);
        setEvaluationSeasonBean(new EvaluationSeasonBean(evaluationSeason), model);
        return jspPage("read");
    }

    private static final String _DELETE_URI = "/delete/";
    public static final String DELETE_URL = CONTROLLER_URL + _DELETE_URI;

    @RequestMapping(value = _DELETE_URI + "{oid}", method = RequestMethod.POST)
    public String delete(@PathVariable("oid") final EvaluationSeason evaluationSeason, final Model model,
            final RedirectAttributes redirectAttributes) {

        setEvaluationSeason(evaluationSeason, model);
        try {
            deleteEvaluationSeason(evaluationSeason);

            addInfoMessage(ULisboaSpecificationsUtil.bundle("label.success.delete") + " EvaluationSeason", model);
            return redirect(CONTROLLER_URL, model, redirectAttributes);

        } catch (Exception ex) {
            addErrorMessage(ULisboaSpecificationsUtil.bundle("label.error.delete") + ex.getLocalizedMessage(), model);
        }

        return jspPage("read/") + getEvaluationSeason(model).getExternalId();
    }

    @RequestMapping(value = "/read/{oid}/readrules")
    public String processReadToReadRules(@PathVariable("oid") final EvaluationSeason evaluationSeason, final Model model,
            final RedirectAttributes redirectAttributes) {
        setEvaluationSeason(evaluationSeason, model);
//
        /* Put here the logic for processing Event readRules 	*/
        //doSomething();

        // Now choose what is the Exit Screen	 
        return redirect("/fenixedu-ulisboa-specifications/evaluation/manageevaluationseasonrule/evaluationseasonrule//"
                + getEvaluationSeason(model).getExternalId(), model, redirectAttributes);
    }

    private static final String _UPDATE_URI = "/update/";
    public static final String UPDATE_URL = CONTROLLER_URL + _UPDATE_URI;

    @RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") final EvaluationSeason evaluationSeason, final Model model) {
        setEvaluationSeason(evaluationSeason, model);
        
        final EvaluationSeasonBean bean = new EvaluationSeasonBean(evaluationSeason);
        this.setEvaluationSeasonBean(bean, model);

        return jspPage("update");

    }

    private static final String _UPDATEPOSTBACK_URI = "/updatepostback/";
    public static final String UPDATEPOSTBACK_URL = CONTROLLER_URL + _UPDATEPOSTBACK_URI;

    @RequestMapping(value = _UPDATEPOSTBACK_URI + "{oid}", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public @ResponseBody ResponseEntity<String> updatepostback(@PathVariable("oid") final EvaluationSeason evaluationSeason,
            @RequestParam(value = "bean", required = false) final EvaluationSeasonBean bean, final Model model) {

        // Do validation logic ?!?!
        //if (something_wrong){
        //                 return new ResponseEntity<String>(<MESSAGE_FROM_BUNDLE>,HttpStatus.BAD_REQUEST);
        //}
        this.setEvaluationSeasonBean(bean, model);
        return new ResponseEntity<String>(getBeanJson(bean), HttpStatus.OK);
    }

    @RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.POST)
    public String update(@PathVariable("oid") final EvaluationSeason evaluationSeason,
            @RequestParam(value = "bean", required = false) final EvaluationSeasonBean bean, final Model model,
            final RedirectAttributes redirectAttributes) {
        setEvaluationSeason(evaluationSeason, model);

        try {
            /*
            *  UpdateLogic here
            */

            //	updateEvaluationSeason( .. get fields from bean..., model);

            /*Succes Update */

            return redirect(READ_URL + getEvaluationSeason(model).getExternalId(), model, redirectAttributes);
        } catch (Exception de) {

            /*
            * If there is any error in validation 
            *
            * Add a error / warning message
            * 
            * addErrorMessage(ULisboaSpecificationsUtil.bundle( "label.error.update") + de.getLocalizedMessage(),model);
            * addWarningMessage(ULisboaSpecificationsUtil.bundle( "label.error.update")  + de.getLocalizedMessage(),model);
            */

            addErrorMessage(ULisboaSpecificationsUtil.bundle("label.error.update") + de.getLocalizedMessage(), model);
            setEvaluationSeason(evaluationSeason, model);
            this.setEvaluationSeasonBean(bean, model);

            return jspPage("update");
        }
    }

    @Atomic
    public void updateEvaluationSeason(LocalizedString name, String code, LocalizedString acronym, Boolean active, boolean normal,
            boolean improvement, boolean special, boolean specialAuthorization, Boolean requiresEnrolmentEvaluation,
            final Model model) {

        // @formatter: off				
        /*
         * Modify the update code here if you do not want to update
         * the object with the default setter for each field
         */

        // CHANGE_ME It's RECOMMENDED to use "Edit service" in DomainObject
        //getEvaluationSeason(model).edit(fields_to_edit);

        //Instead, use individual SETTERS and validate "CheckRules" in the end
        // @formatter: on
        
        getEvaluationSeason(model).setName(name);
        getEvaluationSeason(model).setCode(code);
        getEvaluationSeason(model).setAcronym(acronym);
        //TODO:
//        getEvaluationSeason(model).setActive(active);
        getEvaluationSeason(model).setNormal(normal);
        getEvaluationSeason(model).setImprovement(improvement);
        getEvaluationSeason(model).setSpecial(special);
        getEvaluationSeason(model).setSpecialAuthorization(specialAuthorization);
        //TODO:
//        getEvaluationSeason(model).setRequiresEnrolmentEvaluation(requiresEnrolmentEvaluation);
    }

//				
    private static final String _CREATE_URI = "/create";
    public static final String CREATE_URL = CONTROLLER_URL + _CREATE_URI;

    @RequestMapping(value = _CREATE_URI, method = RequestMethod.GET)
    public String create(final Model model) {

        final EvaluationSeasonBean bean = new EvaluationSeasonBean();
        this.setEvaluationSeasonBean(bean, model);

        return jspPage("create");
    }

    private static final String _CREATEPOSTBACK_URI = "/createpostback";
    public static final String CREATEPOSTBACK_URL = CONTROLLER_URL + _CREATEPOSTBACK_URI;

    @RequestMapping(value = _CREATEPOSTBACK_URI, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody ResponseEntity<String> createpostback(
            @RequestParam(value = "bean", required = false) final EvaluationSeasonBean bean, final Model model) {

        // Do validation logic ?!?!
        //if (something_wrong){
        //                 return new ResponseEntity<String>(<MESSAGE_FROM_BUNDLE>,HttpStatus.BAD_REQUEST);
        //}
        this.setEvaluationSeasonBean(bean, model);
        return new ResponseEntity<String>(getBeanJson(bean), HttpStatus.OK);
    }

    @RequestMapping(value = _CREATE_URI, method = RequestMethod.POST)
    public String create(@RequestParam(value = "name", required = false) LocalizedString name,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "acronym", required = false) LocalizedString acronym,
            @RequestParam(value = "active", required = false) Boolean active,
            @RequestParam(value = "normal", required = false) boolean normal,
            @RequestParam(value = "improvement", required = false) boolean improvement,
            @RequestParam(value = "special", required = false) boolean special,
            @RequestParam(value = "specialauthorization", required = false) boolean specialAuthorization,
            @RequestParam(value = "requiresenrolmentevaluation", required = false) Boolean requiresEnrolmentEvaluation,
            final Model model, final RedirectAttributes redirectAttributes) {

        try {

            final EvaluationSeason evaluationSeason = createEvaluationSeason(name, code, acronym, active, normal, improvement,
                    special, specialAuthorization, requiresEnrolmentEvaluation);

            //Success Validation
            //Add the bean to be used in the View
            model.addAttribute("evaluationSeason", evaluationSeason);
            return redirect(jspPage("read/") + getEvaluationSeason(model).getExternalId(), model, redirectAttributes);
        } catch (Exception de) {

            // @formatter: off
            /*
             * If there is any error in validation 
             *
             * Add a error / warning message
             * 
             * addErrorMessage(ULisboaSpecificationsUtil.bundle( "label.error.create") + de.getLocalizedMessage(),model);
             * addWarningMessage(ULisboaSpecificationsUtil.bundle( "label.error.create") + de.getLocalizedMessage(),model); */
            // @formatter: on

            addErrorMessage(ULisboaSpecificationsUtil.bundle("label.error.create") + de.getLocalizedMessage(), model);
            return create(model);
        }
    }

    @Atomic
    public EvaluationSeason createEvaluationSeason(LocalizedString name, String code, LocalizedString acronym, Boolean active,
            boolean normal, boolean improvement, boolean special, boolean specialAuthorization,
            Boolean requiresEnrolmentEvaluation) {

        // @formatter: off

        /*
         * Modify the creation code here if you do not want to create
         * the object with the default constructor and use the setter
         * for each field
         * 
         */

        // CHANGE_ME It's RECOMMENDED to use "Create service" in DomainObject
        //final EvaluationSeason evaluationSeason = evaluationSeason.create(fields_to_create);

        //Instead, use individual SETTERS and validate "CheckRules" in the end
        // @formatter: on

        final EvaluationSeason evaluationSeason = new EvaluationSeason();
        evaluationSeason.setName(name);
        evaluationSeason.setCode(code);
        evaluationSeason.setAcronym(acronym);
        //TODO:
        //evaluationSeason.setActive(active);
        evaluationSeason.setNormal(normal);
        evaluationSeason.setImprovement(improvement);
        evaluationSeason.setSpecial(special);
        evaluationSeason.setSpecialAuthorization(specialAuthorization);
        //TODO:
        //evaluationSeason.setRequiresEnrolmentEvaluation(requiresEnrolmentEvaluation);

        return evaluationSeason;
    }

}
