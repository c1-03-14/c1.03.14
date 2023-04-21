
package acme.features.lecturers.courses;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.entities.lecture.Lecture;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturersCoursesPublishService extends AbstractService<Lecturer, Course> {

	@Autowired
	protected LecturersCoursesRepository repository;


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Course course;

		masterId = super.getRequest().getData("id", int.class);
		course = this.repository.findOneCourseById(masterId);
		status = course != null && course.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Course object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneCourseById(id);

		super.getBuffer().setData(object);
	}
	@Override
	public void bind(final Course object) {
		assert object != null;

		super.bind(object, "code", "title", "Abstract", "retailPrice", "link");
	}

	@Override
	public void validate(final Course object) {
		assert object != null;
		if (!super.getBuffer().getErrors().hasErrors("code")) {
			final Course course;

			course = this.repository.findOneCourseByCode(object.getCode());
			super.state(course == null || course.equals(object), "code", "lecturer.course.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("*")) {
			Collection<Lecture> lectures;

			lectures = this.repository.findManyLecturesUnpublishedByCourse(object.getId());
			super.state(lectures.isEmpty(), "draftMode", "lecturer.course.form.error.lectures-unpublished");
		}
		if (!super.getBuffer().getErrors().hasErrors("*")) {
			Collection<Lecture> lectures;

			lectures = this.repository.findManyNonTheoreticalLecturesByCourseId(object.getId());
			super.state(!lectures.isEmpty(), "isTheoretical", "lecturer.course.form.error.lectures-theoretical");
		}
		object.setTheoretical(false);
	}

	@Override
	public void perform(final Course object) {
		assert object != null;

		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Course object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "code", "title", "Abstract", "retailPrice", "isTheoretical", "link", "draftMode");
		super.getResponse().setData(tuple);
	}

}