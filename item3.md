## private 생성자나 열거 타입으로 싱글턴임을 보증하라.

**Singleton 이란, 인스턴스를 오직 하나만 생성할 수 있는 클래스.**

좋은 이 패턴의 단점은

***그러나 클래스를 싱글턴으로 만들면 이를 사용하는 클라이언트를 테스트하기가 어려워질 수 있다***  
: 타입을 인터페이스로 정의한 다음 그 인터페이스를 구현해서 만든 싱글턴이 아니라면 싱글턴 인스턴스를 가짜(mock)구현으로 대체할 수 없기 때문이다.

***싱글턴을 만드는 두 가지 방식***
두 방식 모두 생성자는 private, 유일한 인스턴스에 접근할 수 있는 수단으로 public static 멤버를 하나 준비한다.

```java
// public static final 필드 방식의 싱글턴
public class Elvis {
    public static final Elvis INSTANCE = new Elvis();
    private Elvis(){ ... }
    
    public void leaveTheBuilding(){ ... }
}
```

> private Elvis(){ ... }

이 부분은 Elvis.INSTANCE를 초기화 할 때 딱 한 번만 호출.

cf ) 예외는 존재하는데, AccessibleObject.setAccessible을 사용해 private 생성자를 호출 할 수 있다.

이러한 공격을 방어하려면 생성자를 수정하여 두 번째 객체가 생성되려 할 때 예외를 던지게 한다.

```java
// 정적 팩터리 방식의 싱글턴
public class Elvis {
    *private* static final Elvis INSTANCE = new Elvis();
    private Elvis(){ ... }
    public static Elvis getInstance() { return INSTANCE; }
    
    public void leaveTheBuilding(){ ... }
}
```

첫번째 방식에 비해 두번째 방식의 첫번째 장점은

- API에 싱글턴임을 명백하게 드러내 준다. 
- 간결함

두번째 방식의 장점은

- (마음이 바뀌면) API를 바꾸지 않고도 싱글턴이 아니게 변경할 수 있다.  
  ex) 유일한 인스턴스를 반환하던 팩터리 메서드가 (예컨대)호출하는 스레드별로 다른 인스턴스를 넘겨주게 할 수 있음
- 원한다면 정적 팩터리를 제네릭 싱글턴 팩터리로 만들 수 있다는 점.
- 정적 팩터리의 메서드 참조를 공급자(supplier)로 사용할 수 있다는 점.  
  ex) Elvis::getInstance를 Supplier\<Elvis>로 사용하는 



싱글턴 클래스를 직렬화하려면 단순히 Serializable을 구현한다고 선언하는 것만으로는 부족하고, 모든 인스턴스 필드를 일시적(transient)이라고 선언하고 readResolve메서드를 제공해야 한다. 이렇게 하지 않으면 직렬화된 인스턴스를 역직렬화할 때 마다 새로운 인스턴스가 만들어진다.



2번째 코드에서 가짜 Elvis탄생을 예방하고 싶다면 Elvis클래스에 다음의 readResolve 메서드를 추가할 것.

```java
//싱글턴임을 보장해주는 readResolve 메서드
private Object readResolve(){
    //'진짜' Elvis를 반환하고, 가짜 Elvis는 가비지 컬렉터에 맡김
    return INSTANCE;
}
```

싱글턴을 만드는 마지막 방법은 열거 타입을 선언하는 것.

```java
public enum Elvis {
    INSTANCE;
    
    public void leaveTheBuilding(){ ... }
}
```

열거 타입을 활용한 싱글턴은

- 제2의 인스턴스 생성을 완벽 방지한다는 것에 의의가 있다.

단, 만들려는 싱글턴이 Enum 외의 클래스를 상속해야 한다면 이 방법은 사용할 수 없다.(열거 타입이 다른 인터페스를 구현하도록 선언할 수는 있다.)





