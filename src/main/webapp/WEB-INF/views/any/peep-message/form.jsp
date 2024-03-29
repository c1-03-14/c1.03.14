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

	<acme:input-textbox code="any.peep.form.label.title" path="title" />
	<acme:input-textbox code="any.peep.form.label.message" path="message" />
	<acme:input-moment code="any.peep.form.label.instantiation"
		path="instantiation" readonly="true"/>
	<acme:input-url code="any.peep.form.label.link" path="link" />
	<acme:input-email code="any.peep.form.label.email" path="email" />
	<acme:input-textbox code="any.peep.form.label.nickname" path="nickname" />
	<jstl:choose>
		<jstl:when test="${_command == 'publish' }">
			<acme:submit code="any.peep.form.button.apply"
				action="/any/peep-message/publish" />
		</jstl:when>
	</jstl:choose>
</acme:form>