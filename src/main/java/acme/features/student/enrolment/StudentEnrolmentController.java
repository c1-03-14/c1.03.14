
package acme.features.student.enrolment;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.enrolments.Enrolment;
import acme.framework.controllers.AbstractController;
import acme.roles.Student;

@Controller
public class StudentEnrolmentController extends AbstractController<Student, Enrolment> {

	@Autowired
	protected StudentEnrolmentListMineService	listMineService;

	@Autowired
	protected StudentEnrolmentShowService		showService;

	@Autowired
	protected StudentEnrolmentCreateService		createService;

	@Autowired
	protected StudentEnrolmentUpdateService		updateService;

	@Autowired
	protected StudentEnrolmentDeleteService		deleteService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addCustomCommand("list-mine", "list", this.listMineService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
	}

}
