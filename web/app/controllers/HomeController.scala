package controllers

import play.api.Environment
import play.api.mvc.AbstractController
import play.api.mvc.ControllerComponents

import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject
import javax.inject.Singleton
import play.api.Mode

@Singleton
class HomeController @Inject() (cc: ControllerComponents, environment: Environment) extends AbstractController(cc) {

  private val counter = new InMemoryCounter()

  private val cachedCounter = if (environment.mode == Mode.Dev) new CachedCounter(environment.classLoader) else counter

  def index = Action {
    val count = counter.incrementAndGet()
    val cachedCount = cachedCounter.incrementAndGet()
    Ok(views.html.index(count, cachedCount))
  }

}

trait Counter {
  def incrementAndGet(): Long
}

class InMemoryCounter extends Counter {

  private val underlying = new AtomicLong(0)

  override def incrementAndGet(): Long = underlying.incrementAndGet()
}

/**
 * `CachedCounter` which use the `CounterCache` class load in parent classloader. This should be used only in Dev mode.
 *
 * In Play's Dev mode, application codes are loaded in Play's application classloader (`Environment#classLoader`). It is
 * reloaded every time the application codes are changed. This behavior is annoying when you are using in-memory cache
 * since the cache is also reloaded and cleared every time you change the codes.
 *
 * This class caches data in the parent classloader of `Environment#classLoader`. The parent is `AssetsClassLoader` in
 * Dev Mode. This classloader is not reloaded when the codes are changed. Of course, The cache in the parent classloader
 * is cleared when you shutdown sbt.
 */
class CachedCounter(classLoader: ClassLoader) extends Counter {

  private val parent = classLoader.getParent()
  private val cache = parent.loadClass("com.example.CounterCache")

  override def incrementAndGet(): Long = {
    val m = cache.getMethod("incrementAndGet")
    m.invoke(null).asInstanceOf[Long]
  }

}
