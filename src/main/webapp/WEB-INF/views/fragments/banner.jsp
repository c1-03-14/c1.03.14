<%--
- banner.jsp
-
- Copyright (C) 2012-2023 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page language="java" import="java.util.concurrent.ThreadLocalRandom"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<div class="rounded"
	style="background: <acme:message code='master.banner.background'/>">
	<img src="${bannerToDisplay.pictureLink}" id="imagenBanner"
		style="width: 100% !important;max-height: 300px"
		alt="<acme:message code='master.banner.alt'/>"
		class="img-fluid mx-auto d-block rounded" />
	<acme:message code="${bannerToDisplay.slogan}"/>
</div>