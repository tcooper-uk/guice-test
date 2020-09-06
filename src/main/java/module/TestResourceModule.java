package module;

import com.google.inject.AbstractModule;
import resource.TestResource;
import resource.TestResourceImpl;

public class TestResourceModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(TestResource.class).to(TestResourceImpl.class);
  }
}
