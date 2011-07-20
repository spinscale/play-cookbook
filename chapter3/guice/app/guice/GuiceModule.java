package guice;

import com.google.inject.AbstractModule;

public class GuiceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(EncryptionService.class).to(EncryptionServiceImpl.class);
	}
}
