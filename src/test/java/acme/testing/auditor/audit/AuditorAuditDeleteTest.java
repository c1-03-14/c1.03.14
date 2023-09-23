
package acme.testing.auditor.audit;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.audit.Audit;
import acme.testing.TestHarness;

public class AuditorAuditDeleteTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorAuditTestRepository repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/audit/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int index, final String code, final String conclusion, final String strongPoints, final String weakPoints, final String courseTitle, final String isPublished) {

		super.signIn("auditor1", "auditor1");
		super.clickOnMenu("Auditor", "List my audits");
		super.checkListingExists();

		super.clickOnButton("Create audit");
		super.checkFormExists();
		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("conclusion", conclusion);
		super.fillInputBoxIn("strongPoints", strongPoints);
		super.fillInputBoxIn("weakPoints", weakPoints);
		super.fillInputBoxIn("course", courseTitle);
		super.clickOnSubmit("Create audit");

		super.clickOnMenu("Auditor", "List my audits");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(index, 0, code);
		super.checkColumnHasValue(index, 2, isPublished);
		super.checkColumnHasValue(index, 3, courseTitle);

		super.clickOnListingRecord(index);
		super.checkFormExists();

		super.clickOnSubmit("Delete audit");

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/audit/delete-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int index, final String code, final String mark, final String isPublished, final String courseTitle) {
		super.signIn("auditor1", "auditor1");
		super.clickOnMenu("Auditor", "List my audits");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(index, 0, code);
		super.checkColumnHasValue(index, 1, mark);
		super.checkColumnHasValue(index, 2, isPublished);
		super.checkColumnHasValue(index, 3, courseTitle);

		super.clickOnListingRecord(index);
		super.checkFormExists();

		super.checkNotSubmitExists("Delete audit");

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		Collection<Audit> audits;
		String param;

		audits = this.repository.findManyAuditsByAuditorUsername("auditor1");

		for (final Audit audit : audits) {
			param = String.format("id=%d", audit.getId());

			super.checkLinkExists("Sign in");
			super.request("/auditor/audit/delete", param);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/auditor/audit/delete", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("company1", "company1");
			super.request("/auditor/audit/delete", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("student1", "student1");
			super.request("/auditor/audit/delete", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("auditor2", "auditor2");
			super.request("/auditor/audit/delete", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("lecturer1", "lecturer1");
			super.request("/auditor/audit/delete", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("assistant1", "assistant1");
			super.request("/auditor/audit/delete", param);
			super.checkPanicExists();
			super.signOut();

			if (audit.isPublished()) {
				super.signIn("auditor1", "auditor1");
				super.request("/auditor/audit/delete", param);
				super.checkPanicExists();
				super.signOut();
			}
		}
	}
}
