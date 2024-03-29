<%--
- form.jsp
-
- Copyright (C) 2012-2023 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="student.activity.form.label.title"
		path="title" placeholder=""/>
		<acme:input-textbox code="student.activity.form.label.abstractField"
		path="abstractField" placeholder=""/>
		<acme:input-select code="student.activity.form.label.activityType" path="activityType"
		choices="${activityTypes}" />
		<acme:input-moment code="student.activity.form.label.startPeriod"
		path="startPeriod" placeholder=""/>
		<acme:input-moment code="student.activity.form.label.endPeriod"
		path="endPeriod" placeholder=""/>
	<acme:input-textbox code="student.activity.form.label.link"
		path="link" placeholder="https://ejemplo.com"/>
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete') && finalised == true}">
			<acme:submit code="student.activity.form.button.update"
				action="/student/activity/update" />
			<acme:submit code="student.activity.form.button.delete"
				action="/student/activity/delete" />
		</jstl:when>
		<jstl:when test="${_command == 'create' && finalised == true}">
			<acme:submit code="student.activity.form.button.create"
				action="/student/activity/create?enrolment=${enrolment}" />
		</jstl:when>
	</jstl:choose>
</acme:form>
