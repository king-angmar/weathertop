package xyz.wongs.weathertop.base.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * The class Big decimal utils.
 *
 * @author iwcngs@gmail.com
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BigDecimalUtil {

	/**
	 * Add big decimal.
	 *
	 * @param v1 the v 1
	 * @param v2 the v 2
	 *
	 * @return the big decimal
	 */
	public static BigDecimal add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2);
	}

	/**
	 * Sub big decimal.
	 *
	 * @param v1 the v 1
	 * @param v2 the v 2
	 *
	 * @return the big decimal
	 */
	public static BigDecimal sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2);
	}


	/**
	 * Mul big decimal.
	 *
	 * @param v1 the v 1
	 * @param v2 the v 2
	 *
	 * @return the big decimal
	 */
	public static BigDecimal mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2);
	}

	/**
	 * Div big decimal.
	 *
	 * @param v1 the v 1
	 * @param v2 the v 2
	 *
	 * @return the big decimal
	 */
	public static BigDecimal div(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 *
	 * @param v1	the v 1
	 * @param v2	the v 2
	 * @param scale		保留几位小数
	 * @return
	 */
	public static BigDecimal div(double v1, double v2, int scale) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP);
	}

}
