
package acme.features.company.practicum;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.practicums.Practicum;
import acme.framework.controllers.AbstractController;
import acme.roles.Company;

@Controller
public class CompanyPracticumController extends AbstractController<Company, Practicum> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyPracticumShowService		showService;

	@Autowired
	protected CompanyPracticumCreateService		createService;

	@Autowired
	protected CompanyPracticumUpdateService		updateService;

	@Autowired
	protected CompanyPracticumDeleteService		deleteService;

	@Autowired
	protected CompanyPracticumListMineService	listMineService;

	@Autowired
	protected CompanyPracticumPublishService	publishService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);

		super.addCustomCommand("list-mine", "list", this.listMineService);
		super.addCustomCommand("publish", "update", this.publishService);
	}

}
