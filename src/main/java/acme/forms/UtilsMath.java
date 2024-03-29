
package acme.forms;

import java.util.List;

public class UtilsMath {

	public static Double getAverage(final List<Double> list) {

		return list.stream().mapToDouble(Double::doubleValue).average().orElse(0.);

	}

	public static Double getDeviation(final List<Double> list) {

		final double media = list.stream().mapToDouble(Double::doubleValue).average().orElse(0.);

		return Math.sqrt(list.stream().mapToDouble(value -> Math.pow(value - media, 2)).reduce(0, Double::sum) / list.size());

	}

	public static Double getMinimum(final List<Double> list) {

		return list.stream().mapToDouble(Double::doubleValue).min().orElse(0.);
	}

	public static Double getMaximum(final List<Double> list) {

		return list.stream().mapToDouble(Double::doubleValue).max().orElse(0.);
	}

}
