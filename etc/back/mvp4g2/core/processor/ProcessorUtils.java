package de.gishmo.gwt.mvp4g2.old.processor;

public final class ProcessorUtils {
//
//  private final static String WRAPPER_NAME = "Wrapper";
//  private final static String IMPL_NAME = "Impl";
//
//  public static String createNameFromClass(String className) {
//    return className + ProcessorUtils.IMPL_NAME;
//  }
//
//  public static String createNameFromEnclosedTypes(TypeElement element) {
//    return element.getSimpleName() + ProcessorUtils.IMPL_NAME;
//  }
//
//  public static String createWrapperNameFromEnclosedTypes(TypeElement element) {
//    return element.getSimpleName() + ProcessorUtils.WRAPPER_NAME + ProcessorUtils.IMPL_NAME;
//  }
//
//  public static List<? extends TypeMirror> findParameterizationOf(Types types,
//                                                                  TypeMirror intfType,
//                                                                  TypeMirror subType) {
//    for (TypeMirror supertype : getFlattenedSupertypeHierarchy(types, subType)) {
//      if (supertype instanceof DeclaredType) {
//        DeclaredType parameterized = (DeclaredType) supertype;
//        if (MoreTypes.asElement(intfType).equals(parameterized.asElement())) {//dodgy bit here, forcing them raw to compare their base types, is this safe?
//          // Found the desired supertype
//          return new ArrayList<>(parameterized.getTypeArguments());//copy contents, internal impl is nuts
//          //seems too easy...
//        }
//      }
//    }
//    return null;
//  }
//
//  /**
//   * Returns all of the superclasses and superinterfaces for a given type
//   * including the type itself. The returned set maintains an internal
//   * breadth-first ordering of the type, followed by its interfaces (and their
//   * super-interfaces), then the supertype and its interfaces, and so on.
//   */
//  public static Set<TypeMirror> getFlattenedSupertypeHierarchy(Types types, TypeMirror t) {
//    List<TypeMirror>          toAdd  = new ArrayList<>();
//    LinkedHashSet<TypeMirror> result = new LinkedHashSet<>();
//    toAdd.add(t);
//    for (int i = 0; i < toAdd.size(); i++) {
//      TypeMirror type = toAdd.get(i);
//      if (result.add(type)) {
//        toAdd.addAll(types.directSupertypes(type));
//      }
//    }
//    return result;
//  }
}
