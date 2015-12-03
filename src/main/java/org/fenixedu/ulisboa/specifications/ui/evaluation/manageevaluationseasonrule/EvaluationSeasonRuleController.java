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
package org.fenixedu.ulisboa.specifications.ui.evaluation.manageevaluationseasonrule;

import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.EvaluationSeason;
import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.fenixedu.ulisboa.specifications.domain.evaluation.EvaluationSeasonRule;
import org.fenixedu.ulisboa.specifications.dto.evaluation.EvaluationSeasonRuleBean;
import org.fenixedu.ulisboa.specifications.ui.FenixeduUlisboaSpecificationsBaseController;
import org.fenixedu.ulisboa.specifications.ui.evaluation.manageevaluationseason.EvaluationSeasonController;
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

@BennuSpringController(value = EvaluationSeasonController.class)
@RequestMapping(EvaluationSeasonRuleController.CONTROLLER_URL)
public class EvaluationSeasonRuleController extends FenixeduUlisboaSpecificationsBaseController {

    public static final String CONTROLLER_URL =
            "/fenixedu-ulisboa-specifications/evaluation/manageevaluationseasonrule/evaluationseasonrule";

    private static final String JSP_PATH = CONTROLLER_URL.substring(1);

    private String jspPage(final String page) {
        return JSP_PATH + "/" + page;
    }

    @RequestMapping
    public String home(final Model model) {
        return "forward:" + CONTROLLER_URL + "/";
    }

    private EvaluationSeasonRuleBean getEvaluationSeasonRuleBean(final Model model) {
        return (EvaluationSeasonRuleBean) model.asMap().get("evaluationSeasonRuleBean");
    }

    private void setEvaluationSeasonRuleBean(final EvaluationSeasonRuleBean bean, final Model model) {
        model.addAttribute("evaluationSeasonRuleBeanJson", getBeanJson(bean));
        model.addAttribute("evaluationSeasonRuleBean", bean);
    }

    private EvaluationSeasonRule getEvaluationSeasonRule(final Model model) {
        return (EvaluationSeasonRule) model.asMap().get("evaluationSeasonRule");
    }

    private void setEvaluationSeasonRule(final EvaluationSeasonRule evaluationSeasonRule, final Model model) {
        model.addAttribute("evaluationSeasonRule", evaluationSeasonRule);
    }
    
    private static final String _SEARCH_URI = "/";
    public static final String SEARCH_URL = CONTROLLER_URL + _SEARCH_URI;

    @RequestMapping(value = _SEARCH_URI + "{oid}")
    public String search(@PathVariable("oid") final EvaluationSeason evaluationSeason, final Model model) {
        final EvaluationSeasonRuleBean bean = new EvaluationSeasonRuleBean(evaluationSeason);
        this.setEvaluationSeasonRuleBean(bean, model);
        
        List<EvaluationSeasonRule> searchevaluationseasonruleResultsDataSet = filterSearchEvaluationSeasonRule(evaluationSeason);

        model.addAttribute("searchevaluationseasonruleResultsDataSet", searchevaluationseasonruleResultsDataSet);
        return jspPage("search");
    }

    private List<EvaluationSeasonRule> filterSearchEvaluationSeasonRule(final EvaluationSeason evaluationSeason) {
        return evaluationSeason.getRulesSet().stream().collect(Collectors.toList());
    }

    private static final String _SEARCH_TO_UPDATERULE_URI = "/search/updaterule/";
    public static final String _TO_UPDATERULE_URL = CONTROLLER_URL + _SEARCH_TO_UPDATERULE_URI;

    @RequestMapping(value = _SEARCH_TO_UPDATERULE_URI)
    public String processSearchToUpdateRule(final Model model, final RedirectAttributes redirectAttributes) {
        // TODO
        return redirect("/specifications/evaluation/manageevaluationseasonrule/evaluationseasonrule/update", model,
                redirectAttributes);
    }

    private static final String _SEARCH_TO_DELETERULE_URI = "/search/deleterule/";
    public static final String _TO_DELETERULE_URL = CONTROLLER_URL + _SEARCH_TO_DELETERULE_URI;

