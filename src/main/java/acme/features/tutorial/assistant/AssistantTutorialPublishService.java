
package acme.features.tutorial.assistant;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.entities.session.SessionTutorial;
import acme.entities.tutorial.Tutorial;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantTutorialPublishService extends AbstractService<Assistant, Tutorial> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantTutorialRepository repository;

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

		status = super.getRequest().getPrincipal().hasRole(Assistant.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Tutorial tutorial;
		int id;
		id = super.getRequest().getData("id", int.class);
		tutorial = this.repository.findTutorialById(id);

		super.getBuffer().setData(tutorial);
	}

	@Override
	public void bind(final Tutorial tutorial) {
		assert tutorial != null;
		int courseCode;
		Course course;

		courseCode = super.getRequest().getData("course", int.class);
		course = this.repository.findCourseById(courseCode);

		super.bind(tutorial, "title", "abstractMessage", "goals", "estimatedTotalTime", "isPublished");
		tutorial.setCourse(course);

		Collection<SessionTutorial> sessions;

		sessions = this.repository.findSessionsOfTutorial(tutorial);
		tutorial.setPublished(!sessions.isEmpty());
	}

	@Override
	public void validate(final Tutorial tutorial) {
		assert tutorial != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Tutorial existing;

			existing = this.repository.findTutorialByCode(tutorial.getCode());
			super.state(existing == null || existing.equals(tutorial), "code", "assistant.tutorial.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors()) {
			Collection<SessionTutorial> sessions;

			sessions = this.repository.findSessionsOfTutorial(tutorial);
			super.state(!sessions.isEmpty(), "code", "assistant.tutorial.form.error.no-sessions-on-publish");

		}

	}

	@Override
	public void perform(final Tutorial tutorial) {
		assert tutorial != null;

		this.repository.save(tutorial);
		Collection<SessionTutorial> sessions;

		sessions = this.repository.findSessionsOfTutorial(tutorial);
		for (final SessionTutorial session : sessions)
			session.setDraftMode(false);
		this.repository.saveAll(sessions);
	}

	@Override
	public void unbind(final Tutorial tutorial) {
		assert tutorial != null;

		Tuple tuple;
		Collection<Course> courses;
		SelectChoices choices;

		courses = this.repository.findAllCourses();
		choices = SelectChoices.from(courses, "code", tutorial.getCourse());
		tuple = super.unbind(tutorial, "code", "title", "abstractMessage", "goals", "estimatedTotalTime", "isPublished");

		tuple.put("course", choices.getSelected().getLabel());
		tuple.put("courses", choices);

		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}

}
