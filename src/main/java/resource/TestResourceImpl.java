package resource;

import com.google.inject.Singleton;

@Singleton
public class TestResourceImpl implements TestResource {

  @Override
  public String test() {
    return "Tom test";
  }

  @Override
  public String notTest() {
    return "Not Test";
  }
}
