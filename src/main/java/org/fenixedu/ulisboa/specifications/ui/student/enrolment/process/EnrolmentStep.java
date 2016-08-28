package org.fenixedu.ulisboa.specifications.ui.student.enrolment.process;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.IBean;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.ulisboa.specifications.dto.enrolmentperiod.AcademicEnrolmentPeriodBean;
import org.fenixedu.ulisboa.specifications.util.ULisboaSpecificationsUtil;
import org.springframework.web.servlet.HandlerMapping;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class EnrolmentStep implements IBean {

    private LocalizedString description;
    private String entryPointURL;

    private EnrolmentProcess process;
    private EnrolmentStep next;
    private EnrolmentStep previous;

    protected EnrolmentStep(final LocalizedString description, final String entryPointURL) {
        this.description = description;
        this.entryPointURL = entryPointURL;
    }

    protected EnrolmentStep(final AcademicEnrolmentPeriodBean input) {
        this(buildDescription(input), input.getEntryPointURL());
    }

    static private LocalizedString buildDescription(final AcademicEnrolmentPeriodBean input) {
        final LocalizedString.Builder builder = new LocalizedString.Builder();
        builder.append(ULisboaSpecificationsUtil.bundleI18N("label.AcademicEnrolmentPeriod.type"));
        builder.append(" ");
        builder.append(input.getEnrolmentPeriodType().getDescriptionI18N());
        return builder.build();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EnrolmentStep other = (EnrolmentStep) obj;
        if (!getDescription().getContent().equals(other.getDescription().getContent())) {
            // legidio, LocalizedString equals seems to fail somehow on it's internal map equals
            return false;
        }
        // legidio, no need to check for same entry URL:
        // we actually want to guarantee exactly only one type of step on each process
        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + getDescription().getContent().hashCode();
        return result;
    }

    public EnrolmentProcess getProcess() {
        return process;
    }

    protected void setProcess(final EnrolmentProcess input) {
        this.process = input;
    }

    public LocalizedString getDescription() {
        return description;
    }

    public EnrolmentStep getNext() {
        return next;
    }

    protected void setNext(final EnrolmentStep input) {
        this.next = input;
    }

    public EnrolmentStep getPrevious() {
        return previous;
    }

    protected void setPrevious(final EnrolmentStep input) {
        this.previous = input;
    }

    /**
     * Call the paramedics before looking into this.
     */
    public boolean isEntryPointURL(final HttpServletRequest request) {

        // 1. the hacking goal
        final List<String> secrets = Lists.newArrayList();

        // 2. the hacking sources
        final ActionMapping mappingStruts = (ActionMapping) request.getAttribute(Globals.MAPPING_KEY);
        final String mappingSpring = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        // TODO legidio, ui layer major hacking....

        // 3. the hacking parsing
        if (mappingStruts != null) {
            // struts

            String method = "";
            if (getEntryPointURL().contains("method")) {
                final String queryString = request.getQueryString();

                if (Strings.isNullOrEmpty(queryString)) {
                    // these are desperate times.
                    method = ".do?method=prepare";

                } else {
                    final int beginIndex = queryString.indexOf("method=");
                    final int end = queryString.contains("&") ? queryString.indexOf("&") : queryString.length();
                    method = ".do?" + queryString.substring(beginIndex, end);
                }
            }

            secrets.add(mappingStruts.getPath() + method);
            if (!method.equals(".do?method=prepare")) {
                // one last try, without method...but only when we aren't invoking a prepare method
                secrets.add(mappingStruts.getPath());
            }

        } else if (!Strings.isNullOrEmpty(mappingSpring)) {
            // spring

            String aux = mappingSpring;

            if (mappingSpring.contains("{")) {
                aux = aux.substring(0, aux.indexOf("/{"));
            }

            if (StringUtils.countMatches(aux, "/") > 1) {
                // these are desperate times.
                // trying to remove the method name
                aux = aux.substring(0, aux.lastIndexOf("/"));
            }

            secrets.add(aux);

        } else if (true) {
            // ui layer

            // TODO legidio
            // secrets.add("");
        }

        // 4. "it's the final count down..."
        return !secrets.isEmpty() && secrets.stream().anyMatch(i -> getEntryPointURL().contains(i));
    }

    public String getEntryPointURL() {
        return this.entryPointURL;
    }

}
