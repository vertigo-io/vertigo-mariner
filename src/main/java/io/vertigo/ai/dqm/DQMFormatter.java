package io.vertigo.ai.dqm;

import io.vertigo.basics.formatter.FormatterBoolean;
import io.vertigo.basics.formatter.FormatterDate;
import io.vertigo.basics.formatter.FormatterNumber;
import io.vertigo.basics.formatter.FormatterString;
import io.vertigo.core.lang.BasicType;
import io.vertigo.datamodel.structure.definitions.Formatter;
import io.vertigo.datamodel.structure.definitions.FormatterException;

/**
 * Default formatter for boolean, number, date and string.
 * It's possible to override default formatting args by registering specifics parameters with a conventional name.
 * fmtStringDefaultArgs, fmtLocalDateDefaultArgs, fmtInstantDefaultArgs, fmtBooleanDefaultArgs, fmtNumberDefaultArgs
 *
 * @author pchretien, npiedeloup
 */
public final class DQMFormatter implements Formatter {

	private final Formatter booleanFormatter;
	private final Formatter numberformatter;
	private final Formatter localDateFormater;
	private final Formatter instantFormater;
	private final Formatter stringFormatter;

	/**
	 * Constructor.
	 */
	public DQMFormatter() {
		booleanFormatter = obtainFormatterBoolean();
		numberformatter = obtainFormatterNumber();
		localDateFormater = obtainFormatterLocalDate();
		instantFormater = obtainFormatterInstant();
		stringFormatter = obtainFormatterString();
	}

	/**
	 *
	 * @param dataType Type
	 * @return Formatter simple utilisé.
	 */
	public Formatter getFormatter(final BasicType dataType) {
		switch (dataType) {
			case String:
				return stringFormatter;
			case LocalDate:
				return localDateFormater;
			case Instant:
				return instantFormater;
			case Boolean:
				return booleanFormatter;
			case Integer:
			case Long:
			case Double:
			case BigDecimal:
				return numberformatter;
			case DataStream:
			default:
				throw new IllegalArgumentException(dataType + " n'est pas géré par ce formatter");
		}
	}

	/** {@inheritDoc} */
	@Override
	public String valueToString(final Object objValue, final BasicType dataType) {
		return getFormatter(dataType).valueToString(objValue, dataType);
	}

	/** {@inheritDoc} */
	@Override
	public Object stringToValue(final String strValue, final BasicType dataType) throws FormatterException {
		return getFormatter(dataType).stringToValue(strValue, dataType);
	}

	private static Formatter obtainFormatterBoolean() {
		return new FormatterBoolean("true; false");
	}

	private static Formatter obtainFormatterNumber() {
		return new FormatterNumber("#,###.##");
	}

	private static Formatter obtainFormatterLocalDate() {
		return new FormatterDate("dd/MM/yyyy");
	}

	private static Formatter obtainFormatterInstant() {
		return new FormatterDate("dd/MM/yyyy HH:mm");
	}

	private static Formatter obtainFormatterString() {
		//Fonctionnement de base (pas de formatage)
		return new FormatterString("BASIC");
	}

}
