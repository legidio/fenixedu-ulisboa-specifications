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
package org.fenixedu.ulisboa.specifications.ui.evaluation.managemarksheet.teacher;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.ulisboa.specifications.domain.evaluation.markSheet.CompetenceCourseMarkSheet;
import org.fenixedu.ulisboa.specifications.dto.evaluation.markSheet.CompetenceCourseMarkSheetBean;
import org.fenixedu.ulisboa.specifications.ui.FenixeduUlisboaSpecificationsBaseController;
import org.fenixedu.ulisboa.specifications.ui.FenixeduUlisboaSpecificationsController;
import org.fenixedu.ulisboa.specifications.util.ULisboaSpecificationsUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Component("org.fenixedu.ulisboa.specifications.evaluation.manageMarkSheet.teacher")
@SpringFunctionality(app = FenixeduUlisboaSpecificationsController.class, title = "label.title.evaluation.manageMarkSheet",
        accessGroup = "role(TEACHER)")
@RequestMapping(CompetenceCourseMarkSheetController.CONTROLLER_URL)
public class CompetenceCourseMarkSheetController extends FenixeduUlisboaSpecificationsBaseController {

    public static final String CONTROLLER_URL =
            "/fenixedu-ulisboa-specifications/evaluation/managemarksheet/teacher/competencecoursemarksheet";

    private static final String JSP_PATH = CONTROLLER_URL.substring(1);

    private String jspPage(final String page) {
        return JSP_PATH + "/" + page;
    }

    @RequestMapping
    public String home(final Model model) {
        return "forward:" + CONTROLLER_URL + "/";
    }

    private CompetenceCourseMarkSheetBean getCompetenceCourseMarkSheetBean(final Model model) {
        return (CompetenceCourseMarkSheetBean) model.asMap().get("competenceCourseMarkSheetBean");
    }

    private void setCompetenceCourseMarkSheetBean(final CompetenceCourseMarkSheetBean bean, final Model model) {
        model.addAttribute("competenceCourseMarkSheetBeanJson", getBeanJson(bean));
        model.addAttribute("competenceCourseMarkSheetBean", bean);
    }

    private CompetenceCourseMarkSheet getCompetenceCourseMarkSheet(final Model model) {
        return (CompetenceCourseMarkSheet) model.asMap().get("competenceCourseMarkSheet");
    }

    private void setCompetenceCourseMarkSheet(final CompetenceCourseMarkSheet competenceCourseMarkSheet, final Model model) {
        model.addAttribute("competenceCourseMarkSheet", competenceCourseMarkSheet);
    }

    private static final String _SEARCH_URI = "/";
    public static final String SEARCH_URL = CONTROLLER_URL + _SEARCH_URI;

    @RequestMapping(value = _SEARCH_URI)
    public String search(final Model model) {
        final List<CompetenceCourseMarkSheet> searchResultsDataSet = filterSearch();

        model.addAttribute("searchcompetencecoursemarksheetResultsDataSet", searchResultsDataSet);
        return jspPage("search");
    }

    private Stream<CompetenceCourseMarkSheet> getSearchUniverseSearchDataSet() {
        // TODO
        return new ArrayList<CompetenceCourseMarkSheet>().stream();
    }

    private List<CompetenceCourseMarkSheet> filterSearch() {
        return getSearchUniverseSearchDataSet().collect(Collectors.toList());
    }

    private static final String _SEARCH_TO_VIEW_ACTION_URI = "/search/view/";
    public static final String SEARCH_TO_VIEW_ACTION_URL = CONTROLLER_URL + _SEARCH_TO_VIEW_ACTION_URI;

    @RequestMapping(value = _SEARCH_TO_VIEW_ACTION_URI + "{oid}")
    public String processSearchToViewAction(@PathVariable("oid") final CompetenceCourseMarkSheet competenceCourseMarkSheet,
            final Model model, final RedirectAttributes redirectAttributes) {

        return redirect(READ_URL + competenceCourseMarkSheet.getExternalId(), model, redirectAttributes);
    }

    private static final String _READ_URI = "/read/";
    public static final String READ_URL = CONTROLLER_URL + _READ_URI;

    @RequestMapping(value = _READ_URI + "{oid}")
    public String read(@PathVariable("oid") final CompetenceCourseMarkSheet competenceCourseMarkSheet, final Model model) {
        setCompetenceCourseMarkSheet(competenceCourseMarkSheet, model);
        return jspPage("read");
    }

