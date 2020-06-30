package com.social.utils;

import java.math.BigDecimal;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class);

	private CommonUtils() {
		throw new IllegalStateException("Utility class");

	}

	public static boolean isNull(Long value) {
		return (value == null || value == 0);
	}

	public static boolean isNull(String value) {
		return (value == null || value.trim().length() == 0);
	}

	public static boolean isNotNull(Long value) {
		return (value != null && value >= 0);
	}

	public static boolean isNotNull(String value) {
		return (value != null && value.trim().length() > 0);
	}

	public static boolean isNull(BigDecimal value) {
		return (value == null || value.longValue() == 0);
	}

	public static boolean isNull(Integer value) {
		return (value == null || value == 0);
	}

	public static boolean isNull(Double value) {
		return (value == null || value == 0);
	}

	public static boolean isNotNull(Double value) {
		return (value != null && value > 0);
	}

	public static Long getLongValue(String value) {

		Long returnValue = 0L;
		try {
			if (value != null) {
				returnValue = Long.valueOf(value);
			}
		} catch (NumberFormatException nfe) {
			LOGGER.error(nfe.getMessage(), nfe);

		}
		return returnValue;
	}

	public static Integer getIntegerValue(String value) {

		Integer returnValue = 0;
		try {
			if (value != null) {
				returnValue = Integer.valueOf(value);
			}
		} catch (NumberFormatException nfe) {
			LOGGER.error(nfe.getMessage(), nfe);
		}
		return returnValue;
	}

	public static Double getDoubleValue(String value) {

		Double returnValue = 0D;
		try {
			if (value != null) {
				returnValue = Double.valueOf(value);
			}
		} catch (NumberFormatException nfe) {
			LOGGER.error(nfe.getMessage(), nfe);
		}
		return returnValue;
	}

	public static Double checkDoubleValue(Double value) {

		Double returnValue = 0D;
		try {
			if (value != null) {
				returnValue = value;
			}
		} catch (NumberFormatException nfe) {
			LOGGER.error(nfe.getMessage(), nfe);
		}
		return returnValue;
	}

	public static <T> boolean isEmpty(Collection<T> collection) {

		if ((collection == null) || collection.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
}
