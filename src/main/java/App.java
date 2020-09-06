import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import module.JettyModule;
import module.RestEasyModule;
import module.TestResourceModule;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;

public class App {

  private static final String CONTEXT_ROOT = "/";

  private final GuiceFilter filter;
  private final GuiceResteasyBootstrapServletContextListener listener;

  @Inject
  public App(GuiceFilter filter,
      GuiceResteasyBootstrapServletContextListener listener) {
    this.filter = filter;
    this.listener = listener;
  }

  private static Injector setup() {
    return Guice.createInjector(
        new JettyModule(),
        new RestEasyModule(),
        new TestResourceModule());
  }

  public void run() throws Exception {
    System.out.println("Starting...");

    final int port = 9001;
    Server server = new Server(port);

    // Setup the basic Application "context" at "/".
    // This is also known as the handler tree (in Jetty speak).
    final ServletContextHandler context = new ServletContextHandler(server, CONTEXT_ROOT);

    // guice filter
    FilterHolder filterHolder = new FilterHolder(filter);
    context.addFilter(filterHolder,  "/*", null);

    // rest easy listen for requests
    context.addEventListener(listener);

    // fall back to default servlet if cannot find endpoint in guice
    context.addServlet(DefaultServlet.class, CONTEXT_ROOT);

    server.setHandler(context);

    server.start();
    server.join();

    System.out.println("Stopping...");
  }

  public static void main(String[] args) throws Exception {
    setup().getInstance(App.class).run();
  }
}
