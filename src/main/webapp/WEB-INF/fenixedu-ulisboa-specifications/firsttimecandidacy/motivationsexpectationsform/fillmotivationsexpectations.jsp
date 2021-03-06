<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="datatables" uri="http://github.com/dandelion/datatables"%>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>

<spring:url var="datatablesUrl" value="/javaScript/dataTables/media/js/jquery.dataTables.latest.min.js"/>
<spring:url var="datatablesBootstrapJsUrl" value="/javaScript/dataTables/media/js/jquery.dataTables.bootstrap.min.js"></spring:url>
<script type="text/javascript" src="${datatablesUrl}"></script>
<script type="text/javascript" src="${datatablesBootstrapJsUrl}"></script>
<spring:url var="datatablesCssUrl" value="/CSS/dataTables/dataTables.bootstrap.min.css"/>

<link rel="stylesheet" href="${datatablesCssUrl}"/>
<spring:url var="datatablesI18NUrl" value="/javaScript/dataTables/media/i18n/${portal.locale.language}.json"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/dataTables/dataTables.bootstrap.min.css"/>

${portal.toolkit()}

<link href="${pageContext.request.contextPath}/static/fenixedu-ulisboa-specifications/css/dataTables.responsive.css" rel="stylesheet"/>
<script src="${pageContext.request.contextPath}/static/fenixedu-ulisboa-specifications/js/dataTables.responsive.js"></script>
<link href="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/css/dataTables.tableTools.css" rel="stylesheet"/>
<script src="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/js/dataTables.tableTools.js"></script>
<link href="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/css/select2.min.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/js/select2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/webjars/bootbox/4.4.0/bootbox.js" ></script>
<script src="${pageContext.request.contextPath}/static/fenixedu-ulisboa-specifications/js/omnis.js"></script>

<%@page import="java.util.List"%>
<%@page import="org.fenixedu.ulisboa.specifications.domain.UniversityDiscoveryMeansAnswer"%>
<%@page import="org.fenixedu.ulisboa.specifications.domain.UniversityChoiceMotivationAnswer"%>


<%-- TITLE --%>
<div class="page-header">
	<h1><spring:message code="label.firstTimeCandidacy.fillMotivationsExpectations" />
		<small></small>
	</h1>
</div>

<%-- NAVIGATION --%>
<div class="well well-sm" style="display:inline-block">
	<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a class="" href="${pageContext.request.contextPath}/fenixedu-ulisboa-specifications/firsttimecandidacy/motivationsexpectationsform/back"><spring:message code="label.back"/></a>	
</div>

	<c:if test="${not empty infoMessages}">
				<div class="alert alert-info" role="alert">
					
					<c:forEach items="${infoMessages}" var="message"> 
						<p> <span class="glyphicon glyphicon glyphicon-ok-sign" aria-hidden="true">&nbsp;</span>
  							${message}
  						</p>
					</c:forEach>
					
				</div>	
			</c:if>
			<c:if test="${not empty warningMessages}">
				<div class="alert alert-warning" role="alert">
					
					<c:forEach items="${warningMessages}" var="message"> 
						<p> <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span>
  							${message}
  						</p>
					</c:forEach>
					
				</div>	
			</c:if>
			<c:if test="${not empty errorMessages}">
				<div class="alert alert-danger" role="alert">
					
					<c:forEach items="${errorMessages}" var="message"> 
						<p> <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span>
  							${message}
  						</p>
					</c:forEach>
					
				</div>	
			</c:if>

<form method="post" class="form-horizontal">
	<div class="panel panel-default">
		<div class="panel-body">
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.MotivationsExpectationsForm.universityDiscoveryMeansAnswers" />
				</div>

				<div class="col-sm-10">
				<ul class="list-unstyled">
					<% for (UniversityDiscoveryMeansAnswer answer : (List<UniversityDiscoveryMeansAnswer>) request.getAttribute("universityDiscoveryMeansAnswers")) { %>
						<% Boolean isChecked = (Boolean) request.getAttribute("universityDiscoveryMeans_" + answer.getExternalId()); %>
						<li><input id="<%= "motivationsexpectationsform_universityDiscoveryMeans_" + answer.getExternalId() %>" type="checkbox" name="<%= "universityDiscoveryMeans_" + answer.getExternalId() %>" <%= isChecked? "checked" : "" %>>
							<label for="<%= "motivationsexpectationsform_universityDiscoveryMeans_" + answer.getExternalId() %>"><%= answer.getDescription().getContent() %></label>
						</input></li>
					<% } %>
				</ul>
				</div>
			</div>
			
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.MotivationsExpectationsForm.otherUniversityDiscoveryMeans" />
				</div>

				<div class="col-sm-10">
					<input id="motivationsexpectationsform_otherUniversityDiscoveryMeans" class="form-control" type="text" name="otherUniversityDiscoveryMeans"
						value='<c:out value='${not empty param.otheruniversitydiscoverymeans ? param.otheruniversitydiscoverymeans : motivationsexpectationsform.otherUniversityDiscoveryMeans }'/>' />
				</div>
			</div>
			
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.MotivationsExpectationsForm.universityChoiceMotivationAnswers" />
				</div>

				<div class="col-sm-10">
				<ul class="list-unstyled">
					<% for (UniversityChoiceMotivationAnswer answer : (List<UniversityChoiceMotivationAnswer>) request.getAttribute("universityChoiceMotivationAnswers")) { %>
						<% Boolean isChecked = (Boolean) request.getAttribute("universityChoiceMotivation_" + answer.getExternalId()); %>
						<li><input id="<%= "motivationsexpectationsform_universityChoiceMotivation_" + answer.getExternalId() %>" type="checkbox" name="<%= "universityChoiceMotivation_" + answer.getExternalId() %>" <%= isChecked? "checked" : "" %>>
						 	<label for="<%= "motivationsexpectationsform_universityChoiceMotivation_" + answer.getExternalId() %>"><%= answer.getDescription().getContent() %></label>
						 </input></li>
					<% } %>
				</ul>
				</div>
			</div>
			
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.MotivationsExpectationsForm.otherUniversityChoiceMotivation" />
				</div>

				<div class="col-sm-10">
					<input id="motivationsexpectationsform_otherUniversityChoiceMotivation" class="form-control" type="text" name="otherUniversityChoiceMotivation"
						value='<c:out value='${not empty param.otheruniversitychoicemotivation ? param.otheruniversitychoicemotivation : motivationsexpectationsform.otherUniversityChoiceMotivation }'/>' />
				</div>
			</div>
		</div>
		<div class="panel-footer">
			<input type="submit" class="btn btn-default" role="button" value="<spring:message code="label.submit" />" />
		</div>
	</div>
</form>

<script>
$(document).ready(function() {
});
</script>