    private static final String _DELETE_URI = "/delete/";
    public static final String DELETE_URL = CONTROLLER_URL + _DELETE_URI;

    @RequestMapping(value = _DELETE_URI + "{oid}", method = RequestMethod.POST)
    public String delete(@PathVariable("oid") final CompetenceCourseMarkSheet competenceCourseMarkSheet, final Model model,
            final RedirectAttributes redirectAttributes) {

        setCompetenceCourseMarkSheet(competenceCourseMarkSheet, model);
        try {
            competenceCourseMarkSheet.delete();

            addInfoMessage(ULisboaSpecificationsUtil.bundle("label.success.delete"), model);
            return redirect(CONTROLLER_URL, model, redirectAttributes);

        } catch (Exception ex) {
            addErrorMessage(ULisboaSpecificationsUtil.bundle("label.error.delete") + ex.getLocalizedMessage(), model);
        }

        return jspPage("read/" + getCompetenceCourseMarkSheet(model).getExternalId());
    }

    @RequestMapping(value = "/read/{oid}/updateevaluations")
    public String processReadToUpdateEvaluations(@PathVariable("oid") final CompetenceCourseMarkSheet competenceCourseMarkSheet,
            final Model model, final RedirectAttributes redirectAttributes) {
        setCompetenceCourseMarkSheet(competenceCourseMarkSheet, model);

        return redirect(UPDATEEVALUATIONS_URL + getCompetenceCourseMarkSheet(model).getExternalId(), model, redirectAttributes);
    }

    @RequestMapping(value = "/read/{oid}/submitmarksheet")
    public String processReadToSubmitMarkSheet(@PathVariable("oid") final CompetenceCourseMarkSheet competenceCourseMarkSheet,
            final Model model, final RedirectAttributes redirectAttributes) {
        setCompetenceCourseMarkSheet(competenceCourseMarkSheet, model);

        return redirect(READ_URL + getCompetenceCourseMarkSheet(model).getExternalId(), model, redirectAttributes);
    }

    private static final String _UPDATE_URI = "/update/";
    public static final String UPDATE_URL = CONTROLLER_URL + _UPDATE_URI;

    @RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") final CompetenceCourseMarkSheet competenceCourseMarkSheet, final Model model) {
        setCompetenceCourseMarkSheet(competenceCourseMarkSheet, model);

        CompetenceCourseMarkSheetBean bean = new CompetenceCourseMarkSheetBean(competenceCourseMarkSheet);
        this.setCompetenceCourseMarkSheetBean(bean, model);

