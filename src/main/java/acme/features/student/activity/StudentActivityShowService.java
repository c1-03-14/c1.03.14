
package acme.features.student.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.enrolments.Activity;
import acme.entities.enrolments.Activity.ActivityType;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentActivityShowService extends AbstractService<Student, Activity> {

	@Autowired
	protected StudentActivityRepository repository;

	// AbstractService interface ----------------------------------------------


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

		status = this.repository.findStudentByActivity(masterId).getId() == principal.getActiveRoleId();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Activity object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneActivityById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void unbind(final Activity object) {
		assert object != null;
		Tuple tuple;
		boolean finalised = false;

		SelectChoices activityTypes;

		activityTypes = SelectChoices.from(ActivityType.class, object.getActivityType());

		if (object.getEnrolment().getHolderName() != null && !object.getEnrolment().getHolderName().isEmpty())
			finalised = true;

		tuple = super.unbind(object, "title", "abstractField", "startPeriod", "endPeriod", "link");
		tuple.put("finalised", finalised);
		tuple.put("activityType", activityTypes.getSelected().getKey());
		tuple.put("activityTypes", activityTypes);
		super.getResponse().setData(tuple);

	}
}
