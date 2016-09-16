package com.redhat.cep.util;

import com.Ostermiller.util.LabeledCSVParser;

public interface ObjectCreator {

	public Object newInstance(final LabeledCSVParser parser) throws Exception;
}
