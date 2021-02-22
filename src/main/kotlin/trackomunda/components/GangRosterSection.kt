package trackomunda.components

import react.RBuilder
import react.RComponent
import react.RProps
import react.RState

class GangRosterSection : RComponent<RProps, RState>() {
    override fun RBuilder.render() {

    }


}


fun RBuilder.gangRoster() = child(GangRosterSection::class) {}