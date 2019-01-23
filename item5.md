## 자원을 직접 명시하지 말고 의존 객체 주입을 사용하라.

잘못된 예
```java
// 정적 유틸리티를 잘못사용한 예 - 유연하지 않고 테스트하기 어렵다.
public class SpellChecker{
	private static final Lexicon dictionary = ...;
    
    private SpellChecker() {} //객체 생성 방지
    public static boolean isValid(String word){ ... }
    public static List<String> suggestions(String typo){ ... }
}
// 싱글턴을 잘못 사용한 예 - 유연하지 않고 테스트하기 어려움
public class SpellChecker{
	private final Lexicon dictionary = ...;
    
    private SpellChecker() {} //객체 생성 방지
    public static SpellChecker INSTANCE = new SpellChecker(...);
    public static boolean isValid(String word){ ... }
    public static List<String> suggestions(String typo){ ... }
}
```

**문제점이 무엇일까?**

- 오직 하나만 사용한다는 가정하에 작성되었다.
- 멀티스레드 환경에서는 쓸 수 없다.
- 사용하는 자원에 따라 동작이 달라지는 클래스에는 정적 유틸리티 클래스나 싱글턴 방식이 적합하지 않다.



**그렇다면 어떻게 해결해야될까?**

 : *클래스(SpellChecker)가 여러 자원 인스턴스를 지원해야 하며, 클라이언트가 원하는 자원(dictionary)을 사용해야 한다.*

즉,

**인스턴스를 생성할 때 생성자에 필요한 자원을 넘겨주는 방식**

```java
public class SpellChecker{
	private final Lexicon dictionary;
    
    private SpellChecker(Lexicon dictionary) {
        this.dictionary = Objects.requireNonNull(dictionary);
    }
    
    public static boolean isValid(String word){ ... }
    public static List<String> suggestions(String typo){ ... }
}
```



**의존 객체 주입**은 생성자, 정적 팩터리, 빌더 모두에 똑같이 응용 가능하다.

조금 더 응용된 패턴으로, 팩터리 메서드 패턴을 말할 수 있다. 이는

**Supplier\<T> 인터페이스**가 팩터리를 표현한 완벽한 예

```java
@FunctionalInterface
public interface Supplier<T> {

    /**
     * Gets a result.
     *
     * @return a result
     */
    T get();
}
```

