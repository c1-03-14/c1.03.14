
package acme.entities.practicumSessions;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.URL;

import acme.entities.practicums.Practicum;
import acme.framework.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PracticumSession extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Size(max = 75)
	protected String			title;

	@NotBlank
	@Size(max = 100)
	protected String			abstractText;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	protected Date				startDate;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	protected Date				endDate;

	@URL
	protected String			link;

	protected boolean			isAddendum;

	// Derived attributes -----------------------------------------------------


	@Transient
	public Boolean isDraftMode() {
		return this.practicum.isDraftMode();
	}

	// Relationships ----------------------------------------------------------


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	protected Practicum practicum;

}
