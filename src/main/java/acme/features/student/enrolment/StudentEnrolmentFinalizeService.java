
package acme.features.student.enrolment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.entities.enrolments.Enrolment;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentEnrolmentFinalizeService extends AbstractService<Student, Enrolment> {

	@Autowired
	protected StudentEnrolmentRepository repository;

	// AbstractService<Employer, Job> -------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		final boolean status;
		final Principal principal;
		final int masterId;

		masterId = super.getRequest().getData("id", int.class);
		principal = super.getRequest().getPrincipal();

		status = this.repository.findOneEnrolmentById(masterId).getStudent().getId() == principal.getActiveRoleId();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Enrolment object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneEnrolmentById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Enrolment object) {
		assert object != null;
		int studentId;
		Student student;
		int courseId;
		Course course;
		studentId = super.getRequest().getPrincipal().getActiveRoleId();
		student = this.repository.findOneStudentById(studentId);
		courseId = super.getRequest().getData("course", int.class);
		course = this.repository.findOneCourseById(courseId);
		super.bind(object, "holderName", "lowerNibble");
		object.setLowerNibble(this.getRequest().getData("creditCard", String.class).substring(this.getRequest().getData("creditCard", String.class).length() - 4));
		object.setHolderName(this.getRequest().getData("holderName", String.class));
	}

	@Override
	public void validate(final Enrolment object) {
		assert object != null;

		/*
		 * if (!super.getBuffer().getErrors().hasErrors("deadline")) {
		 * Date minimumDeadline;
		 * 
		 * minimumDeadline = MomentHelper.deltaFromCurrentMoment(7, ChronoUnit.DAYS);
		 * super.state(MomentHelper.isAfter(object.getDeadline(), minimumDeadline), "deadline", "employer.job.form.error.too-close");
		 * }
		 */

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Enrolment existing;

			existing = this.repository.findOneEnrolmentByCode(object.getCode());
			super.state(existing == null || existing.equals(object), "code", "employer.job.form.error.duplicated");
		}

		//if (!super.getBuffer().getErrors().hasErrors("salary"))
		//super.state(object.getSalary().getAmount() > 0, "salary", "employer.job.form.error.negative-salary");
	}

	@Override
	public void perform(final Enrolment object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Enrolment object) {
		assert object != null;

		//int studentId;
		Collection<Course> courses;
		SelectChoices choices;
		Tuple tuple;

		//studentId = super.getRequest().getPrincipal().getActiveRoleId();
		courses = this.repository.findAllCourses();
		choices = SelectChoices.from(courses, "title", object.getCourse());

		tuple = super.unbind(object, "code", "motivation", "goals", "workTime");
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);

		super.getResponse().setData(tuple);
	}

}
