
package acme.features.student.lecture;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.lecture.Lecture;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;
import acme.roles.Student;

@Service
public class StudentLectureListByCourseService extends AbstractService<Student, Lecture> {

	@Autowired
	protected StudentLectureRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Lecture> objects;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		objects = this.repository.findLecturesByCourse(masterId);

		super.getBuffer().setData(objects);
	}

	@Override
	public void unbind(final Lecture object) {
		assert object != null;
		Lecturer lecturer;

		lecturer = this.repository.findOneLecturerByLectureId(object.getId());

		Tuple tuple;

		tuple = super.unbind(object, "title");
		tuple.put("almaMater", lecturer.getAlmaMater());

		super.getResponse().setData(tuple);
	}
}
