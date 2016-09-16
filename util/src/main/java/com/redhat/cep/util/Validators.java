package com.redhat.cep.util;

import com.Ostermiller.util.LabeledCSVParser;

public class Validators {

	static public void checkNotNull(final LabeledCSVParser parser, String ...names) throws Exception
	{
		StringBuffer msg = new StringBuffer();

		for (int i = 0; i < names.length; i++) {
			//System.out.println("********** " +names[i]);
			if (parser.getValueByLabel(names[i]).isEmpty() || (parser.getValueByLabel(names[i]) == null)) {
				if (msg.length() != 0) msg.append(", ");
				msg.append(names[i]);
			}
		}
		if (msg.length() != 0) {
			throw new Exception(msg.toString() + " column(s) is/are null");
		}
		
	}

	
	static public void checkIntegers(final LabeledCSVParser parser, String ...names) throws Exception
	{
		StringBuffer msg = new StringBuffer();

		for (int i = 0; i < names.length; i++) {
			try {
				Integer.valueOf(parser.getValueByLabel(names[i]));
			} catch (NumberFormatException e) {
				if (msg.length() != 0) msg.append(", ");
				msg.append(names[i]);
			}
		}
		if (msg.length() != 0) {
			throw new Exception(msg.toString() + " column(s) is/are not interger(s)");
		}
	}

	static public void checkNotNull(Object ...names) throws Exception
	{
		StringBuffer msg = new StringBuffer();

		for (int i = 0; i < names.length; i++) {
			//System.out.println("********** " +names[i]);
			if ((names[i]) == null) {
				if (msg.length() != 0) msg.append(", ");
				msg.append(names[i]);
			}
		}
		if (msg.length() != 0) {
			throw new Exception(msg.toString() + " column(s) is/are null");
		}
	}
	
	static public void checkBooleans(final LabeledCSVParser parser, String ...names) throws Exception
	{
		StringBuffer msg = new StringBuffer();

		for (int i = 0; i < names.length; i++) {
			if (!parser.getValueByLabel(names[i]).equals("true") && !parser.getValueByLabel(names[i]).equals("false")) {
				if (msg.length() != 0) msg.append(", ");
				msg.append(names[i]);
			}
		}
		if (msg.length() != 0) {
			throw new Exception(msg.toString() + " column(s) is/are not Boolean(s)");
		}
	}

}
