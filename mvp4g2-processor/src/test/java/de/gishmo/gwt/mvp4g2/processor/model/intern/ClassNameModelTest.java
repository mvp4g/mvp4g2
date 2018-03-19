package de.gishmo.gwt.mvp4g2.processor.model.intern;

import com.squareup.javapoet.ClassName;
import de.gishmo.gwt.mvp4g2.core.application.IsApplication;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ClassNameModelTest {

  private ClassNameModel classNameModel;

  @Before
  public void before() {
    classNameModel = new ClassNameModel(IsApplication.class.getCanonicalName());
  }

  @Test
  public void getClassName() {
    assertEquals(IsApplication.class.getCanonicalName(),
                 this.classNameModel.getClassName());
  }

  @Test
  public void getTypeName() {
    ClassName expectedtypeName = ClassName.get(IsApplication.class);
    assertEquals(expectedtypeName,
                 this.classNameModel.getTypeName());
  }

  @Test
  public void getPackage() {
    assertEquals(IsApplication.class.getPackage()
                                    .getName(),
                 this.classNameModel.getPackage());
  }

  @Test
  public void getSimpleName() {
    assertEquals(IsApplication.class.getPackage()
                                    .getName(),
                 this.classNameModel.getPackage());
  }

  @Test
  public void equals() {
    assertTrue(this.classNameModel.equals(new ClassNameModel(IsApplication.class.getCanonicalName())));
  }
}