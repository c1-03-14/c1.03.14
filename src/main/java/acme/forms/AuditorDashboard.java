
package acme.forms;

import acme.framework.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditorDashboard extends AbstractForm {

	protected static final long	serialVersionUID	= 1L;

	int							totalAuditsTheory, totalAuditsHandOn;
	int							minimumAuditRecords, maximumAuditRecords;
	double						minimumPeriodLengthAuditRecords, maximumPeriodLengthAuditRecords;
	double						averageAuditRecords, standardDeviationAuditRecords;
	double						averagePeriodLengthAuditRecords, standardPeriodLengthDeviationAuditRecords;
}
