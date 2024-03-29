
package acme.features.auditor.audit;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.audit.Audit;
import acme.entities.audit.Mark;
import acme.entities.course.Course;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;
import acme.utils.MarkUtils;

@Service
public class AuditorAuditPublishService extends AbstractService<Auditor, Audit> {

	@Autowired
	protected AuditorAuditRepository repository;


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int auditId;
		int auditorId;
		Audit audit;

		auditorId = super.getRequest().getPrincipal().getActiveRoleId();
		auditId = super.getRequest().getData("id", int.class);
		audit = this.repository.findOneAuditById(auditId);

		status = audit != null && super.getRequest().getPrincipal().hasRole(audit.getAuditor()) && audit.getAuditor().getId() == auditorId && !audit.isPublished();
		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Audit object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneAuditById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Audit object) {
		assert object != null;

		int courseId;
		Course course;
		Collection<Mark> marksCollection;

		courseId = super.getRequest().getData("course", int.class);
		course = this.repository.findOneCourseById(courseId);

		marksCollection = this.repository.findMarksOfAuditByAuditId(object.getId());

		if (!marksCollection.isEmpty()) {
			Mark finalMark;
			finalMark = MarkUtils.getNewMark(marksCollection);

			object.setMark(finalMark);
		}

		super.bind(object, "code", "conclusion", "strongPoints", "weakPoints", "isPublished");
		object.setCourse(course);

	}

	@Override
	public void validate(final Audit object) {
		assert object != null;

		Long numberOfRecords;
		numberOfRecords = this.repository.countRecordsFromAuditById(object.getId());
		super.state(numberOfRecords > 0, "isPublished", "auditor.audit.form.error.no-records");

		if (!super.getBuffer().getErrors().hasErrors("course")) {
			final int courseId = super.getRequest().getData("course", int.class);
			final Course c = this.repository.findOneCourseById(courseId);
			super.state(c != null && c.isDraftMode(), "course", "auditor.audit.form.error.course-not-published");
		}

		if (!super.getBuffer().getErrors().hasErrors("code"))
			super.state(!object.isPublished(), "code", "auditor.audit.form.error.code-already-published");

	}

	@Override
	public void perform(final Audit object) {
		assert object != null;

		Collection<Mark> marksCollection;
		marksCollection = this.repository.findMarksOfAuditByAuditId(object.getId());
		Mark finalMark;
		finalMark = MarkUtils.getNewMark(marksCollection);

		object.setMark(finalMark);
		object.setPublished(true);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Audit object) {
		assert object != null;

		Collection<Course> courseOptions;
		SelectChoices marks, courses;
		Tuple tuple;

		marks = SelectChoices.from(Mark.class, object.getMark());

		courseOptions = this.repository.findCoursesPublished();

		courses = SelectChoices.from(courseOptions, "title", object.getCourse());

		tuple = super.unbind(object, "code", "conclusion", "strongPoints", "weakPoints", "isPublished", "mark");
		tuple.put("masterId", object.getAuditor().getId());
		tuple.put("marks", marks);
		tuple.put("course", courses.getSelected().getKey());
		tuple.put("courses", courses);

		super.getResponse().setData(tuple);
	}

}
