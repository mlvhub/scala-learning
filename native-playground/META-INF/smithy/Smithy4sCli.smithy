$version: "2"

namespace cli

@alloy#simpleRestJson // 1
service Numbers {
    operations: [
        GetNumberFact
    ]
}

@readonly
@http(method: "GET", uri: "/{number}/{type}")
operation GetNumberFact {
    input := {
        @required
        @pattern("^(([0-9]*)|(random))$") // 2
        @httpLabel
        number: String

        @required
        @httpLabel
        type: Type

        @required
        @httpQuery("json")
        json: Boolean = true
    }

    output := {
        @required
        text: String

        @required
        found: Boolean
    }
}

enum Type { // 3
    TRIVIA = "trivia"
    MATH = "math"
    DATE = "date"
    YEAR = "year"
}
