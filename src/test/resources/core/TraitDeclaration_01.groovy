package core

trait A {}
trait B<T> {}
trait C<T extends A> {}
trait D<T extends A & B> {}
trait E<T extends A & B & C> {}
trait F<T extends A & B & C> extends A {}
trait F2 extends A<T> {}
trait G1<T extends A & B & C> extends A implements X {}
trait G2<T extends A & B & C> extends A<T> implements X<T> {}
trait G3                      extends A<T> implements X<T> {}
trait G4                      extends A    implements X<T> {}
trait G5                      extends A    implements X    {}
trait G2<T extends A & B & C> extends A<T> implements X<T> {}
trait H<T extends A & B & C> extends A implements X, Y {}
trait I<T extends A & B & C> extends A implements X, Y, Z {}
public trait J<T extends A & B & C> extends A implements X, Y, Z {}
@Test2 public trait K<T extends A & B & C> extends A implements X, Y, Z {}
@Test2 @Test3 public trait L<T extends A & B & C> extends A implements X, Y, Z {}

@Test2
@Test3
public
trait M
<
        T extends
        A &
        B &
        C
>
extends
A
implements
X,
Y,
Z
        {

        }