        return jspPage("update");

    }

    private static final String _UPDATEPOSTBACK_URI = "/updatepostback/";
    public static final String UPDATEPOSTBACK_URL = CONTROLLER_URL + _UPDATEPOSTBACK_URI;

    @RequestMapping(value = _UPDATEPOSTBACK_URI + "{oid}", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public @ResponseBody ResponseEntity<String> updatepostback(
            @PathVariable("oid") final CompetenceCourseMarkSheet competenceCourseMarkSheet,
            @RequestParam(value = "bean", required = false) final CompetenceCourseMarkSheetBean bean, final Model model) {

        this.setCompetenceCourseMarkSheetBean(bean, model);
        return new ResponseEntity<String>(getBeanJson(bean), HttpStatus.OK);
    }

    @RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.POST)
    public String update(@PathVariable("oid") final CompetenceCourseMarkSheet competenceCourseMarkSheet,
            @RequestParam(value = "bean", required = false) final CompetenceCourseMarkSheetBean bean, final Model model,
            final RedirectAttributes redirectAttributes) {
        setCompetenceCourseMarkSheet(competenceCourseMarkSheet, model);

        try {
//
//            competenceCourseMarkSheet.edit(bean.getRectified(), bean.getEvaluationSeason(), bean.getEvaluationDate(),
//                    bean.getCheckSum(), bean.getPrinted());

            return redirect(READ_URL + getCompetenceCourseMarkSheet(model).getExternalId(), model, redirectAttributes);
        } catch (Exception de) {

            addErrorMessage(ULisboaSpecificationsUtil.bundle("label.error.update") + de.getLocalizedMessage(), model);
            setCompetenceCourseMarkSheet(competenceCourseMarkSheet, model);
            this.setCompetenceCourseMarkSheetBean(bean, model);

            return jspPage("update");
        }
    }

    private static final String _CREATE_URI = "/create";
    public static final String CREATE_URL = CONTROLLER_URL + _CREATE_URI;

    @RequestMapping(value = _CREATE_URI, method = RequestMethod.GET)
    public String create(final Model model) {
        final CompetenceCourseMarkSheetBean bean = new CompetenceCourseMarkSheetBean();
        this.setCompetenceCourseMarkSheetBean(bean, model);

        return jspPage("create");
    }

    private static final String _CREATEPOSTBACK_URI = "/createpostback";
    public static final String CREATEPOSTBACK_URL = CONTROLLER_URL + _CREATEPOSTBACK_URI;

    @RequestMapping(value = _CREATEPOSTBACK_URI, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody ResponseEntity<String> createpostback(
            @RequestParam(value = "bean", required = false) final CompetenceCourseMarkSheetBean bean, final Model model) {

        this.setCompetenceCourseMarkSheetBean(bean, model);
        return new ResponseEntity<String>(getBeanJson(bean), HttpStatus.OK);
    }

    @RequestMapping(value = _CREATE_URI, method = RequestMethod.POST)
    public String create(@RequestParam(value = "bean", required = false) final CompetenceCourseMarkSheetBean bean,
            final Model model, final RedirectAttributes redirectAttributes) {

        try {
            final CompetenceCourseMarkSheet competenceCourseMarkSheet =
                    CompetenceCourseMarkSheet.create(bean.getEvaluationSeason(), bean.getEvaluationDate());

            model.addAttribute("competenceCourseMarkSheet", competenceCourseMarkSheet);
            return redirect(UPDATEEVALUATIONS_URL + getCompetenceCourseMarkSheet(model).getExternalId(), model,
                    redirectAttributes);

        } catch (Exception de) {

            addErrorMessage(ULisboaSpecificationsUtil.bundle("label.error.create") + de.getLocalizedMessage(), model);
            this.setCompetenceCourseMarkSheetBean(bean, model);
            return jspPage("create");
        }
    }

    private static final String _UPDATEEVALUATIONS_URI = "/updateevaluations/";
    public static final String UPDATEEVALUATIONS_URL = CONTROLLER_URL + _UPDATEEVALUATIONS_URI;

    @RequestMapping(value = _UPDATEEVALUATIONS_URI + "{oid}", method = RequestMethod.GET)
    public String updateevaluations(@PathVariable("oid") final CompetenceCourseMarkSheet competenceCourseMarkSheet,
            final Model model) {
        setCompetenceCourseMarkSheet(competenceCourseMarkSheet, model);

        CompetenceCourseMarkSheetBean bean = new CompetenceCourseMarkSheetBean(competenceCourseMarkSheet);
        this.setCompetenceCourseMarkSheetBean(bean, model);

        return jspPage("updateevaluations");
    }

    private static final String _UPDATEEVALUATIONSPOSTBACK_URI = "/updateevaluationspostback/";
    public static final String UPDATEEVALUATIONSPOSTBACK_URL = CONTROLLER_URL + _UPDATEEVALUATIONSPOSTBACK_URI;

    @RequestMapping(value = _UPDATEEVALUATIONSPOSTBACK_URI + "{oid}", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public @ResponseBody ResponseEntity<String> updateevaluationspostback(
            @PathVariable("oid") final CompetenceCourseMarkSheet competenceCourseMarkSheet,
            @RequestParam(value = "bean", required = false) final CompetenceCourseMarkSheetBean bean, final Model model) {

        this.setCompetenceCourseMarkSheetBean(bean, model);
        return new ResponseEntity<String>(getBeanJson(bean), HttpStatus.OK);
    }

    @RequestMapping(value = _UPDATEEVALUATIONS_URI + "{oid}", method = RequestMethod.POST)
    public String updateevaluations(@PathVariable("oid") final CompetenceCourseMarkSheet competenceCourseMarkSheet,
            @RequestParam(value = "bean", required = false) final CompetenceCourseMarkSheetBean bean, final Model model,
            final RedirectAttributes redirectAttributes) {
        setCompetenceCourseMarkSheet(competenceCourseMarkSheet, model);

        try {
            // TODO
            competenceCourseMarkSheet.editEvaluations();

            return redirect(READ_URL + getCompetenceCourseMarkSheet(model).getExternalId(), model, redirectAttributes);
        } catch (Exception de) {
            addErrorMessage(ULisboaSpecificationsUtil.bundle("label.error.update") + de.getLocalizedMessage(), model);
            setCompetenceCourseMarkSheet(competenceCourseMarkSheet, model);
            this.setCompetenceCourseMarkSheetBean(bean, model);

            return jspPage("updateevaluations");
        }
    }

}
