package eu.vesek.formattednames;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FormattedNames implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("formattednames");

	@Override
	public void onInitialize() {
		LOGGER.info("Formatted names loaded!");
	}
}