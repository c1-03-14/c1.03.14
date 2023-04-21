
package acme.entities.enrolments;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import acme.entities.course.Course;
import acme.framework.data.AbstractEntity;
import acme.roles.Student;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Enrolment extends AbstractEntity {

	protected static final long	serialVersionUID	= 1L;

	@NotBlank
	@Pattern(regexp = "^[A-Z]{1,3}[0-9]{3}$")
	@Column(unique = true)
	protected String			code;

	@NotBlank
	@Size(max = 75)
	protected String			motivation;

	@NotBlank
	@Size(max = 100)
	protected String			goals;

	protected String			holderName;

	@NotNull
	@Valid
	protected String			lowerNibble;

	@ManyToOne(optional = false)
	protected Student			student;

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	protected Course			course;
}
