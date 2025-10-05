enum Tree[A]:
    case Leaf(value: A)
    case Node(left: Tree[A], right: Tree[A])

    // def size: Int =
    //     this match
    //         case Leaf(value) => 1
    //         case Node(left, right) =>
    //             left.size + right.size
    
    // def contains(elem: A): Boolean =
    //     this match
    //         case Leaf(value) => value == elem
    //         case Node(left, right) =>
    //             left.contains(elem) || right.contains(elem)

    // def map[B](f: A => B): Tree[B] =
    //     this match
    //         case Leaf(value) => Leaf(f(value))
    //         case Node(left, right) =>
    //             Node(left.map(f), right.map(f))
        
    def fold[B](leaf: A => B, f: (B, B) => B): B =
        this match
            case Leaf(value) => leaf(value)
            case Node(left, right) =>
                f(left.fold(leaf, f), right.fold(leaf, f))

    def size: Int = fold(_ => 1, _ + _)
    def contains(value: A): Boolean = fold(_ == value, _ || _)
    def map[B](f: A => B): Tree[B] = fold(l => Leaf(f(l)), (l, r) => Node(l, r))
        
assert(Tree.Leaf("A").size == 1)
assert(Tree.Node(Tree.Leaf("A"), Tree.Leaf("A")).size == 2)
assert(Tree.Node(Tree.Node(Tree.Leaf("A"), Tree.Leaf("A")), Tree.Leaf("A")).size == 3)
assert(Tree.Node(Tree.Leaf("A"), Tree.Node(Tree.Leaf("A"), Tree.Leaf("A"))).size == 3)

assert(Tree.Leaf("A").contains("A"))
assert(!Tree.Leaf("A").contains("B"))
assert(Tree.Node(Tree.Leaf("A"), Tree.Leaf("B")).contains("A"))
assert(!Tree.Node(Tree.Leaf("A"), Tree.Leaf("B")).contains("C"))
assert(Tree.Node(Tree.Node(Tree.Leaf("A"), Tree.Leaf("B")), Tree.Leaf("C")).contains("C"))
assert(!Tree.Node(Tree.Node(Tree.Leaf("A"), Tree.Leaf("B")), Tree.Leaf("C")).contains("D"))
assert(Tree.Node(Tree.Leaf("A"), Tree.Node(Tree.Leaf("B"), Tree.Leaf("C"))).contains("B"))
assert(!Tree.Node(Tree.Leaf("A"), Tree.Node(Tree.Leaf("B"), Tree.Leaf("C"))).contains("D"))

assert(Tree.Leaf("A").map(_ => "B").contains("B"))
assert(!Tree.Leaf("A").map(_ => "B").contains("A"))
assert(Tree.Leaf("A").map(_ => 1).contains(1))