    @RequestMapping(value = _SEARCH_TO_DELETERULE_URI)
    public String processSearchToDeleteRule(final Model model, final RedirectAttributes redirectAttributes) {
        // TODO
        return redirect("/specifications/evaluation/manageevaluationseasonrule/evaluationseasonrule/", model, redirectAttributes);
    }

    private static final String _UPDATE_URI = "/update/";
    public static final String UPDATE_URL = CONTROLLER_URL + _UPDATE_URI;

    @RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") final EvaluationSeasonRule evaluationSeasonRule, final Model model) {
        setEvaluationSeasonRule(evaluationSeasonRule, model);

        final EvaluationSeasonRuleBean bean = new EvaluationSeasonRuleBean(evaluationSeasonRule);
        this.setEvaluationSeasonRuleBean(bean, model);

        return jspPage("update");

    }

    private static final String _UPDATEPOSTBACK_URI = "/updatepostback/";
    public static final String UPDATEPOSTBACK_URL = CONTROLLER_URL + _UPDATEPOSTBACK_URI;

    @RequestMapping(value = _UPDATEPOSTBACK_URI + "{oid}", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public @ResponseBody ResponseEntity<String> updatepostback(
            @PathVariable("oid") final EvaluationSeasonRule evaluationSeasonRule,
            @RequestParam(value = "bean", required = false) final EvaluationSeasonRuleBean bean, final Model model) {

        this.setEvaluationSeasonRuleBean(bean, model);
        return new ResponseEntity<String>(getBeanJson(bean), HttpStatus.OK);
    }

    @RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.POST)
    public String update(@PathVariable("oid") final EvaluationSeasonRule evaluationSeasonRule,
            @RequestParam(value = "bean", required = false) final EvaluationSeasonRuleBean bean, final Model model,
            RedirectAttributes redirectAttributes) {
        setEvaluationSeasonRule(evaluationSeasonRule, model);

        try {
            EvaluationSeasonRule.edit();

            return redirect(SEARCH_URL + getEvaluationSeasonRule(model).getExternalId(), model, redirectAttributes);
        } catch (Exception de) {

            addErrorMessage(ULisboaSpecificationsUtil.bundle("label.error.update") + de.getLocalizedMessage(), model);
            setEvaluationSeasonRule(evaluationSeasonRule, model);
            this.setEvaluationSeasonRuleBean(bean, model);

            return jspPage("update");
        }
    }

    private static final String _CREATE_URI = "/create/";
    public static final String CREATE_URL = CONTROLLER_URL + _CREATE_URI;

    @RequestMapping(value = _CREATE_URI + "{oid}", method = RequestMethod.GET)
    public String create(@PathVariable("oid") final EvaluationSeason evaluationSeason, final Model model) {

        final EvaluationSeasonRuleBean bean = new EvaluationSeasonRuleBean(evaluationSeason);
        this.setEvaluationSeasonRuleBean(bean, model);

        return jspPage("create");
    }

    private static final String _CREATEPOSTBACK_URI = "/createpostback/";
    public static final String CREATEPOSTBACK_URL = CONTROLLER_URL + _CREATEPOSTBACK_URI;

    @RequestMapping(value = _CREATEPOSTBACK_URI, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody ResponseEntity<String> createpostback(
            @RequestParam(value = "bean", required = false) final EvaluationSeasonRuleBean bean, final Model model) {

        this.setEvaluationSeasonRuleBean(bean, model);
        return new ResponseEntity<String>(getBeanJson(bean), HttpStatus.OK);
    }

    @RequestMapping(value = _CREATE_URI, method = RequestMethod.POST)
    public String create(@RequestParam(value = "bean", required = false) final EvaluationSeasonRuleBean bean, final Model model,
            RedirectAttributes redirectAttributes) {

        try {

            final EvaluationSeasonRule evaluationSeasonRule = EvaluationSeasonRule.create(bean.getName());

            model.addAttribute("evaluationSeasonRule", evaluationSeasonRule);
            return redirect(SEARCH_URL + getEvaluationSeasonRule(model).getExternalId(), model, redirectAttributes);
        } catch (Exception de) {

            addErrorMessage(ULisboaSpecificationsUtil.bundle("label.error.create") + de.getLocalizedMessage(), model);
            this.setEvaluationSeasonRuleBean(bean, model);
            return jspPage("create");
        }
    }

}
