<%@page import="org.fenixedu.ulisboa.specifications.ui.servicerequestslot.ServiceRequestSlotController"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<spring:url var="datatablesUrl" value="/javaScript/dataTables/media/js/jquery.dataTables.latest.min.js"/>
<spring:url var="datatablesBootstrapJsUrl" value="/javaScript/dataTables/media/js/jquery.dataTables.bootstrap.min.js"></spring:url>
<script type="text/javascript" src="${datatablesUrl}"></script>
<script type="text/javascript" src="${datatablesBootstrapJsUrl}"></script>
<spring:url var="datatablesCssUrl" value="/CSS/dataTables/dataTables.bootstrap.min.css"/>

<link rel="stylesheet" href="${datatablesCssUrl}"/>
<spring:url var="datatablesI18NUrl" value="/javaScript/dataTables/media/i18n/${portal.locale.language}.json"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/dataTables/dataTables.bootstrap.min.css"/>

${portal.toolkit()}

<link href="${pageContext.request.contextPath}/static/ulisboaspecifications/css/dataTables.responsive.css" rel="stylesheet"/>
<script src="${pageContext.request.contextPath}/static/ulisboaspecifications/js/dataTables.responsive.js"></script>
<link href="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/css/dataTables.tableTools.css" rel="stylesheet"/>
<script src="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/js/dataTables.tableTools.js"></script>
<link href="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/css/select2.min.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/js/select2.min.js"></script>                        
<script type="text/javascript" src="${pageContext.request.contextPath}/webjars/bootbox/4.4.0/bootbox.js" ></script>
<script src="${pageContext.request.contextPath}/static/ulisboaspecifications/js/omnis.js"></script>

<%-- TITLE --%>
<div class="page-header">
    <h1>
        <spring:message
            code="label.manageServiceRequestSlots.createServiceRequestSlot" />
        <small></small>
    </h1>
</div>

<%-- NAVIGATION --%>
<div class="well well-sm" style="display: inline-block">
    <span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a
        class=""
        href="${pageContext.request.contextPath}<%= ServiceRequestSlotController.SEARCH_URL %>"><spring:message
            code="label.event.back" /></a> &nbsp;
</div>
<c:if test="${not empty infoMessages}">
    <div class="alert alert-info" role="alert">

        <c:forEach items="${infoMessages}" var="message">
            <p>
                <span class="glyphicon glyphicon glyphicon-ok-sign"
                    aria-hidden="true">&nbsp;</span> ${message}
            </p>
        </c:forEach>
    </div>
</c:if>
<c:if test="${not empty warningMessages}">
    <div class="alert alert-warning" role="alert">

        <c:forEach items="${warningMessages}" var="message">
            <p>
                <span class="glyphicon glyphicon-exclamation-sign"
                    aria-hidden="true">&nbsp;</span> ${message}
            </p>
        </c:forEach>
    </div>
</c:if>
<c:if test="${not empty errorMessages}">
    <div class="alert alert-danger" role="alert">

        <c:forEach items="${errorMessages}" var="message">
            <p>
                <span class="glyphicon glyphicon-exclamation-sign"
                    aria-hidden="true">&nbsp;</span> ${message}
            </p>
        </c:forEach>
    </div>
</c:if>

<form name='form' method="post" class="form-horizontal">
    <div class="panel panel-default">
        <div class="panel-body">
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message
                        code="label.ServiceRequestSlot.code" />
                </div>

                <div class="col-sm-10">
                    <input id="serviceRequestSlot_code" class="form-control" type="text" name="code" value='<c:out value='${param.code}'/>' required />
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.ServiceRequestSlot.uiComponentType" />
                </div>

                <div class="col-sm-10">
                    <select id="serviceRequestSlot_uicomponenttype"
                        class="js-example-basic-single" name="uicomponenttype"
                        required>
                        <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%>
                    </select>
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.ServiceRequestSlot.label" />
                </div>

                <div class="col-sm-10">
                    <input id="serviceRequestSlot_label" class="form-control" type="text" name="label" bennu-localized-string value='${not empty param.label ? param.label : "{}" } ' />
                </div>
            </div>            
        </div>
        <div class="panel-footer">
            <input type="submit" class="btn btn-default" role="button"
                value="<spring:message code="label.submit" />" />
        </div>
    </div>
</form>
<script>
$(document).ready(function() {
    <%-- Block for providing administrativeOffice options --%>
    <%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
    var sortFunction = function(a,b) { return a.text.localeCompare(b.text) };

    uiComponentType_options = [
        <c:forEach items="${uiComponentTypeList}" var="element"> 
            {
                text : "<c:out value='${element.descriptionI18N.content}'/>",  
                id : "<c:out value='${element}'/>"
            },
        </c:forEach>
    ];
    
    $("#serviceRequestSlot_uicomponenttype").select2(
        {
            data : uiComponentType_options.sort(sortFunction),
        }     
    );
    
    $("#serviceRequestSlot_uicomponenttype").select2().select2('val','${param.uiComponentType }');
    });
</script>
