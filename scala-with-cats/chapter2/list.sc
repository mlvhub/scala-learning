enum MyList[A]:
    case Empty()
    case Pair(_head: A, _tail: MyList[A])

    def isEmpty: Boolean =
        this match
            case Empty() => true
            case _ => false

    def head: A =
        this match
            case Pair(head, _) => head

    def tail: MyList[A] =
        this match
            case Pair(_, tail) => tail
        
    def map[B](f: A => B): MyList[B] =
        this match
            case Empty() => Empty()
            case Pair(head, tail) => Pair(f(head), tail.map(f))
        
object MyList:
    def unfold[A, B](seed: A)(stop: A => Boolean, f: A => B, next: A => A): MyList[B] =
        if stop(seed) then MyList.Empty()
        else MyList.Pair(f(seed), unfold(next(seed))(stop, f, next))

    def fill[A](n: Int)(elem: => A): MyList[A] =
        unfold(n)(_ == 0, _ => elem, _ - 1)

    def iterate[A](start: A, len: Int)(f: A => A): MyList[A] =
        val stop = (pair: (A, Int)) => pair._2 == 0
        val newF = (pair: (A, Int)) => pair._1
        val next = (pair: (A, Int)) => (f(pair._1), pair._2 - 1)
        unfold((start, len))(stop, newF, next)

    def map[A, B](list: MyList[A], f: A => B): MyList[B] =
        unfold(list)(
            _.isEmpty,
            ls => f(ls.head),
            _.tail
        )

assert(MyList.iterate(1, 2)(_ * 2) == MyList.Pair(1, MyList.Pair(2, MyList.Empty())))
