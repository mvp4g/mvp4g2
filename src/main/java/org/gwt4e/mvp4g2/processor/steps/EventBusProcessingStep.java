/*
 * Copyright (C) 2015 Frank Hossfeld
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gwt4e.mvp4g2.processor.steps;

import com.google.auto.common.BasicAnnotationProcessor.ProcessingStep;
import com.google.common.collect.SetMultimap;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.gwt4e.mvp4g2.client.EventBusCore;
import org.gwt4e.mvp4g2.client.annotations.EventBus;
import org.gwt4e.mvp4g2.processor.EventBusProcessorContext;
import org.gwt4e.mvp4g2.processor.context.EventBusContext;
import org.gwt4e.mvp4g2.processor.context.EventContext;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;

public class EventBusProcessingStep
  implements ProcessingStep {

  private final Messager messager;
  private final Filer    filer;
  private final Types    types;
  private final Elements elements;

  private EventBusProcessorContext processorContext;

//------------------------------------------------------------------------------

  //  private static final ClassName PREFIX_AND_TOKEN_CLASS_NAME = ClassName.get(
//      AbstractPlaceHistoryMapper.PrefixAndToken.class);
//  private static final ClassName PLACE_TOKENIZER_CLASS_NAME = ClassName.get(PlaceTokenizer.class);
//
//  private final Messager messager;
//  private final Types types;
//  private final Elements elements;
//
//  private final TypeElement abstractPlaceHistoryMapperType;
//  private final ExecutableElement getPrefixAndTokenMethod;
//  private final VariableElement getPrefixAndTokenMethodParameter;
//  private final ExecutableElement getTokenizerMethod;
//  private final VariableElement getTokenizerMethodParameter;
//

//------------------------------------------------------------------------------

  public EventBusProcessingStep(Messager messager,
                                Filer filer,
                                Types types,
                                Elements elements,
                                EventBusProcessorContext processorContext) {
    this.messager = messager;
    this.filer = filer;
    this.types = types;
    this.elements = elements;

    this.processorContext = processorContext;

//    this.abstractPlaceHistoryMapperType = elements.getTypeElement(AbstractPlaceHistoryMapper.class.getCanonicalName());
//
//    this.getPrefixAndTokenMethod = ElementFilter.methodsIn(abstractPlaceHistoryMapperType.getEnclosedElements())
//        .stream().filter(method -> method.getSimpleName().contentEquals("getPrefixAndToken"))
//        .findFirst().get();
//    this.getPrefixAndTokenMethodParameter = getPrefixAndTokenMethod.getParameters().get(0);
//    this.getTokenizerMethod = ElementFilter.methodsIn(abstractPlaceHistoryMapperType.getEnclosedElements())
//        .stream().filter(method -> method.getSimpleName().contentEquals("getTokenizer"))
//        .findFirst().get();
//    this.getTokenizerMethodParameter = getTokenizerMethod.getParameters().get(0);
  }

//------------------------------------------------------------------------------


  public Set<? extends Class<? extends Annotation>> annotations() {
    return Collections.singleton(EventBus.class);
  }

  public void process(SetMultimap<Class<? extends Annotation>, Element> elementsByAnnotation) {
    for (Element element : elementsByAnnotation.get(EventBus.class)) {
      EventBusContext context = EventBusContext.create(messager,
                                                       types,
                                                       elements,
                                                       element);

      if (context == null) {
        continue; // error message already emitted
      } else {
        if (this.processorContext.getEventContextMap() == null ||
            this.processorContext.getEventContextMap()
                                 .size() == 0 ||
            this.processorContext.getEventContextMap()
                                 .get(context.getClassName())
                                 .size() == 0) {
          messager.printMessage(Diagnostic.Kind.ERROR,
                                String.format("%s has no events defined",
                                              context.getClassName()));
          return;
        }

//        this.processorContext.getEventBusProcessorContextMap()
//                             .put((ClassName.get((TypeElement) element).),
//                                  context)
//      if (context == null) {
//        continue; // error message already emitted
//      }
//      try {
//        generate(context);
//      } catch (IOException ioe) {
//        StringWriter sw = new StringWriter();
//        PrintWriter pw = new PrintWriter(sw);
//        pw.println("Error generating source file for type " + context.interfaceType.getQualifiedName());
//        ioe.printStackTrace(pw);
//        pw.close();
//        messager.printMessage(Diagnostic.Kind.ERROR, sw.toString());
//      }


        try {
          // generate Event
          generate(context);
//          // save context
//          this.processorContext.getEventProcessorContextMap()
//                               .put(element.getSimpleName()
//                                           .toString(),
//                                    context);
        } catch (IOException ioe) {
          StringWriter sw = new StringWriter();
          PrintWriter pw = new PrintWriter(sw);
          pw.println("Error generating source file for type "
                     //                       +
                     //                       context.interfaceType.getQualifiedName()
          );
          ioe.printStackTrace(pw);
          pw.close();
          messager.printMessage(Diagnostic.Kind.ERROR,
                                sw.toString());
        }
      }
    }
//    Set<Element> eventBusElements = elementsByAnnotation.get(EventBus.class);
//    // only one class can be annotated with @EventBus
//    if (eventBusElements.size() > 1) {
//      messager.printMessage(Diagnostic.Kind.ERROR,
//                            String.format("Found more than one class annotated with %s", EventBus.class.getCanonicalName()));
//      return;
//    }
//    // get the element, that is annotated with @EventBus
//    Element eventBusElement = (Element) eventBusElements.toArray()[0];
  }

//------------------------------------------------------------------------------

  private void generate(EventBusContext context)
    throws IOException {

    TypeSpec.Builder typeSpec = TypeSpec.classBuilder(context.getImplName())
                                        .addOriginatingElement(context.getInterfaceType())
                                        .addModifiers(Modifier.PUBLIC)
                                        .superclass(EventBusCore.class)
                                        .addSuperinterface(ClassName.get(context.getInterfaceType()));

//    for (EventContext contextEvent : this.processorContext.getEventContextMap()
//                                                          .values()) {
//      typeSpec.addMethod(createEventMethod(contextEvent));
//    }
//
//
//    JavaFile.builder(context.getPackageName(),
//                     typeSpec.build())
//            .build()
//            .writeTo(filer);
  }

  private MethodSpec createEventMethod(EventContext contextEvent) {
    MethodSpec.Builder method = MethodSpec.methodBuilder(contextEvent.getMethodName())
                                          .addAnnotation(Override.class)
                                          .addModifiers(Modifier.PUBLIC,
                                                        Modifier.FINAL)
                                          .returns(void.class);
    // method head
    for (VariableElement parameter : contextEvent.getMethod()
                                                 .getParameters()) {
      method.addParameter(ClassName.get(parameter.asType()),
                          parameter.getSimpleName()
                                   .toString());
    }

    method.addCode("this.internalEventBus.fireEvent(new $T(",
                   ClassName.get(contextEvent.getPackageNameEvents(),
                                 contextEvent.getEventClassName()));
    for (int i = 0; i < contextEvent.getMethod()
                                    .getParameters()
                                    .size(); i++) {
      VariableElement parameter = contextEvent.getMethod()
                                              .getParameters()
                                              .get(i);
      method.addCode("$N",
                     parameter.getSimpleName()
                              .toString());
      if (i < contextEvent.getMethod()
                          .getParameters()
                          .size() - 1) {
        method.addCode(", ");
      }
    }
    method.addCode("));\n");

    return method.build();
  }

//  private void generate(PlaceHistoryGeneratorContext context) throws IOException {
//    JavaFile.builder(context.packageName,
//        TypeSpec.classBuilder(context.implName)
//            // XXX: add factoryType, place types and tokenizer types as originating elements?
//            .addOriginatingElement(context.interfaceType)
//            // TODO: add @Generated annotation
//            .addModifiers(Modifier.PUBLIC)
//            .superclass(ParameterizedTypeName.get(ClassName.get(abstractPlaceHistoryMapperType),
//                context.factoryType == null ? TypeName.VOID.box() : TypeName.get(context.factoryType)))
//            .addSuperinterface(ClassName.get(context.interfaceType))
//            .addMethod(generateGetPrefixAndToken(context))
//            .addMethod(generateGetTokenizer(context))
//            .build())
//        .build()
//        .writeTo(filer);
//  }
//
//  private MethodSpec generateGetPrefixAndToken(PlaceHistoryGeneratorContext context) {
//    MethodSpec.Builder builder = MethodSpec.overriding(getPrefixAndTokenMethod);
//    for (TypeElement placeType : context.getPlaceTypes()) {
//      String prefix = context.getPrefix(placeType);
//
//      builder.addCode("if ($N instanceof $T) {\n$>$T place = ($T) $N;\n",
//          getPrefixAndTokenMethodParameter.getSimpleName(), placeType,
//          placeType, placeType, getPrefixAndTokenMethodParameter.getSimpleName());
//
//      ExecutableElement getter = context.getTokenizerGetter(prefix);
//      if (getter != null) {
//        builder.addCode("return new $T($S, this.factory.$N().getToken(place));\n",
//            PREFIX_AND_TOKEN_CLASS_NAME, prefix, getter.getSimpleName());
//      } else {
//        builder.addCode("$T t = new $T();\nreturn new $T($S, t.getToken(place));\n",
//            ParameterizedTypeName.get(PLACE_TOKENIZER_CLASS_NAME, ClassName.get(placeType)),
//            context.getTokenizerType(prefix), PREFIX_AND_TOKEN_CLASS_NAME, prefix);
//      }
//      builder.addCode("$<}\n");
//    }
//    builder.addCode("return null;");
//    return builder.build();
//  }
//
//  private MethodSpec generateGetTokenizer(PlaceHistoryGeneratorContext context) {
//    MethodSpec.Builder builder = MethodSpec.overriding(getTokenizerMethod);
//    for (String prefix : context.getPrefixes()) {
//      builder.addCode("if ($S.equals($N)) {\n$>", prefix, getTokenizerMethodParameter.getSimpleName());
//
//      ExecutableElement getter = context.getTokenizerGetter(prefix);
//      if (getter != null) {
//        builder.addCode("return this.factory.$N();\n", getter.getSimpleName());
//      } else {
//        builder.addCode("return new $T();", context.getTokenizerType(prefix));
//      }
//      builder.addCode("$<}\n");
//    }
//    builder.addCode("return null;");
//    return builder.build();
//  }
}
