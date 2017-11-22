package gwt.mvp4g.client.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>This annotation is used to annotate a slot inside the shell of a mvp4g appplication.</p>
 * <br><br>
 * <p>A slot represnets a area inside the shell which accepts widgets. At least one slot
 * per shell must be defined.
 * <br/><br/>
 * It is possible to define more the one slots in a shell.</p>
 * <br/><br/>
 * The annotation has the following attributes:
 * <ul>
 * <li>slotId: unique application id used to identify the slot</li>
 * <li>history: defines whether changes on the slot will change the bookmarkle URL. (Default is false)</li>
 * </ul>
 *
 * @author Frank Hossfeld
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Slot {

  String slotId();

  boolean history() default false;

}
