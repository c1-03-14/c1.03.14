
package acme.features.company.practicum;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.entities.practicumSessions.PracticumSession;
import acme.entities.practicums.Practicum;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyPracticumDeleteService extends AbstractService<Company, Practicum> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyPracticumRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {

		boolean status;
		int practicumId;
		Practicum practicum;
		Principal principal;

		practicumId = super.getRequest().getData("id", int.class);
		practicum = this.repository.findOnePracticumById(practicumId);
		principal = super.getRequest().getPrincipal();
		status = practicum != null && practicum.isDraftMode() && practicum.getCompany().getId() == principal.getActiveRoleId();

		super.getResponse().setAuthorised(status);

		//		boolean status;
		//		int practicumId;
		//		Practicum practicum;
		//		Company company;
		//
		//		practicumId = super.getRequest().getData("id", int.class);
		//		practicum = this.repository.findOnePracticumById(practicumId);
		//		company = practicum == null ? null : practicum.getCompany();
		//		status = practicum != null && practicum.isDraftMode() && super.getRequest().getPrincipal().hasRole(company);
		//
		//		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Practicum object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOnePracticumById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Practicum object) {
		assert object != null;

		super.bind(object, "code", "title", "abstractText", "goals");

	}

	@Override
	public void validate(final Practicum object) {
		assert object != null;

		super.state(object.isDraftMode(), "*", "company.practicum.form.error.not-draft-mode");
	}

	@Override
	public void perform(final Practicum object) {
		assert object != null;

		Collection<PracticumSession> practicumSessions;

		practicumSessions = this.repository.findManyPracticumSessionsByPracticumId(object.getId());
		this.repository.deleteAll(practicumSessions);
		this.repository.delete(object);
	}

	@Override
	public void unbind(final Practicum object) {
		assert object != null;

		Tuple tuple;
		Collection<Course> courses;
		Collection<PracticumSession> practicumSession;
		String estimatedTotalTime;
		SelectChoices choices;

		courses = this.repository.findManyPublishedHandsOnCourses();
		choices = SelectChoices.from(courses, "code", object.getCourse());

		practicumSession = this.repository.findManyPracticumSessionsByPracticumId(object.getId());
		estimatedTotalTime = object.getEstimatedTotalTime(practicumSession);

		tuple = super.unbind(object, "code", "title", "abstractText", "goals", "draftMode");
		tuple.put("courseCode", this.repository.findCourseCodeByPracticumId(object.getId()));
		tuple.put("estimatedTotalTime", estimatedTotalTime);
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);

		super.getResponse().setData(tuple);

	}

}
