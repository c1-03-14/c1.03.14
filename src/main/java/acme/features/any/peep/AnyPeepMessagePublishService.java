
package acme.features.any.peep;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.peep.PeepMessage;
import acme.framework.components.accounts.Any;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;

@Service
public class AnyPeepMessagePublishService extends AbstractService<Any, PeepMessage> {

	@Autowired
	protected AnyPeepMessageRepository repository;

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
		PeepMessage peep;
		Date moment;

		peep = new PeepMessage();
		moment = MomentHelper.getCurrentMoment();
		peep.setInstantiation(moment);

		if (super.getRequest().getPrincipal().isAuthenticated())
			peep.setNickname(super.getRequest().getPrincipal().getUsername());

		super.getBuffer().setData(peep);
	}

	@Override
	public void bind(final PeepMessage peep) {
		assert peep != null;
		super.bind(peep, "title", "message", "instantiation", "nickname", "link", "email");
	}

	@Override
	public void validate(final PeepMessage peep) {
		assert peep != null;
	}

	@Override
	public void perform(final PeepMessage peep) {
		assert peep != null;

		this.repository.save(peep);
	}

	@Override
	public void unbind(final PeepMessage peep) {
		assert peep != null;
		Tuple tuple;
		tuple = super.unbind(peep, "title", "message", "instantiation", "nickname");
		super.getResponse().setData(tuple);
	}

}
